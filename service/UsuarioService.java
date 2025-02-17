package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private ClienteRepository usuarioRepository;

    public List<Cliente> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Cliente salvarUsuario(Cliente usuario) {
        return usuarioRepository.save(usuario);
    }

    public Cliente atualizarUsuario(Long id, Cliente usuarioAtualizado) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setMatricula(usuarioAtualizado.getMatricula());
                usuario.setPeriodo(usuarioAtualizado.getPeriodo());
                usuario.setCurso(usuarioAtualizado.getCurso());
                return usuarioRepository.save(usuario);
            }).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
