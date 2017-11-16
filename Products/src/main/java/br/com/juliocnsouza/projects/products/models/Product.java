package br.com.juliocnsouza.projects.products.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author julio
 */
@Entity
@Table( name = "product" )
@NamedQueries( {
    @NamedQuery( name = "Product.findChildren" ,
                 query = "SELECT p FROM Product p INNER JOIN p.parentProduct parent WHERE parent.id = ?1" )
} )
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column( name = "id" )
    private Integer id;

    @Column( name = "name" )
    private String name;

    @Column( name = "description" )
    private String description;

    @JoinColumn( name = "parent_product_id" ,
                 referencedColumnName = "id" )
    @ManyToOne( cascade = CascadeType.ALL )
    private Product parentProduct;

    @Transient
    private List<Image> images;

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Product getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct( Product parentProduct ) {
        this.parentProduct = parentProduct;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages( List<Image> images ) {
        this.images = images;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode( this.id );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Product other = ( Product ) obj;
        return Objects.equals( this.id , other.id );
    }

    public void merge( Product productChangeData ) {
        if ( productChangeData == null ) {
            return;
        }
        this.description = productChangeData.getDescription() != null
                           ? productChangeData.getDescription()
                           : this.description;
        this.name = productChangeData.getName() != null
                    ? productChangeData.getName()
                    : this.name;
        this.parentProduct = productChangeData.getParentProduct() != null
                             ? productChangeData.getParentProduct()
                             : this.parentProduct;
        this.images = productChangeData.getImages() != null
                      ? parentProduct.getImages()
                      : this.images;
    }

}
