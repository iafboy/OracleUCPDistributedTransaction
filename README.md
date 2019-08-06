# 基于Oracle UCP的分布式事务应用DEMO
### 本程序核心代码基于应用 https://github.com/iafboy/AtumikosMultiDBTransactionDemo.git
### 此程序可以使用Oracle UCP驱动直接使用Oracle Sharding功能满足大量分布式数据的高速查询，同时也能满足使用Atomikos开源框架实现数据的分布式事务写入/更新操作
主要取消了原有DruidXADatasource依赖，改为Oracle UCP默认的PoolXADataSource，分布式事务仍然由Atomikos控制
具体参考代码TestMyBatisConfig1.java以及TestMyBatisConfig2.java的AtomikosDataSourceBean初始化创建过程
#### 关于UCP的介绍请参考官方文档 https://docs.oracle.com/database/121/JJUCP/intro.htm#JJUCP8115
