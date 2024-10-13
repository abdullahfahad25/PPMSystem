package com.example.ppmsystem.web;

import com.example.ppmsystem.domain.Project;
import com.example.ppmsystem.domain.User;
import com.example.ppmsystem.services.MapValidationErrorService;
import com.example.ppmsystem.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.header.Header;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result,
                                              Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<String>("Project wtih id: " + projectId + " is deleted successfully", HttpStatus.OK);
    }
}