/*
package temp;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class MinMaxStock {

    private static File tempDir = new File( System.getProperty("java.io.tmpdir"));
    private static String inputDirBase = "input";
    private static String outputDirBase = "output";
    private static String consolidatedOutputFile = "results.csv";
    public static class ColMapper extends
            Mapper<Object, Text, Text, Text> {

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] cols = value.toString().replaceAll("\"", "").split(",");
            double number;
            try {
                number = Double.parseDouble(cols[2]);
                //context.write(new Text(cols[0]+"\t" +cols[1]), new DoubleWritable(number));
                String colsString = Arrays.toString(cols);
                colsString = colsString.substring(1, colsString.length()-2);
                context.write(new Text(cols[0]), new Text(colsString));
            } catch (NumberFormatException e){
                // ignore this bad record which throws this exception..
            }
        }
    }

    public static class ColReducer extends
            Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values,
                           Context context) throws IOException, InterruptedException {
            double min = Integer.MAX_VALUE, max = 0;
            String dateMin = "", dateMax = "";
            Iterator<Text> iterator = values.iterator(); //Iterating
            while (iterator.hasNext()) {

                Text valueOut = iterator.next();

                String[] cols = valueOut.toString().replaceAll("\"", "").split(",");
                double thirdCol = Double.parseDouble(cols[2]);
                if (thirdCol < min) { //Finding min value
                    min = thirdCol;
                    dateMin = cols[1];
                }
                if (thirdCol > max) { //Finding max value
                    max = thirdCol;
                    dateMax = cols[1];
                }
            }
            context.write(new Text(key), new Text(min+"\t"+ dateMin));
            context.write(new Text(key), new Text(max+"\t"+ dateMax));
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Is tmp Dir:"+ tempDir.isDirectory());
        int loop = args.length;

        Configuration conf = new Configuration();

        FileSystem fs = null;
        boolean[] jobsStatus = new boolean[loop];
        // Create a file array which will hold all the output dirs
        File[] outputDirs = new File[loop];
        for (int i = 0; i < loop ; i = i+1) {
            File inputFileDir = new File(tempDir, inputDirBase+i);
            FileUtils.copyFile(new File(args[i]), inputFileDir);
            File outputFileDir = new File(tempDir, outputDirBase+i);
            Job job = createJob(conf);
            FileInputFormat.addInputPath(job, new Path(inputFileDir.getPath()));
            FileOutputFormat.setOutputPath(job, new Path(outputFileDir.getPath()));
            jobsStatus[i] = job.waitForCompletion(true);
            // Persist each output dir in the file array.
            outputDirs[i] = outputFileDir;
        }
        // Running a consolidation program which will write all the results to single file

        boolean status =  consolidateResults(outputDirs);
        if(status){
            System.out.println("Job completed successfully...");

            cleanUpOutputDirs(outputDirs);
        } else {
            System.out.println("Job Failed... \n Please check the logs for more information.");
        }
        // Now perform clean up of all the temporary input and output folders
        cleanUpTempInputDirs();
        System.out.println("Final Results file : "+ new File(tempDir, consolidatedOutputFile).getPath());
        //System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    private static void cleanUpOutputDirs(File[] outputDirs) {
        System.out.println("Running Cleanup activity for output files and directories...");
        for(int i=0; i<outputDirs.length ; i++){
            try {
                FileUtils.deleteDirectory(outputDirs[i]);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to delete the output directory:"+ outputDirs[i]);
                System.out.println("Please delete the output directories manually before next job run...");
            }
        }
    }

    private static void cleanUpTempInputDirs() {
        System.out.println("Running Cleanup activity for input files...");
        File[] inputFiles = tempDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.startsWith(inputDirBase))
                    return true;
                return false;
            }
        });
        for(int i=0; i< inputFiles.length ; i++){
            FileUtils.deleteQuietly(inputFiles[i]);
        }

    }

    private static boolean consolidateResults(File[] outputDirs)  {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(tempDir, consolidatedOutputFile));
            for (int i =0; i< outputDirs.length ; i++){
                File outputDataFile = getOutputDataFile(outputDirs[i]);
                FileUtils.copyFile(outputDataFile, fos);
            }
        } catch (IOException e){
            e.printStackTrace();
            return false;
        } finally {
            if(fos != null)
            {
                try {
                    fos.close() ;
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return true;
    }

    private static File getOutputDataFile(File outputDir){
        File[] filesToCopy = outputDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.startsWith("part"))
                    return true;
                return false;
            }
        });
        if (filesToCopy!=null && filesToCopy.length >0)
            return filesToCopy[0];
        return null;
    }

    private static Job createJob(Configuration conf){
        Job job = null;
        try {
            job = new Job(conf, "Min and Max");
        } catch (IOException e) {
            e.printStackTrace();
        }
        job.setJarByClass(MinMaxStock.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setMapperClass(ColMapper.class);
        job.setReducerClass(ColReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        return job;
    }
}*/
