pre-agent 
====

基于javaAgent的zipkin链路追踪客户端探针。无需耦合业务代码实现业务埋点，实现链路追踪


Quick-start
=====

### mvn package -Dmaven.test.skip=true  

### 目录结构说明
 
lib --agent依赖包目录    
plugin --插件包目录  
pre-agent.jar --agent程序包  
pre.yml --核心配置文件  


plugin/zipkin.properties--配置文件说明  
#zipkin服务端地址  
zipkin.url=http://ip:端口  
#应用服务名称  
server.name=test    
#采样率设置0-1  
zipkin.percentage=1.0  




