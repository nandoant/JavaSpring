package com.biblioteca.Biblioteca.model;

import com.biblioteca.Biblioteca.dto.LivroDto;

import java.sql.Date;
import java.util.List;

public class Venda {
    private Long id;
    private Date dataVenda;
    private List<LivroDto> livros;

    public Venda(Long id, Date dataVenda, List<LivroDto> livros) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.livros = livros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<LivroDto> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroDto> livros) {
        this.livros = livros;
    }
}
