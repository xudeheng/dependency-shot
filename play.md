# dependency-shot module for Play! #

A dependency-shot module for Play! is available in the [download](http://code.google.com/p/dependency-shot/downloads/list) section

The dependency-shot module helps you to inject dependency-shot managed components into your Play! application. The injection points are defined by the usual @javax.inject.Inject annotation.

To install this module, just unzip the file in the module directory of your Play! distribution.

You can enable the dependency-shot module by adding the following line to your application's /conf/application.conf file:

```
module.dependencyshot=${play.path}/modules/dependencyshot
```

Then you just have to define one, or more, dependency-shot binder using a class extending cx.ath.mancel01.dependencyshot.graph.Binder and write your bindings in the configureBindings method.