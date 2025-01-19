package com.biblioteca.Biblioteca.dao;

import com.biblioteca.Biblioteca.config.Conexao;
import com.biblioteca.Biblioteca.model.ItemVenda;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemVendaDao {

    private Conexao conn;

    @PostConstruct
    public void criarTabelaItemVenda() throws SQLException {
        conn = new Conexao();
        
        String sql = "CREATE TABLE IF NOT EXISTS item_venda (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "id_venda BIGINT NOT NULL," +
                    "id_livro BIGINT NOT NULL," +
                    "FOREIGN KEY (id_venda) REFERENCES venda(id)," +
                    "FOREIGN KEY (id_livro) REFERENCES livro(id)" +
                    ")";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao criar tabela: " + ex.getMessage());
        }
    }

    public void salvar(ItemVenda itemVenda) throws SQLException {
        conn = new Conexao();

        String sql = "INSERT INTO item_venda (id_venda, id_livro) VALUES (?, ?)";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, itemVenda.getIdVenda());
            ps.setLong(2, itemVenda.getIdLivro());
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao salvar item venda: " + ex.getMessage());
        }
    }

    public List<ItemVenda> listar() throws SQLException {
        List<ItemVenda> itensVenda = new ArrayList<>();
        conn = new Conexao();

        String sql = "SELECT * FROM item_venda";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                itensVenda.add(criarItemVendaDeResultSet(rs));
            }
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao listar itens venda: " + ex.getMessage());
        }

        return itensVenda;
    }

    public ItemVenda buscarPorId(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "SELECT * FROM item_venda WHERE id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return criarItemVendaDeResultSet(rs);
            }
            return null;
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao buscar item venda: " + ex.getMessage());
        }
    }

    public List<ItemVenda> buscarPorVendaId(Long id) throws SQLException {
        List<ItemVenda> itensVenda = new ArrayList<>();
        conn = new Conexao();

        String sql = "SELECT * FROM item_venda WHERE id_venda = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                itensVenda.add(criarItemVendaDeResultSet(rs));
            }
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao buscar itens por venda: " + ex.getMessage());
        }

        return itensVenda;
    }

    public void deletar(Long id) throws SQLException {
        conn = new Conexao();

        String sql = "DELETE FROM item_venda WHERE id = ?";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao deletar item venda: " + ex.getMessage());
        }
    }

    public void deletarPorIdVenda(Long idVenda) throws SQLException {
        conn = new Conexao();

        String sql = "DELETE FROM item_venda WHERE id_venda = ?";

        PreparedStatement ps = null;
        try {
            ps = conn.getConexao().prepareStatement(sql);
            ps.setLong(1, idVenda);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Erro ao deletar itens por venda: " + ex.getMessage());
        }
    }

    private ItemVenda criarItemVendaDeResultSet(ResultSet rs) throws SQLException {
        return new ItemVenda(
            rs.getLong("id_livro"),
            rs.getLong("id_venda"),
            rs.getLong("id")
        );
    }
}