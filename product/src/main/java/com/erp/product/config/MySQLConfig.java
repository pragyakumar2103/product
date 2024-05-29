package com.erp.product.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = {"com.erp.user.entity"})
@EnableJpaRepositories(
        basePackages = "com.erp.user.repository",
        entityManagerFactoryRef = "mySQLEntityManager",
        transactionManagerRef = "mySQLTransactionManager")
@PropertySource("classpath:application-test.properties")
public class MySQLConfig {

    public class MySqlConfig {

        @Value("${spring.datasource.driver-class}")
        private String driverClassName;
        @Value("${spring.datasource.url}")
        private String mysqlUrl;
        @Value("${spring.datasource.username}")
        private String mysqlUsername;
        @Value("${spring.datasource.password}")
        private String mysqlPassword;

        @Autowired
        private Environment environment;

        @Bean
        @Primary
        public LocalContainerEntityManagerFactoryBean mySQLEntityManager(){
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(userDataSource());
            em.setPackagesToScan(new String[]{"com.erp.user.entity"});
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            em.setJpaVendorAdapter(vendorAdapter);
            HashMap<String, Object> properties = new HashMap<>();
            properties.put("hibernate.hbm2ddl.auto",environment.getProperty("spring.jpa.hibernate.ddl-auto"));
            properties.put("hibernate.dialect",environment.getProperty("org.hibernate.dialect.MySQLDialect"));
            em.setJpaPropertyMap(properties);
            return em;
        }

        @Bean
        @Primary
        public DataSource userDataSource() {
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(3);
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(mysqlUrl);
            config.setUsername(mysqlUsername);
            config.setPassword(mysqlPassword);
            System.out.println("connecting to MySQL.");
            return new HikariDataSource(config);
        }

        @Bean
        @Primary
        public PlatformTransactionManager mySQLTransactionManager(){
            JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(mySQLEntityManager().getObject());
            return transactionManager;
        }
    }

}
