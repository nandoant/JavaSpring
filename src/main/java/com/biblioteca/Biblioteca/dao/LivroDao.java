package com.biblioteca.Biblioteca.dao;

import com.biblioteca.Biblioteca.config.Conexao;
import com.biblioteca.Biblioteca.model.Livro;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroDao {
    
    private Conexao conn;

    @PostConstruct
    public void criarTabelaLivro() throws SQLException {
        conn = new Conexao();
        
        String sql = "CREATE TABLE IF NOT EXISTS livro (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "titulo VARCHAR(255) NOT NULL," +
                    "autor VARCHAR(255) NOT NULL," +
                    "editora VARCHAR(255) NOT NULL," +
                    "isbn VARCHAR(13) NOT NULL UNIQUE," +
                    "quantidade INT NOT NULL" +
                    ")";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao criar tabela: " + ex.getMessage());
        }
    }

    public Long salvar(Livro livro) throws SQLException {
        conn = new Conexao();

        String sql = "INSERT INTO livro (titulo, autor, editora, isbn, quantidade) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preencherStatementParaLivro(ps, livro);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Falha ao obter ID gerado para o livro");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao salvar livro: " + ex.getMessage());
        }
    }

    public List<Livro> listar() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        conn = new Conexao();

        String sql = "SELECT * FROM livro";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                livros.add(criarLivroDeResultSet(rs));
            }
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao listar livros: " + ex.getMessage());
        }

        return livros;
    }

    public void atualizar(Livro livro) throws SQLException {
        conn = new Conexao();

        String sql = "UPDATE livro SET titulo=?, autor=?, editora=?, isbn=?, quantidade=? WHERE id=?";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getEditora());
            ps.setString(4, livro.getIsbn());
            ps.setInt(5, livro.getQuantidade());
            ps.setLong(6, livro.getId());
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao atualizar livro: " + ex.getMessage());
        }
    }

    public Livro buscarPorId(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "SELECT * FROM livro WHERE id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return criarLivroDeResultSet(rs);
            }
            return null;
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao buscar livro: " + ex.getMessage());
        }
    }

    public Livro buscarPorIsbn(String isbn) throws SQLException {
        conn = new Conexao();

        String sql = "SELECT * FROM livro WHERE isbn = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setString(1, isbn);
            rs = ps.executeQuery();

            if (rs.next()) {
                return criarLivroDeResultSet(rs);
            }
            return null;
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao buscar livro por ISBN: " + ex.getMessage());
        }
    }

    public void deletar(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "DELETE FROM livro WHERE id = ?";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao deletar livro: " + ex.getMessage());
        }
    }

    private void preencherStatementParaLivro(PreparedStatement ps, Livro livro) throws SQLException {
        ps.setString(1, livro.getTitulo());
        ps.setString(2, livro.getAutor());
        ps.setString(3, livro.getEditora());
        ps.setString(4, livro.getIsbn());
        ps.setInt(5, livro.getQuantidade());
    }

    private Livro criarLivroDeResultSet(ResultSet rs) throws SQLException {
        return new Livro(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("editora"),
            rs.getString("isbn"),
            rs.getInt("quantidade")
        );
    }
}