package com.iva.task_manager.controller;

import com.iva.task_manager.model.Task;
import com.iva.task_manager.model.User;
import com.iva.task_manager.repository.TaskRepository;
import com.iva.task_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController //govori springu "ova klasa slusa HTTP zahteve i vraca podatke nazad
@RequestMapping("/api/tasks") //sve sto ova klasa radi dostupno je preko adrese /api/tasks
public class TaskController {
    @Autowired
    private TaskRepository taskRepository; //ubrizgavanje zavisnosti
    @Autowired
    private UserRepository userRepository;
    //"spring, daj mi gotovo TaskRepository da ga koristim ovde,
    //ne mroam sama da pravim newTaskRepository() spring to radi u pozadini


    @GetMapping //kada neko posalje get zahtev na /api/tasks pozove se ova metoda,
    //koja preko repository-ja vrati sve taskove iz baze
    public List<Task> getAllTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskRepository.findByUserUsername(username);
    }
    /* SecurityContextHolder.getContext().getAuthentication() je kutija u koju je jwtAuthFilter
    * ranije stavio podatke o ulogovanom korisniku */

    @PostMapping //kada nkeko posalje post zahtev sa podacima za novi task, pozove se ova metoda
    //koja ga cuva u bazu preko taskRepository.save()
    public Task createTask(@RequestBody Task task) { //ovo u zagradi govori springu "pretvori JSON
        //koji je stigao u pravi Task objekat automatski
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        task.setUser(currentUser);
        return taskRepository.save(task);
        /*Umesto da korisnik u JSON-u salje "user": {"id": X},
        mi sami pronadjemo ko je ulogovan (isti trik kao u getAllTasks) i ručno postavimo
        task.setUser(currentUser) pre čuvanja.
        Korisnik sad šalje samo title, description, completed - ništa više, sistem sam zna čiji je task.*/
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Task " +id+" not found"));
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        //kad adresa izgleda kao /api/task/5 taj 5 je path variable
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task "+id+" not found"));

        if(!task.getUser().getUsername().equals(username)) {
            throw new UsernameNotFoundException("User doesn't have permission to update this task");
        }
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(task);

    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Task task = taskRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Task "+id+" not found"));

        if(!task.getUser().getUsername().equals(username)) {
            throw new UsernameNotFoundException("User doesn't have permission to delete this task");
        }
        taskRepository.deleteById(id);
    }

}
