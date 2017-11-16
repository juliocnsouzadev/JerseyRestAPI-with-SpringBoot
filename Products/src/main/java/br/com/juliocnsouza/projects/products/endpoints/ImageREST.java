package br.com.juliocnsouza.projects.products.endpoints;

import br.com.juliocnsouza.projects.products.exceptions.NotFoundException;
import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.services.ImageService;
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
@Path( "/products/images" )
public class ImageREST {

    private Gson gson;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @Autowired
    private ImageService imageService;

    @POST
    @Path( "/create" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response create( String json ) {
        try {
            final Image image = gson.fromJson( json , Image.class );
            if ( image != null && image.getId() != null ) {
                imageService.save( image );
                return Response.status( Response.Status.CREATED ).build();
            }
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( JsonSyntaxException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
    }

    @PUT
    @Path( "/{id}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response update( @PathParam( "id" ) String id , String json ) {
        try {
            final Image image = gson.fromJson( json , Image.class );
            if ( image != null && image.getId() != null ) {
                imageService.update( Integer.parseInt( id ) , image );
                return Response.ok().build();
            }
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( JsonSyntaxException
                | NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @DELETE
    @Path( "/{id}" )
    public Response delete( @PathParam( "id" ) String id ) {
        try {
            imageService.delete( Integer.parseInt( id ) );
            return Response.ok().build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
    }

    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getByID( @PathParam( "id" ) String id ) {
        try {
            final Image image = imageService.get( Integer.parseInt( id ) , "NONE" );
            return Response.ok( gson.toJson( image ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/{id}/{relationshipType}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getByIdWithChild( @PathParam( "id" ) String id ,
                                      @PathParam( "relationshipType" ) String relationshipType ) {
        try {
            final Image image = imageService.get( Integer.parseInt( id ) , relationshipType );
            return Response.ok( gson.toJson( image ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/all" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAll() {
        try {
            final List<Image> images = imageService.getAll( "NONE" );
            return Response.ok( gson.toJson( images ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/all/{relationshipType}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAllWithChild( @PathParam( "relationshipType" ) String relationshipType ) {
        try {
            final List<Image> images = imageService.getAll( relationshipType );
            return Response.ok( gson.toJson( images ) ).build();
        }
        catch ( NumberFormatException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.PRECONDITION_FAILED ).build();
        }
        catch ( NotFoundException ex ) {
            Logger.getLogger( ImageREST.class.getName() ).log( Level.SEVERE , null , ex );
            return Response.status( Response.Status.GONE ).build();
        }
    }

    @GET
    @Path( "/forProduct/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getForProduct( @PathParam( "id" ) String id ) {
        try {
            final List<Image> images = imageService.getForProduct( Integer.parseInt( id ) );
            return Response.ok( gson.toJson( images ) ).build();
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
