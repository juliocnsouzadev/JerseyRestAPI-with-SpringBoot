package br.com.juliocnsouza.projects.products;

import br.com.juliocnsouza.projects.products.endpoints.ImageREST;
import br.com.juliocnsouza.projects.products.endpoints.ProductREST;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author julio
 */
@Configuration
@ApplicationPath( "/ws" )
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register( ProductREST.class );
        register( ImageREST.class );
    }

}
