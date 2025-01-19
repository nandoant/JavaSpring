package com.biblioteca.Biblioteca.controller;

import com.biblioteca.Biblioteca.dto.LivroDto;
import com.biblioteca.Biblioteca.model.Venda;
import com.biblioteca.Biblioteca.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public ResponseEntity<?> realizarVenda(@RequestBody List<LivroDto> livros) {
        try {
            Venda vendaRealizada = vendaService.realizarVenda(livros);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendaRealizada);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar venda: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas() {
        try {
            List<Venda> vendas = vendaService.listarVendas();
            return ResponseEntity.ok(vendas);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        try {
            Venda venda = vendaService.buscarVendaPorId(id);
            if (venda != null) {
                return ResponseEntity.ok(venda);
            }
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarVenda(@PathVariable Long id) {
        try {
            vendaService.deletarVenda(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao deletar venda: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarVenda(@PathVariable Long id, @RequestBody List<LivroDto> livros) {
        try {
            Venda vendaAtualizada = vendaService.editarVenda(id, livros);
            return ResponseEntity.ok(vendaAtualizada);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao editar venda: " + e.getMessage());
        }
    }
}