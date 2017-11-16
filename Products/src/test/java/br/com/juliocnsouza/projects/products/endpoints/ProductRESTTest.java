package br.com.juliocnsouza.projects.products.endpoints;

import br.com.juliocnsouza.projects.products.DataInitializer;
import br.com.juliocnsouza.projects.products.models.Product;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 *
 * @author julio
 */
@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT )
public class ProductRESTTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataInitializer dataInitializer;

    @Before
    public void testInit() {
        dataInitializer.init();
    }

    @Test
    public void testCreateOK() {
        Product p = new Product();
        final String name = "Product from " + this.getClass().getSimpleName();
        p.setDescription( name );
        p.setId( 3 );
        p.setName( name );
        String json = new Gson().toJson( p );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/create" , HttpMethod.POST ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.CREATED );
    }

    @Test
    public void testCreatePRECONDITION_FAILED() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( "{'INVALID':'JSON'}" , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/create" , HttpMethod.POST ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdate() {
        Product p = new Product();
        final String name = "Product from " + this.getClass().getSimpleName();
        p.setDescription( name );
        p.setId( 1 );
        p.setName( name );
        String json = new Gson().toJson( p );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/1" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.OK );

        //now finding the product and checking if the name has changed
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/1" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );

        final Product foundProduct = new Gson().fromJson( entity.getBody() , Product.class );
        assertThat( foundProduct.getName() ).isEqualTo( name );
    }

    @Test
    public void testUpdatePRECONDITION_FAILED_1() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( "{'INVALID':'JSON'}" , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/1" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdatePRECONDITION_FAILED_2() {
        Product p = new Product();
        final String name = "Product from " + this.getClass().getSimpleName();
        p.setDescription( name );
        p.setId( 1 );
        p.setName( name );
        String json = new Gson().toJson( p );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/one" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdateGONE() {
        Product p = new Product();
        final String name = "Product from " + this.getClass().getSimpleName();
        p.setDescription( name );
        p.setId( 1 );
        p.setName( name );
        String json = new Gson().toJson( p );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/9874" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testDeleteOK() {
        this.restTemplate.delete( "/ws/products/2" );
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/2" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetByID_OK() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/1" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetByID_GONE() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/9874" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetByIdWithChildOK() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/1/NONE" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetByIdWithChildGONE() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/9874/NONE" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetAll() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/all" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetAllWithChild() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/all/IMAGE" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetChildren() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/children/1" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetChildrenPRECONDITION_FAILED() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/children/one" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

}
