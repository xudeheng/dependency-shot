/*
 *  Copyright 2009-2010 Mathieu ANCELIN
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package cx.ath.mancel01.dependencyshot.graph;

import cx.ath.mancel01.dependencyshot.util.InstanceProvider;
import cx.ath.mancel01.dependencyshot.api.DSBinder;
import cx.ath.mancel01.dependencyshot.api.DSInjector;
import cx.ath.mancel01.dependencyshot.injection.fluent.FluentBinder;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Provider;

/**
 * A binder is a configuration class containing bindings
 * between interface classes and their implementations.
 * At least one binder with one binding is necessary to
 * inject dependencies in your code.
 * To use a binder, define your own binder class extending this one
 * and define your own configureBindings method with your personnal
 * bindings.
 * 
 * @author Mathieu ANCELIN
 */
public abstract class Binder implements DSBinder, FluentBinder {

    /**
     * Context for named injections.
     */
    private Map<Binding<?>, Binding<?>> bindings = new HashMap<Binding<?>, Binding<?>>();
    /**
     * The injector that manage the binder instance.
     */
    private DSInjector binderInjector;
    private Class from = null;
    private Class to = null;
    private String named = null;
    private Class<? extends Annotation> annotation = null;
    private Provider provider = null;

    /**
     * Constructor.
     */
    public Binder() {
    }

    /**
     * Abstract method to configure the binder with bindings.
     */
    @Override
    public abstract void configureBindings();

    /**
     * Binding method
     * 
     * @param <T> type
     * @param from Binded class
     * @param to Implementation of extends of from
     */
    @Deprecated
    public final <T> void bind(Class<T> from, Class<? extends T> to) {
        addBindingToBinder(new Binding<T>(null, null, from, to, null));
    }

    /**
     * Binding method
     *
     * @param <T> type
     * @param c Binded class
     */
    @Deprecated
    public final <T> void bind(Class<T> c) {
        addBindingToBinder(new Binding<T>(null, null, c, c, null));
    }

    /**
     * Binding method
     *
     * @param <T> type
     * @param qualifier
     * @param from Binded class
     * @param to Implementation of extends of from
     */
    @Deprecated
    public final <T> void bind(Class<? extends Annotation> qualifier, Class<T> from, Class<? extends T> to) {
        addBindingToBinder(new Binding<T>(qualifier, null, from, to, null));
    }

    /**
     * Binding method
     * 
     * @param <T> type
     * @param name name of the binding @Named
     * @param from Binded class
     * @param provider provide object
     */
    @Deprecated
    public final <T> void bind(String name, Class<T> from, Provider<T> provider) {
        addBindingToBinder(new Binding<T>(null, name, from, from, provider));
    }

    /**
     * Binding method
     *
     * @param <T> type
     * @param from Binded class
     * @param name name of the binding @Named
     * @param to Implementation of extends of from
     */
    @Deprecated
    public final <T> void bind(Class<T> from, String name, Class<? extends T> to) {
        addBindingToBinder(new Binding<T>(null, name, from, to, null));
    }

    /**
     * Add a binding to current bindings.
     *
     * @param <T> type
     * @param binding a binding to add.
     */
    private <T> void addBindingToBinder(Binding<T> binding) {
        if (bindings.containsKey(binding)) {
            return;
        }
        Binding<?> old = bindings.put(binding, binding);
        if (old != null) {
            throw new IllegalArgumentException(binding + " overwrites " + old);
        }
    }

    @Override
    public final void configureLastBinding() {
        addBindingToBinder(
                new Binding(this.annotation, this.named,
                this.from, this.to, this.provider));
        this.from = null;
        this.to = null;
        this.named = null;
        this.annotation = null;
        this.provider = null;
    }

    //TODO : binding validation in fluent API
    public final <T> FluentBinder fbind(Class<T> from) {
        addBindingToBinder(
                new Binding<T>(this.annotation, this.named,
                this.from, this.to, this.provider));
        this.from = from;
        this.to = from;
        this.named = null;
        this.annotation = null;
        this.provider = null;
        return this;
    }

    @Override
    public final <T> FluentBinder to(Class<T> to) {
        this.to = to;
        return this;
    }

    @Override
    public final <T> FluentBinder named(String named) {
        this.named = named;
        return this;
    }

    @Override
    public final <T> FluentBinder annotedWith(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    @Override
    public final <T> FluentBinder providedBy(Provider<T> provider) {
        this.provider = provider;
        return this;
    }

    @Override
    public final <T> FluentBinder toInstance(Object instance) {
        this.provider = new InstanceProvider(instance);
        return this;
    }

    public final Map<Binding<?>, Binding<?>> getBindings() {
        return bindings;
    }

    @Override
    public final boolean isEmpty() {
        return bindings.isEmpty();
    }

    @Override
    public final void setInjector(DSInjector injector) {
        binderInjector = injector;
    }

    public final DSInjector getBinderInjector() {
        return binderInjector;
    }
}