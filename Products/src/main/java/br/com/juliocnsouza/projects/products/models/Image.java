package br.com.juliocnsouza.projects.products.models;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author julio
 */
@Entity
@Table( name = "image" )
@NamedQueries( {
    @NamedQuery( name = "Image.findByProduct" ,
                 query = "SELECT i FROM Image i INNER JOIN i.product p WHERE p.id = ?1" )
} )
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column( name = "id" )
    private Integer id;

    @JoinColumn( name = "product_id" ,
                 referencedColumnName = "id" )
    @ManyToOne( cascade = CascadeType.ALL )
    private Product product;

    public Image() {
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct( Product product ) {
        this.product = product;
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
        final Image other = ( Image ) obj;
        return Objects.equals( this.id , other.id );
    }

    public void merge( Image imgChangeData ) {
        this.product = imgChangeData.getProduct();
    }

}
