package com.iva.task_manager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    //ovaj projekat ima listu taskova, mapped by project znaci pogledaj polje project unutar Task
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "project-tasks")//ovo je 'napred' strana veze ukljuci je normalno u json
    private List<Task> tasks; //ako brises projekat obrisu se i svi njegovi taskovi istovremeno

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
