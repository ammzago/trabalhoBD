package com.example.demo.controllers;

import com.example.demo.model.Cliente;
import com.example.demo.service.UsuarioService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"}) 
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Criar usuário
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Cliente usuario) {
        // Verifica se o ID já existe na base de dados
        if (usuarioService.buscarPorId(usuario.getId()).isPresent()) {
            return ResponseEntity
                    .status(409)
                    .body("Erro: Aluno com este ID já foi cadastrado!");
        }

        // Se o ID não existe, cria um novo usuário
        Cliente novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }



    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Cliente>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PostMapping("/consulta")
    public ResponseEntity<?> executarConsulta(@RequestBody Map<String, String> request) {
        try {
            Map<String, Object> resultado = usuarioService.executarConsultaSQL(request.get("sql"));
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao executar consulta: " + e.getMessage());
        }
    }


    // Buscar usuário por ID
    @GetMapping("/{id:[0-9]+}")
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
