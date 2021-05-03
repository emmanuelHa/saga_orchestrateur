package com.soat.formation.saga.delivery.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("com.soat.formation.saga.delivery.infra.dao")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource getDataSource() {
        String projectPath = getResourceBasePath();
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        LOGGER.debug(String.format("Le fichier de persistance H2 pour delivery est situé dans :%s", projectPath+File.separator+"DB"));
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:"+projectPath+ File.separator+ "DB/:deliverydb;AUTO_SERVER=TRUE");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }


    private String getResourceBasePath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            LOGGER.error("Erreur lors de la recuperation du chemin vers la racine du projet "
                             + "pour construire l url ou stocker le fichier de persistance H2 ");
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }
        String pathStr = path.getAbsolutePath();
        pathStr = pathStr.replace("\\target\\classes", "");
        return pathStr;
    }

}
