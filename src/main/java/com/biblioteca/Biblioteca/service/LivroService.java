package com.biblioteca.Biblioteca.service;

import com.biblioteca.Biblioteca.dao.LivroDao;
import com.biblioteca.Biblioteca.exception.customExceptions.DadosInvalidosException;
import com.biblioteca.Biblioteca.exception.customExceptions.EntidadeNaoEncontrada;
import com.biblioteca.Biblioteca.exception.customExceptions.ISBNJaCadastradoException;
import com.biblioteca.Biblioteca.model.Livro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class LivroService {

    @Autowired
    private LivroDao livroDao;

    public Livro salvarLivro(Livro livro) throws RuntimeException {
        try {
            validarLivro(livro);

            if(livroDao.buscarPorIsbn(livro.getIsbn()) != null) {
                throw new ISBNJaCadastradoException("Livro com ISBN já cadastrado");
            }

            Long id = livroDao.salvar(livro);
            return livroDao.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro", e);
        }
    }

    public List<Livro> listarTodos() throws RuntimeException {
        try {
            return livroDao.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros", e);
        }
    }

    public Livro buscarPorId(Long id) throws RuntimeException {
        try {
            if (id == null || id <= 0) {
                throw new DadosInvalidosException("ID inválido");
            }
            Livro livro = livroDao.buscarPorId(id);
            if (livro == null) {
                throw new EntidadeNaoEncontrada("Livro não encontrado com ID: " + id);
            }
            return livro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID", e);
        }
    }
    
    public void deletarLivro(Long id) throws RuntimeException {
        try {
            if (id == null || id <= 0) {
                throw new DadosInvalidosException("ID inválido");
            }
            livroDao.deletar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar livro", e);
        }
    }

    public Livro editarLivro(Livro livro) throws RuntimeException {
        try {   
            validarLivro(livro);

            Livro livroDB = livroDao.buscarPorIsbn(livro.getIsbn());

            if (livroDB != null && livroDB.getId() != livro.getId()) {
                throw new ISBNJaCadastradoException("Livro com ISBN já cadastrado");
            }

            livroDao.atualizar(livro);
            return livroDao.buscarPorId(livro.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro", e);
        }
    }

    private void validarLivro(Livro livro){

        if (livro.getQuantidade() < 0) {
            throw new DadosInvalidosException("Quantidade não pode ser negativa");
        }

        if(livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new DadosInvalidosException("Título não pode ser vazio");
        }

        if(livro.getEditora() == null || livro.getEditora().trim().isEmpty()) {
            throw new DadosInvalidosException("Editora não pode ser vazia");
        }

        if(livro.getIsbn() == null || livro.getIsbn().trim().isEmpty()) {
            throw new DadosInvalidosException("ISBN não pode ser vazio");
        }

        if(livro.getAutor() == null || livro.getAutor().trim().isEmpty()) {
            throw new DadosInvalidosException("Autor não pode ser vazio");
        }
    }
}
