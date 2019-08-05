package com.demo.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * @Configuration 和@MapperScan两个注解
 * DataSource1Config类的两个注解开启，TestMyBatisConfig1类的两个注解关闭，就代表使用单数据库事物
 * TestMyBatisConfig1类的两个注解开启，DataSource1Config类的两个注解关闭，就代表使用分布式数据库事物
  *  因为TestMyBatisConfig1使用了atomikos
 *
 */
// basePackages 最好分开配置 如果放在同一个文件夹可能会报错
@Configuration
@MapperScan(basePackages = "com.demo.db2", sqlSessionTemplateRef = "test2SqlSessionTemplate")
public class TestMyBatisConfig2 {
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestMyBatisConfig2.class);

	// 配置数据源
	@Bean(name = "dataSource2")
	public DataSource testDataSource(DBConfig2 testConfig) throws SQLException {
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		PoolXADataSource pds = PoolDataSourceFactory.getPoolXADataSource();
		pds.setConnectionFactoryClassName("oracle.jdbc.xa.client.OracleXADataSource");
		pds.setURL(testConfig.getUrl());
		pds.setUser(testConfig.getUsername());
		pds.setPassword(testConfig.getPassword());
		pds.setInitialPoolSize(testConfig.getMinPoolSize());
		
		xaDataSource.setUniqueResourceName("oracle2");
		xaDataSource.setXaDataSource(pds);
//		xaDataSource.setXaDataSourceClassName("oracle.ucp.jdbc.PoolXADataSource");
//		xaDataSource.setMaxLifetime(testConfig.getMaxLifetime());
//		xaDataSource.setMinPoolSize(testConfig.getMinPoolSize());
//		xaDataSource.setMaxPoolSize(testConfig.getMaxPoolSize());
//		xaDataSource.setBorrowConnectionTimeout(testConfig.getBorrowConnectionTimeout());
//		xaDataSource.setLoginTimeout(testConfig.getLoginTimeout());
//		xaDataSource.setMaintenanceInterval(testConfig.getMaintenanceInterval());
//		xaDataSource.setMaxIdleTime(testConfig.getMaxIdleTime());
//		xaDataSource.setTestQuery(testConfig.getTestQuery());
		
		LOG.info("分布式事物dataSource2实例化成功");
		return xaDataSource;
	}

	@Bean(name = "test2SqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Bean(name = "test2SqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(
			@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
