package br.com.juliocnsouza.projects.products.helper;

import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.models.Product;
import br.com.juliocnsouza.projects.products.repositories.ImageRepository;
import br.com.juliocnsouza.projects.products.repositories.ProductRepository;
import java.util.List;

/**
 *
 * @author julio
 */
public class ProductRelationshipCleaner {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductRelationshipCleaner( ProductRepository productRepository , ImageRepository imageRepository ) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public Product clearAll( int id ) {
        clearRelationshipsWithImages( id );
        clearRelationshipWithChildren( id );
        return clearRelationshipWithParent( id );
    }

    private Product clearRelationshipWithParent( int id ) {
        final Product found = productRepository.findOne( id );
        found.setParentProduct( null );
        final Product product = productRepository.save( found );
        return product;
    }

    private void clearRelationshipWithChildren( int id ) {
        final List<Product> children = productRepository.findChildren( id );
        if ( children != null ) {
            children.stream().forEach( child -> {
                child.setParentProduct( null );
                productRepository.save( child );
            } );
        }
    }

    private void clearRelationshipsWithImages( int id ) {
        final List<Image> images = imageRepository.findByProduct( id );
        if ( images != null ) {
            for ( Image image : images ) {
                image.setProduct( null );
                final Image img = imageRepository.save( image );
                imageRepository.delete( img );
            }
        }
    }

}
