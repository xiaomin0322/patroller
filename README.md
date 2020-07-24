patroller 
====

基于javaAgent的zipkin链路追踪客户端探针。无需耦合业务代码实现业务埋点，实现链路追踪,依赖zipkin服务端。


Quick-start
=====

### mvn package -Dmaven.test.skip=true  

### 目录结构说明
 
lib --agent依赖包目录    
plugin --插件包目录  
pre-agent.jar --agent程序包  
pre.yml --核心配置文件  


### zipkin文件配置,也可以通过java -Dserver.name -Dzipkin.url -Dzipkin.percentage 传入参数
plugin/zipkin.properties --配置文件说明  
#zipkin服务端地址  
zipkin.url=http://ip:端口  
#应用服务名称  
server.name=test    
#采样率设置0-1  
zipkin.percentage=1.0    

### pre.yml 根据具体业务需求配置aop埋点。例子：  
#业务service配置      
  pre-service:  
    patterns:#具体servier包名称  
      - com.*.service.impl.*  
    excludedPatterns://需要排除的方法名称list  
    includedPatterns://需要埋点的方法名称list  
      - key: .*  
    interceptors://拦截器  
      - com.preapm.agent.plugin.interceptor.ZipkinInterceptor  
    track:  
           inParam: true #记录入参  
           outParam: true #记录出参  
           time: -1    #不设置时间限制   
           serialize: fastjson   #参数序列化方式，默认toString



### 启动zipkin服务
参考 https://github.com/openzipkin/zipkin


### 执行demo
java -javaagent:具体目录\pre-agent\pre-agent.jar -javaagent:具体目录\pre-agent\plugin\transmittable-thread-local-2.11.2.jar    -Dserver.name=pre-test   -jar pre-agent.jar  


### log配置打印TraceId,SpanId
logback:  
 %X{X-B3-TraceId:-} | %X{X-B3-SpanId:-}  
log4j:  
 %X{X-B3-TraceId} | %X{X-B3-SpanId}   
log4j2:  
%pt  %ps  
### 业务端埋点打标签
log.info("tracer.orderId {}",orderId);


### 目前支持的插件  (某些插件只需要配置就好)
tomcat  
httpclient  
okhttp  
mysql-jdbc-driver  
logback  
log4j2  
log4j  
druid  
jedis  
jetty  
jdk-http  


QQ群：858951677
 
-------------------------todo list -------------------------------------------  
1.opentracing client 支持多种服务端上报  
2.线程问题(已修复)  
目前用的阿里的https://github.com/alibaba/transmittable-thread-local,某些场景会出现问题，需要自己实现一个基于jdk thead的agent解决，参考https://github.com/naver/pinpoint/blob/master/plugins/thread/src/main/java/com/navercorp/pinpoint/plugin/thread/ThreadPlugin.java    



 
