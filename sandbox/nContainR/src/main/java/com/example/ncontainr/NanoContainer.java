package com.example.ncontainr;

import com.example.ncontainr.annotation.PostConstruct;
import com.example.ncontainr.annotation.PreDestroy;
import com.example.ncontainr.api.Provider;
import com.example.ncontainr.api.AbstractContainer;
import com.example.ncontainr.aop.BeanProxy;
import com.example.ncontainr.exception.ContainerException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Actual implementation of the container.
 */
public class NanoContainer extends AbstractContainer {

    private Map<Class<?>, Class<?>> registry;
    private Map<Class<?>, Provider> factories;
    private List<Class<?>> dependencies;
    private List<Object> managedBeans;
    private boolean aopEnabled = false;

    public NanoContainer(boolean aopEnabled) {
        registry = new HashMap<Class<?>, Class<?>>();
        factories = new HashMap<Class<?>, Provider>();
        dependencies = new ArrayList<Class<?>>();
        managedBeans = new ArrayList<Object>();
        this.aopEnabled = aopEnabled;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> void register(Class<T> interf, Class<? extends T> impl) {
        this.registry.put(interf, impl);
    }

    public void registerFactory(Class impl, Provider factory) {
        this.factories.put(impl, factory);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T getBean(Class<T> clazz) {
        // gestion des dependances cycliques
        if (dependencies.contains(clazz)) {
            throw new ContainerException("Cyclic dependency detected in class " + clazz);
        } else {
            dependencies.add(clazz);
        }
        try {
            // match vers une implementation ou non via le registre
            Class<? extends T> impl = clazz;
            if (registry.containsKey(clazz)) {
                impl = (Class<? extends T>) registry.get(clazz);
            }
            if (impl.isInterface()) {
                throw new ContainerException("Can't instanciate "
                        + impl.getName()
                        + ".\nYou should provide a configuration to bind an "
                        + "implementation to this interface.");
            }
            Object instance = null;
            // injection de dépendances
            if (factories.containsKey(impl))
                instance = factories.get(impl).get();
            else
                instance = injectConstructors(clazz, impl);
            instance = injectFields(impl, instance);
            instance = injectMethods(impl, instance);
            // lifecycle start
            managedBeans.add(instance);
            applyPostConstruct(instance);
            // décoration par AOP
            instance = applyAOP(clazz, instance);
            return (T) instance;
        } catch (Exception e) {
            System.out.println(e);
            throw new ContainerException(e);
        } finally {
            // gestion des dependances cycliques
            dependencies.remove(clazz);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        for (Object obj : this.managedBeans) {
            if (obj != null)
                // lifecycle stop
                applyPreDestroy(obj);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected <T> T injectConstructors(Class clazz, Class impl) throws Exception {
        Object instance = null;
        for (Constructor<?> constructor : impl.getDeclaredConstructors()) {
            Inject annotation = constructor.getAnnotation(Inject.class);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            boolean isDefaultConstructor = (impl.getDeclaredConstructors().length == 1
                    && parameterTypes.length == 0
                    && Modifier.isPublic(constructor.getModifiers()));
            if (annotation != null || isDefaultConstructor) {
                Object[] parameters = new Object[parameterTypes.length];
                for (int j = 0; j < parameterTypes.length; j++) {
                    parameters[j] = getBean(parameterTypes[j]);
                }
                boolean accessible = constructor.isAccessible();
                if (!accessible) {
                    constructor.setAccessible(true);
                }
                try {
                    instance = clazz.cast(constructor.newInstance(parameters));
                } finally {
                    if (!accessible) {
                        constructor.setAccessible(accessible);
                    }
                }
            } else {
                instance = impl.newInstance();
            }
        }
        return (T) instance;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected <T> T injectFields(Class<T> impl, Object instance) throws Exception {
        for (Field field : impl.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                boolean isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }
                field.set(instance, getBean(field.getType()));
                field.setAccessible(isAccessible);
            }
        }
        return (T) instance;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected <T> T injectMethods(Class<T> impl, Object instance) throws Exception {
        for (Method method : impl.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Inject.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                for (int j = 0; j < parameterTypes.length; j++) {
                    parameters[j] = getBean(parameterTypes[j]);
                }
                boolean accessible = method.isAccessible();
                if (!accessible) {
                    method.setAccessible(true);
                }
                try {
                    method.invoke(instance, parameters);
                } finally {
                    if (!accessible) {
                        method.setAccessible(accessible);
                    }
                }
            }
        }
        return (T) instance;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected <T> T applyAOP(Class<T> clazz, Object instance) {
        if (clazz.isInterface() && this.aopEnabled) {
            return (T) Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{clazz},
                    new BeanProxy(instance, this));
        } else {
            return (T) instance;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void applyPostConstruct(Object instance) throws Exception {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                boolean accessible = method.isAccessible();
                if (!accessible) {
                    method.setAccessible(true);
                }
                try {
                    method.invoke(instance);
                } finally {
                    if (!accessible) {
                        method.setAccessible(accessible);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void applyPreDestroy(Object instance) throws Exception {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                boolean accessible = method.isAccessible();
                if (!accessible) {
                    method.setAccessible(true);
                }
                try {
                    method.invoke(instance);
                } finally {
                    if (!accessible) {
                        method.setAccessible(accessible);
                    }
                }
            }
        }
    }
}
