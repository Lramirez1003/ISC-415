package edu.pucmm.eitc.encapsulaciones;

import jakarta.persistence.*;
import java.io.Serializable;
@Entity
public class Comentario implements Serializable {

    @Id
    @GeneratedValue
    Long Id;
    String Comment;
    int ProductoId;
    boolean Estado;

    public Comentario(){

    }

    public Comentario(String Comment,int ProductoId){
        this.Comment = Comment;
        this.ProductoId= ProductoId;
        Estado = true;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public int getProductoId() {
        return ProductoId;
    }

    public boolean isEstado() {
        return Estado;
    }

    public void setProductoId(int productoId) {
        ProductoId = productoId;
    }


    public void setEstado(boolean estado) {
        Estado = estado;
    }
}
