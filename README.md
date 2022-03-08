项目技术栈：

服务注册中心nacos

服务配置中心Apollo

服务调用OpenFeign

消息队列RabbitMQ

缓存Redis

网关Spring Cloud gateway

基础的spring boot, MySQl, Mybatis-plus,  ~~Mongodb~~（由于原项目在这部分设计的不合理，故我为了优化正考虑去除）

未来会考虑加入：Sentinel ，SpringBoot Admin 

前端

vue, webpack

客户端用的是nuxt框架

管理页面用的是https://github.com/PanJiaChen/vue-admin-template脚手架开发

部署方面：

之前用的是集成了kubernetes，jenkins的[KubeSphere](https://kubesphere.io/zh/)但是由于在实验过程中发现KubeSphere消耗性能太大,且发现其他持续集成和部署的方案和技术等原因，故现在正考虑废弃Kubernetes