package com.lister.ldap.auth.config;

import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Custom configuration of validation. This configuration defines custom:
 * <ul>
 *     <li>ConstraintValidationFactory - so that validators are able to inject Jersey providers/resources.</li>
 *     <li>ParameterNameProvider - if method input parameters are invalid, this class returns actual parameter names
 *     instead of the default ones ({@code arg0, arg1, ..})</li>
 * </ul>
 * Created by vamsikrishna on 16/9/14.
 */
public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

    @Context
    private ResourceContext resourceContext;

    @Override
    public ValidationConfig getContext(final Class<?> type) {
        final ValidationConfig config = new ValidationConfig();
        config.constraintValidatorFactory(resourceContext.getResource(InjectingConstraintValidatorFactory.class));
        config.parameterNameProvider(new CustomParameterNameProvider());
        //config.
        return config;
    }

    /**
     * See ContactCardTest#testAddInvalidContact.
     */
    private class CustomParameterNameProvider implements ParameterNameProvider {

        private final ParameterNameProvider nameProvider;

        public CustomParameterNameProvider() {
            nameProvider = Validation.byDefaultProvider().configure().getDefaultParameterNameProvider();
        }

        @Override
        public List<String> getParameterNames(final Constructor<?> constructor) {
            return nameProvider.getParameterNames(constructor);
        }

        @Override
        public List<String> getParameterNames(Method method) {
            Annotation[][] annotationsByParam = method.getParameterAnnotations();
            List<String> names = new ArrayList<String>( annotationsByParam.length );
            for ( Annotation[] annotations : annotationsByParam ) {
                String name = getParamName(annotations);
                if ( name == null )
                    name = "arg" + ( names.size() + 1 );

                names.add( name );
            }

            return names;
        }

        private String getParamName(Annotation[] annotations) {
            for ( Annotation annotation : annotations ) {
                if ( annotation.annotationType() == QueryParam.class ) {
                    return QueryParam.class.cast( annotation ).value();
                }
                else if ( annotation.annotationType() == PathParam.class ) {
                    return PathParam.class.cast( annotation ).value();
                }
                else if (annotation.annotationType() == FormParam.class) {
                    return FormParam.class.cast(annotation).value();
                }
            }
            return null;
        }

        /*@Override
        public List<String> getParameterNames(final Method method) {
            // See ContactCardTest#testAddInvalidContact.
            if ("authenticateUser".equals(method.getName())) {
                return Arrays.asList("userId", "password");
            } else if("authorizeUser".equals(method.getName())) {
                return Arrays.asList("userId", "groupName");
            } else if("validateUser".equals(method.getName())) {
                return Arrays.asList("userId", "password", "groupName", "isAuthorizationRequired");
            }

            return nameProvider.getParameterNames(method);
        }*/
    }



}
