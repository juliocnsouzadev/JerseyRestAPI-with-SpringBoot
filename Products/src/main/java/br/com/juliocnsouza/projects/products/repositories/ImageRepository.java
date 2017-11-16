package br.com.juliocnsouza.projects.products.repositories;

import br.com.juliocnsouza.projects.products.models.Image;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author julio
 */
public interface ImageRepository extends CrudRepository<Image , Integer> {

    public List<Image> findByProduct( int productId );

}
