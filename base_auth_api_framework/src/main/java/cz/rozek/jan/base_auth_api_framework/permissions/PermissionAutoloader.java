package cz.rozek.jan.base_auth_api_framework.permissions;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.context.ApplicationContext;

public class PermissionAutoloader {

    private ApplicationContext context;    
    private String rootPackage;
    private IPermissionDatabaseHandler handler;
    
    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public IPermissionDatabaseHandler getHandler() {
        return handler;
    }

    public void setHandler(IPermissionDatabaseHandler handler) {
        this.handler = handler;
    }

    public void storePermissions() {
        List<Permission> permissions = findPermissions();

        handler.store(permissions);
    }

    private List<Class<?>> findClassesWithAnnotation(Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = new ArrayList<>();
        Reflections reflections = new Reflections(rootPackage, new TypeAnnotationsScanner(), new SubTypesScanner(false));

        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotationClass);
        classes.addAll(annotatedClasses);
        return classes;
    }

    private List<Permission> findPermissions() {
        List<Permission> permissions = new ArrayList<>();

        List<Class<?>> annotatedClasses = findClassesWithAnnotation(ContainsPermissions.class);

        for (Class<?> clazz : annotatedClasses) {
            List.of(clazz.getMethods())
            .stream()
            .filter(m -> m.isAnnotationPresent(ReturningPermission.class))
            .forEach(m -> {
                try {
                    permissions.add((Permission) m.invoke(context.getBean(clazz)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return permissions;
    }
}
