  * add DefaultInjector (with bindings.xml in META-INF)
  * injection on existing instances
  * graph on project binder
  * AOP like interceptor (added, multiples interceptor classes support needed)
  * default bindings (like logger)
  * automatic binding configuration from xml (without conf file and explicit binder)
  * XML configuration

```
  <bindings>
     <binding bind="MyGenericItf.java" to="MyRealImplementation.java"/>
     <binding named="injectable interface" to="MyRealImplementation2.java"/>
     <binding bind="MyGenericItf3.java" to="MyRealImplementation3.java" scope="SINGLETON"/>
  <bindings> 
```