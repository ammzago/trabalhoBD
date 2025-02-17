package com.example.demo.controllers;

import com.example.demo.model.Cliente;
import com.example.demo.service.UsuarioService;
import org.springframework.ui.Model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Criar usuário
    @PostMapping
    public ResponseEntity<Cliente> criarUsuario(@RequestBody Cliente usuario) {
        return ResponseEntity.ok(usuarioService.salvarUsuario(usuario));
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Cliente>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }


    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarUsuario(@PathVariable Long id, @RequestBody Cliente usuario) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuario));
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
