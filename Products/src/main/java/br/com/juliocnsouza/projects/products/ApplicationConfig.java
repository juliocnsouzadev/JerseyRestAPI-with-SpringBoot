package br.com.juliocnsouza.projects.products;

import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 *
 * @author julio
 */
@SpringBootApplication
public class ApplicationConfig {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setName( "ca_test" )
                .setType( EmbeddedDatabaseType.HSQL )
                .addScript( "script/sql/create-db.sql" )
                .build();
        return db;
    }

    public static void main( String[] args ) {
        SpringApplication.run( ApplicationConfig.class , args );
    }

}
