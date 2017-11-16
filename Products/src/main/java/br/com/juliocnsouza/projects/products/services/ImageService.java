package br.com.juliocnsouza.projects.products.services;

import br.com.juliocnsouza.projects.products.exceptions.NotFoundException;
import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.models.Product;
import br.com.juliocnsouza.projects.products.repositories.ImageRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author julio
 */
@Component
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public enum RelationshipType {
        PRODUCT,
        NONE;

        public static RelationshipType get( String child ) {
            for ( RelationshipType type : values() ) {
                if ( type.toString().equals( child ) ) {
                    return type;
                }
            }
            return NONE;
        }
    }

    public Image save( Image image ) {
        return imageRepository.save( image );
    }

    public Image update( int id , Image imgChangeData )
            throws NotFoundException {
        final Image image = imageRepository.findOne( id );
        if ( image == null ) {
            throw new NotFoundException( "image with id: " + id );
        }
        image.merge( imgChangeData );
        return save( image );
    }

    public void delete( int id ) {
        final Image img = imageRepository.findOne( id );
        img.setProduct( null );
        final Image saved = imageRepository.save( img );
        imageRepository.delete( saved );
    }

    private void setImages( final Product product ) {
        final List<Image> images = imageRepository.findByProduct( product.getId() );
        product.setImages( images );
        setImagesToParent( product );
    }

    private void setImagesToParent( Product product ) {
        final Product parent = product.getParentProduct();
        if ( parent != null ) {
            setImages( parent );
        }
    }

    public List<Image> getAll( String relationship )
            throws NotFoundException {
        final Iterable<Image> images = imageRepository.findAll();
        if ( images == null ) {
            throw new NotFoundException( "no images found" );
        }
        List<Image> result = new ArrayList<>();
        final RelationshipType relationshipType = RelationshipType.get( relationship );
        images.forEach( img -> {
            processRelationships( relationshipType , img );
            result.add( img );
        } );
        return result;
    }

    public Image get( int id , String relationship )
            throws NotFoundException {
        final Image image = imageRepository.findOne( id );
        if ( image == null ) {
            throw new NotFoundException( "image with id: " + id );
        }
        final RelationshipType elationshipType = RelationshipType.get( relationship );
        processRelationships( elationshipType , image );
        return image;
    }

    private void processRelationships( RelationshipType relationshipType , final Image img ) {
        switch ( relationshipType ) {
            case NONE:
                noneRelationshipSetting( img );
                break;
            case PRODUCT:
                //no need set parent since the relationship is eager by default
                break;
            default:
                // default is NONE
                noneRelationshipSetting( img );
        }
    }

    private void noneRelationshipSetting( final Image img ) {
        img.setProduct( null );
    }

    public List<Image> getForProduct( int id )
            throws NotFoundException {
        final List<Image> images = imageRepository.findByProduct( id );
        if ( images == null ) {
            throw new NotFoundException( "images for product " + id );
        }
        return images;
    }
}
