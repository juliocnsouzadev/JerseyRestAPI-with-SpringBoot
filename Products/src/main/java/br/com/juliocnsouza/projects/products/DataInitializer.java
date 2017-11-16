package br.com.juliocnsouza.projects.products;

import br.com.juliocnsouza.projects.products.models.Image;
import br.com.juliocnsouza.projects.products.models.Product;
import br.com.juliocnsouza.projects.products.repositories.ImageRepository;
import br.com.juliocnsouza.projects.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author julio
 */
@Component
public class DataInitializer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    public long[] init() {
        Product prod01 = buildProd01();
        Product prod02 = buildProd02( prod01 );
        productRepository.save( prod02 );
        long[] values = { productRepository.count() ,
                          imageRepository.count() };
        return values;
    }

    private Product buildProd01() {
        Product prod01 = new Product();
        prod01.setDescription( "First Product" );
        prod01.setId( 1 );
        prod01.setName( "prod one" );
        buildImg01( prod01 );
        return prod01;
    }

    private Product buildProd02( Product prod01 ) {
        Product prod02 = new Product();
        prod02.setDescription( "Second Product" );
        prod02.setId( 2 );
        prod02.setName( "prod two" );
        buildImg02( prod02 );
        prod02.setParentProduct( prod01 );
        return prod02;
    }

    private void buildImg02( Product prod02 ) {
        Image img02 = new Image();
        img02.setId( 2 );
        img02.setProduct( prod02 );
        imageRepository.save( img02 );
    }

    private void buildImg01( Product prod01 ) {
        Image img01 = new Image();
        img01.setId( 1 );
        img01.setProduct( prod01 );
        imageRepository.save( img01 );
    }

}
