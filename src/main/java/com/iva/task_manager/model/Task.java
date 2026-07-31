package com.iva.task_manager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity // javljam springu da ovo nije obicna klasa, vec tabela u bazi, bez toga spring ne bi znao da ovo treba da cuva u bazi
public class Task {
    @Id // ovo polje je jedinstveni identifikator
    @GeneratedValue(strategy = GenerationType.IDENTITY) //baza sama automatski generise ID
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    @ManyToOne //mnogo taskova moze da pripada jednom projektu
    @JoinColumn(name = "project_id") //u task tabeli napravi kolonu project id koja cuva id projekta kome task pripada
    //to je strani kljuc
    @JsonBackReference(value = "project-tasks") //ovo je 'nazad' strana veze preskoci je kad serijalizujes Task da ne napravis petlju
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-tasks")
    private User user;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
