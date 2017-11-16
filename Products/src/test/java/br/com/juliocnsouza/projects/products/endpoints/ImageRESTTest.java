package br.com.juliocnsouza.projects.products.endpoints;

import br.com.juliocnsouza.projects.products.DataInitializer;
import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.models.Product;
import com.google.gson.Gson;
import java.util.Arrays;
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
public class ImageRESTTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataInitializer dataInitializer;

    @Before
    public void testInit() {
        dataInitializer.init();
    }

    @Test
    public void testJson() {

        Product parent = new Product();
        parent.setDescription( "parent product" );
        parent.setId( 1 );
        parent.setName( "parent" );

        Image img1 = new Image();
        img1.setId( 1 );
        img1.setProduct( parent );

        //parent.setImages( Arrays.asList( img1 ) );
        Product child = new Product();
        child.setDescription( "child product" );
        child.setId( 1 );
        child.setName( "child" );
        Image img2 = new Image();
        img2.setId( 2 );
        child.setParentProduct( parent );
        child.setImages( Arrays.asList( img2 ) );

        Gson gson = new Gson();

        ///String jsonProduct = gson.toJson( child );
        String jsonImg = gson.toJson( img1 );

        System.out.println( "----- **** ------" );
        //System.out.println( "Product Json:\n" + jsonProduct );
        System.out.println( "----- **** ------" );
        System.out.println( "Image Json:\n" + jsonImg );
        System.out.println( "----- **** ------" );

    }

    @Test
    public void testCreateOK() {
        Image img = getImg( 3 , 3 );
        String json = new Gson().toJson( img );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/create" ,
                                                                            HttpMethod.POST ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.CREATED );
    }

    private Image getImg( int prodId , int imgID ) {
        Product prod = new Product();
        final String name = "Product from " + this.getClass().getSimpleName();
        prod.setDescription( name );
        prod.setId( prodId );
        prod.setName( name );
        Image img = new Image();
        img.setId( imgID );
        img.setProduct( prod );
        return img;
    }

    @Test
    public void testCreatePRECONDITION_FAILED() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( "{'INVALID':'JSON'}" , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/create" ,
                                                                            HttpMethod.POST ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdate() {
        Image img = getImg( 4 , 1 );
        String json = new Gson().toJson( img );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/1" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.OK );

        //now finding the image and checking if the product has changed
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/1" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );

        final Image found = new Gson().fromJson( entity.getBody() , Image.class );
        assertThat( found ).isNotNull();
    }

    @Test
    public void testUpdatePRECONDITION_FAILED_1() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( "{'INVALID':'JSON'}" , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/1" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdatePRECONDITION_FAILED_2() {
        Image img = getImg( 1 , 1 );
        String json = new Gson().toJson( img );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/one" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

    @Test
    public void testUpdateGONE() {
        Image img = getImg( 1 , 1 );
        String json = new Gson().toJson( img );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> httpEntity = new HttpEntity<>( json , headers );
        final ResponseEntity<String> exchange = this.restTemplate.exchange( "/ws/products/images/9874" , HttpMethod.PUT ,
                                                                            httpEntity , String.class );
        assertThat( exchange.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testDeleteOK() {
        this.restTemplate.delete( "/ws/products/images/2" );
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/2" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetByID_OK() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/1" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetByID_GONE() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/9874" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetByIdWithChildOK() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/1/PRODUCT" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetByIdWithChildGONE() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/9874/NONE" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.GONE );
    }

    @Test
    public void testGetAll() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/all" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void testGetAllWithChild() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/all/PRODUCT" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void getForProduct() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/forProduct/2" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.OK );
    }

    @Test
    public void getForProductPRECONDITION_FAILED() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity( "/ws/products/images/forProduct/two" ,
                                                                        String.class );
        assertThat( entity.getStatusCode() ).isEqualTo( HttpStatus.PRECONDITION_FAILED );
    }

}
