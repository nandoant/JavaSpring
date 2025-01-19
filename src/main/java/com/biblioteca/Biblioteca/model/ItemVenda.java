package com.biblioteca.Biblioteca.model;

public class ItemVenda {
    private Long id;
    private Long idVenda;
    private Long idLivro;   

    public ItemVenda(Long idLivro, Long idVenda, Long id) {
        this.idLivro = idLivro;
        this.idVenda = idVenda;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }
}
