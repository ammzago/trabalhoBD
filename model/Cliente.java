package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno")
public class Cliente {
    @Id
    @Column(name = "idusuario")
    private Long idUsuario;


    private String matricula;
    private String periodo;
    private String curso;

    // Getters e Setters
    public Long getId() { return idUsuario; }
    public void setId(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
}
