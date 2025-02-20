package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private ClienteRepository usuarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

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


    public Map<String, Object> executarConsultaSQL(String sql) {
        entityManager.clear();

        // Cria a query nativa
        Query query = entityManager.createNativeQuery(sql);

        // Se a consulta for um SELECT
        if (sql.trim().toUpperCase().startsWith("SELECT")) {
            List<Object[]> resultados = query.getResultList();

            // Obtém os nomes das colunas usando metadados
            List<String> nomesColunas = new ArrayList<>();

            try {
                // Acessa a conexão do EntityManager e executa a consulta
                entityManager.unwrap(org.hibernate.Session.class)
                             .doWork(connection -> {
                                 try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
                                     ResultSetMetaData rsMetaData = resultSet.getMetaData();
                                     int numColunas = rsMetaData.getColumnCount();
                                     for (int i = 1; i <= numColunas; i++) {
                                         nomesColunas.add(rsMetaData.getColumnLabel(i));
                                     }
                                 } catch (SQLException e) {
                                     e.printStackTrace();
                                 }
                             });
            } catch (Exception e) {
                // Lidar com exceções, caso não consiga acessar os metadados
                e.printStackTrace();
            }

            // Cria a resposta formatada
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("colunas", nomesColunas);
            resposta.put("dados", resultados);

            return resposta;
        } else {
            // Se for um comando diferente de SELECT
            int linhasAfetadas = query.executeUpdate();
            entityManager.flush();  // Garante que a BD está sincronizada
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("linhasAfetadas", linhasAfetadas);
            return resposta;
        }
    }


}
