package br.com.juliocnsouza.projects.products.services;

import br.com.juliocnsouza.projects.products.exceptions.NotFoundException;
import br.com.juliocnsouza.projects.products.helper.ProductRelationshipCleaner;
import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.models.Product;
import br.com.juliocnsouza.projects.products.repositories.ImageRepository;
import br.com.juliocnsouza.projects.products.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author julio
 */
@Component
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    public enum RelationshipType {
        PARENT,
        IMAGES,
        BOTH,
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

    public void save( Product product ) {
        if ( product.getImages() != null ) {
            product.getImages().stream().forEach( img -> {
                imageRepository.save( img );
            } );
        }
        productRepository.save( product );
    }

    public void update( int id , Product productChangeData )
            throws NotFoundException {
        final Product product = productRepository.findOne( id );
        if ( product == null ) {
            throw new NotFoundException( "product with id: " + id );
        }
        product.merge( productChangeData );
        save( product );
    }

    public void delete( int id ) {
        Product product = new ProductRelationshipCleaner( productRepository , imageRepository )
                .clearAll( id );
        productRepository.delete( product );
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

    public List<Product> getAll( String relationship )
            throws NotFoundException {
        final Iterable<Product> products = productRepository.findAll();
        if ( products == null ) {
            throw new NotFoundException( "no products found" );
        }
        List<Product> result = new ArrayList<>();
        final RelationshipType relationshipType = RelationshipType.get( relationship );
        products.forEach( prod -> {
            processRelationships( relationshipType , prod );
            result.add( prod );
        } );
        return result;
    }

    public Product get( int id , String child )
            throws NotFoundException {
        final Product product = productRepository.findOne( id );
        if ( product == null ) {
            throw new NotFoundException( "product with id: " + id );
        }
        final RelationshipType childType = RelationshipType.get( child );
        processRelationships( childType , product );
        return product;
    }

    private void processRelationships( RelationshipType childType , final Product product ) {
        switch ( childType ) {
            case BOTH:
                bothRelationshipSetting( product );
                break;
            case IMAGES:
                imagesRelationshipSetting( product );
                break;
            case NONE:
                noneRelatationshipSetting( product );
                break;
            case PARENT:
                //no need set parent since the relationship is eager by default
                break;
            default:
                noneRelatationshipSetting( product );
        }
    }

    private void noneRelatationshipSetting( final Product product ) {
        product.setParentProduct( null );// since the  relationship is eager by defaulf set to null
    }

    private void imagesRelationshipSetting( final Product product ) {
        product.setParentProduct( null );// since the  relationship is eager by defaulf set to null
        setImages( product );
    }

    private void bothRelationshipSetting( final Product product ) {
        //no need to set parent since the relationship is eager by default
        setImages( product );
    }

    public List<Product> getChildren( int id )
            throws NotFoundException {
        final List<Product> children = productRepository.findChildren( id );
        if ( children == null ) {
            throw new NotFoundException( "children for " + id );
        }
        return children;
    }

}
