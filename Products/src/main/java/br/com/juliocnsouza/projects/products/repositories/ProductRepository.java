package br.com.juliocnsouza.projects.products.repositories;

import br.com.juliocnsouza.projects.products.models.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author julio
 */
public interface ProductRepository extends CrudRepository<Product , Integer> {

    public List<Product> findChildren( int id );

}
