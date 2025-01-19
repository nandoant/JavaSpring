package com.biblioteca.Biblioteca.service;

import com.biblioteca.Biblioteca.dao.*;
import com.biblioteca.Biblioteca.dto.LivroDto;
import com.biblioteca.Biblioteca.exception.customExceptions.*;
import com.biblioteca.Biblioteca.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

@Service
public class VendaService {
    @Autowired private VendaDao vendaDao;
    @Autowired private ItemVendaDao itemVendaDao;
    @Autowired private LivroDao livroDao;

    public Venda realizarVenda(List<LivroDto> livrosDto) throws SQLException {
        if (livrosDto.isEmpty()) {
            throw new DadosInvalidosException("Nenhum livro informado para a venda");
        }

        HashMap<String, Integer> quantidades = new HashMap<>();
        for (LivroDto livro : livrosDto) {
            quantidades.put(livro.getIsbn(), quantidades.getOrDefault(livro.getIsbn(), 0) + 1);
        }

        validarEstoque(quantidades);
        
        Venda venda = new Venda(null, new Date(System.currentTimeMillis()), livrosDto);
        venda.setId(vendaDao.salvar(venda));
        
        salvarItensVenda(venda.getId(), quantidades);
        atualizarEstoqueLivros(quantidades, -1);
        
        return venda;
    }

    public Venda editarVenda(Long id, List<LivroDto> novosLivros) throws SQLException {
        Venda vendaExistente = buscarVendaPorId(id);
        if (vendaExistente == null) {
            throw new EntidadeNaoEncontrada("Venda não encontrada");
        }

        HashMap<String, Integer> qtdNovos = contarQuantidadeLivros(novosLivros);
        HashMap<String, Integer> qtdAntigos = contarQuantidadeLivros(vendaExistente.getLivros());
        
        validarDiferencaEstoque(qtdNovos, qtdAntigos);
        
        itemVendaDao.deletarPorIdVenda(id);
        atualizarEstoqueLivros(qtdAntigos, 1);
        atualizarEstoqueLivros(qtdNovos, -1);
        salvarItensVenda(id, qtdNovos);

        return buscarVendaPorId(id);
    }

    private HashMap<String, Integer> contarQuantidadeLivros(List<LivroDto> livros) {
        HashMap<String, Integer> quantidade = new HashMap<>();
        for (LivroDto livro : livros) {
            quantidade.put(livro.getIsbn(), quantidade.getOrDefault(livro.getIsbn(), 0) + 1);
        }
        return quantidade;
    }

    private void validarEstoque(HashMap<String, Integer> quantidades) throws SQLException {
        ArrayList<String> erros = new ArrayList<>();
        for (String isbn : quantidades.keySet()) {
            Livro livro = livroDao.buscarPorIsbn(isbn);
            int qtdSolicitada = quantidades.get(isbn);
            
            if (livro == null || livro.getQuantidade() < qtdSolicitada) {
                String mensagem = String.format("Livro '%s' tem apenas %d unidades, mas foram solicitadas %d unidades.",
                    livro != null ? livro.getTitulo() : "não encontrado",
                    livro != null ? livro.getQuantidade() : 0,
                    qtdSolicitada);
                erros.add(mensagem);
            }
        }
        if (!erros.isEmpty()) {
            throw new EstoqueInsuficienteException(String.join("\n", erros));
        }
    }

    private void validarDiferencaEstoque(HashMap<String, Integer> novos, HashMap<String, Integer> antigos) throws SQLException {
        HashMap<String, Integer> diferenca = new HashMap<>();
        for (String isbn : novos.keySet()) {
            int qtdNova = novos.get(isbn);
            int qtdAntiga = antigos.getOrDefault(isbn, 0);
            if (qtdNova > qtdAntiga) {
                diferenca.put(isbn, qtdNova - qtdAntiga);
            }
        }
        validarEstoque(diferenca);
    }

    private void salvarItensVenda(Long idVenda, HashMap<String, Integer> quantidades) throws SQLException {
        for (String isbn : quantidades.keySet()) {
            Livro livro = livroDao.buscarPorIsbn(isbn);
            int quantidade = quantidades.get(isbn);
            for (int i = 0; i < quantidade; i++) {
                itemVendaDao.salvar(new ItemVenda(livro.getId(), idVenda, null));
            }
        }
    }

    private void atualizarEstoqueLivros(HashMap<String, Integer> quantidades, int multiplicador) throws SQLException {
        for (String isbn : quantidades.keySet()) {
            Livro livro = livroDao.buscarPorIsbn(isbn);
            int quantidade = quantidades.get(isbn);
            livro.setQuantidade(livro.getQuantidade() + (quantidade * multiplicador));
            livroDao.atualizar(livro);
        }
    }

    public List<Venda> listarVendas() throws SQLException {
        List<Venda> vendas = vendaDao.listar();
        List<Venda> vendasCompletas = new ArrayList<>();
        for (Venda venda : vendas) {
            vendasCompletas.add(carregarLivrosDaVenda(venda));
        }
        return vendasCompletas;
    }

    public Venda buscarVendaPorId(Long id) throws SQLException {
        Venda venda = vendaDao.buscarPorId(id);
        return venda != null ? carregarLivrosDaVenda(venda) : null;
    }

    public void deletarVenda(Long id) throws SQLException {
        Venda venda = buscarVendaPorId(id);
        if (venda != null) {
            HashMap<String, Integer> quantidades = contarQuantidadeLivros(venda.getLivros());
            atualizarEstoqueLivros(quantidades, 1);
            itemVendaDao.deletarPorIdVenda(id);
            vendaDao.deletar(id);
        }
    }

    private Venda carregarLivrosDaVenda(Venda venda) throws SQLException {
        List<ItemVenda> itens = itemVendaDao.buscarPorVendaId(venda.getId());
        List<LivroDto> livrosDtos = new ArrayList<>();
        
        for (ItemVenda item : itens) {
            Livro livro = livroDao.buscarPorId(item.getIdLivro());
            if (livro != null) {
                livrosDtos.add(new LivroDto(
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getEditora(),
                    livro.getIsbn()
                ));
            }
        }
        
        venda.setLivros(livrosDtos);
        return venda;
    }
}