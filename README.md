pre-agent 
====

基于javaAgent的zipkin链路追踪客户端探针。无需耦合业务代码实现业务埋点，实现链路追踪


Quick-start
=====

mvn package -Dmaven.test.skip=true  

打包目录接口说明  
lib --agent依赖包目录    
plugin --插件包目录  
pre-agent.jar --agent程序包  
pre.yml --核心配置文件  



