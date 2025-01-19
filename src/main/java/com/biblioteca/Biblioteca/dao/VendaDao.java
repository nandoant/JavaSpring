package com.biblioteca.Biblioteca.dao;

import com.biblioteca.Biblioteca.config.Conexao;
import com.biblioteca.Biblioteca.model.Venda;

import jakarta.annotation.PostConstruct;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class VendaDao {

    private Conexao conn;

    @PostConstruct
    public void criarTabelaVenda() throws SQLException {
        conn = new Conexao();
        
        String sql = "CREATE TABLE IF NOT EXISTS venda (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "data_venda DATE NOT NULL" +
                    ")";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao criar tabela: " + ex.getMessage());
        }
    }

    public Long salvar(Venda venda) throws SQLException {
        conn = new Conexao();

        String sql = "INSERT INTO venda (data_venda) VALUES (?)";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDate(1, venda.getDataVenda());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new SQLException("Falha ao obter ID gerado para a venda");
            
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao salvar venda: " + ex.getMessage());
        }
    }

    public void deletar(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "DELETE FROM venda WHERE id=?";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            ps.execute();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao deletar venda: " + ex.getMessage());
        }
    }

    public List<Venda> listar() throws SQLException {
        List<Venda> vendas = new ArrayList<>();

        conn = new Conexao();

        String sql = "SELECT * FROM venda";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                vendas.add(criarVendaDeResultSet(rs));
            }
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao listar vendas: " + ex.getMessage());
        }

        return vendas;
    }

    public Venda buscarPorId(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "SELECT * FROM venda WHERE id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return criarVendaDeResultSet(rs);
            }
            return null;
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao buscar venda: " + ex.getMessage());
        }
    }

    private Venda criarVendaDeResultSet(ResultSet rs) throws SQLException {
        return new Venda(
            rs.getLong("id"),
            rs.getDate("data_venda"),
            null  // Lista de livros será preenchida no service
        );
    }
}