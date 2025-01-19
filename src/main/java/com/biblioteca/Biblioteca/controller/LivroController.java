package com.biblioteca.Biblioteca.controller;

import com.biblioteca.Biblioteca.model.Livro;
import com.biblioteca.Biblioteca.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("livros")
public class LivroController {
    
    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
        Livro livroSalvo = livroService.salvarLivro(livro);
        return new ResponseEntity<>(livroSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarLivros() {
        List<Livro> livros = livroService.listarTodos();
        return new ResponseEntity<>(livros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivro(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return new ResponseEntity<>(livro, HttpStatus.OK);
       
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletarLivro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> editarLivro(@PathVariable Long id, @RequestBody Livro livro) {
        livro.setId(id);
        Livro livroAtualizado = livroService.editarLivro(livro);
        return new ResponseEntity<>(livroAtualizado, HttpStatus.OK);
    }
}
