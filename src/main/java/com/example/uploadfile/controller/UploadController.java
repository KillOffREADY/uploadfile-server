package com.example.uploadfile.controller;

import com.example.uploadfile.model.Person;
import com.example.uploadfile.model.PersonDTO;
import com.example.uploadfile.repository.ImageRepo;
import com.example.uploadfile.service.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 28.11.2019
 * cpabox777@gmail.com
 */
@CrossOrigin
@RestController
public class UploadController {

    private FileStorage storage;
    private ImageRepo repository;
    private List<String> files;

    @Autowired
    public UploadController(FileStorage storage, ImageRepo repository) {
        this.storage = storage;
        this.repository = repository;
        files = new ArrayList<>();
    }

    @GetMapping("/files")
    public List<String> getAllFiles(Model model) {
        List<String> fileNames = files.stream()
                .map(file -> MvcUriComponentsBuilder
                        .fromMethodName(UploadController.class, "getFile", file).build().toString())
                .collect(Collectors.toList());
        return fileNames;
    }
//
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource resource = storage.getFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        storage.store(file);
        files.add(file.getOriginalFilename());
        message = "Success. " + file.getOriginalFilename() + " uploaded successfully";
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/files/employee")
    public Person savePersonImage(@ModelAttribute PersonDTO personDTO) {
        String path = "http://localhost:8080/files/";
        System.out.println(personDTO.getFile().getOriginalFilename());
        storage.store(personDTO.getFile());
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setImage(path + personDTO.getFile().getOriginalFilename());
        return repository.save(person);
    }

    @GetMapping("/files/person")
    public List<Person> getAllPersons() {
        return repository.findAll();
    }
}
//hello
