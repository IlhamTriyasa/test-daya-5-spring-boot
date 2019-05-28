package com.test.dayalima.test.config;

import com.mchange.v2.c3p0.DriverManagerDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerMysqlProdFactory",
        basePackages = { "com.test.dayalima.test.production.repo" },
        transactionManagerRef = "transactioMysqlProdManager")
public class ProductionConfig {

    @Autowired
    private Environment env;

    @Bean(name = "mysqlproddatasource")
    @ConfigurationProperties(prefix = "mysqlprod.datasource")
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            dataSource.setDriverClass(env.getProperty("mysqlprod.datasource.driver-class-name"));
            dataSource.setJdbcUrl(env.getProperty("mysqlprod.datasource.url"));
            dataSource.setUser(env.getProperty("mysqlprod.datasource.username"));
            dataSource.setPassword(env.getProperty("mysqlprod.datasource.password"));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean(name = "entityManagerMysqlProdFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("mysqlproddatasource") DataSource ds){
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",env.getProperty("mysqlprod.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",env.getProperty("mysqlprod.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.cfg","org.hibernate.cfg.ImprovedNamingStrategy");
        return builder.dataSource(ds).properties(properties).
                packages("com.test.dayalima.test.production.model").build();
    }

    @Bean(name = "transactioMysqlProdManager")
    public PlatformTransactionManager transactioMysqlProdManager(@Qualifier("entityManagerMysqlProdFactory")EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }
}
