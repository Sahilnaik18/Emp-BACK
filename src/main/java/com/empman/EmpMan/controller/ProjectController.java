package com.empman.EmpMan.controller;

import com.empman.EmpMan.Entities.Project;
import com.empman.EmpMan.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project,
                                              @RequestParam Long adminId,
                                              @RequestParam Long employeeId) {
        return ResponseEntity.ok(projectService.addProject(project, adminId, employeeId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Project>> getProjectsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(projectService.getProjectsByEmployee(employeeId));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Project>> getProjectsByAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(projectService.getProjectsByAdmin(adminId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
