package br.com.juliocnsouza.projects.products.endpoints;

import br.com.juliocnsouza.projects.products.exceptions.NotFoundException;
import br.com.juliocnsouza.projects.products.models.Product;
import br.com.juliocnsouza.projects.products.services.ProductService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author julio
 */
@Component
@Path( "/products" )
public class ProductREST {

    private Gson gson;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @Autowired
    private ProductService productService;

    @POST
    @Path( "/create" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response create( String json ) {
        try {
            final Product product = gson.fromJson( json , Product.class );
            if ( product == null || product.getId() == null ) {
                return Response.status( Response.Status.PRECONDITION_FAILED ).build();
            }
            productService.save( product );
            return Response.status( Response.Status.CREATED ).build();
        }
        catch ( JsonSyntaxException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
    }

    @PUT
    @Path( "/{id}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response update( @PathParam( "id" ) String id , String json ) {
        try {
            final Product product = gson.fromJson( json , Product.class );
            if ( product == null || product.getId() == null ) {
                return Response.status( Response.Status.PRECONDITION_FAILED ).build();
            }
            productService.update( Integer.parseInt( id ) , product );
            return Response.ok().build();
        }
        catch ( JsonSyntaxException
                | NumberFormatException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @DELETE
    @Path( "/{id}" )
    public Response delete( @PathParam( "id" ) String id ) {
        try {
            productService.delete( Integer.parseInt( id ) );
            return Response.ok().build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
    }

    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getByID( @PathParam( "id" ) String id ) {
        try {
            final Product product = productService.get( Integer.parseInt( id ) , "NONE" );
            return Response.ok( gson.toJson( product ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/{id}/{relationshipType}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getByIdWithChild( @PathParam( "id" ) String id ,
                                      @PathParam( "relationshipType" ) String relationshipType ) {
        try {
            final Product product = productService.get( Integer.parseInt( id ) , relationshipType );
            return Response.ok( gson.toJson( product ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/all" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAll() {
        try {
            final List<Product> products = productService.getAll( "NONE" );
            return Response.ok( gson.toJson( products ) ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/all/{relationshipType}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAllWithChild( @PathParam( "relationshipType" ) String relationshipType ) {
        try {
            final List<Product> products = productService.getAll( relationshipType );
            return Response.ok( gson.toJson( products ) ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/children/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getChildren( @PathParam( "id" ) String id ) {
        try {
            final List<Product> products = productService.getChildren( Integer.parseInt( id ) );
            return Response.ok( gson.toJson( products ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ProductREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

}
