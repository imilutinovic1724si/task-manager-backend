package com.iva.task_manager.controller;

import com.iva.task_manager.model.Project;
import com.iva.task_manager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class ProjectController {

        @Autowired
        private ProjectRepository projectRepository;

        @GetMapping
        public List<Project> getAllProjects() {
            return projectRepository.findAll();
        }

        @PostMapping
        public Project createProject(@RequestBody Project project) {
            return projectRepository.save(project);
        }

}
