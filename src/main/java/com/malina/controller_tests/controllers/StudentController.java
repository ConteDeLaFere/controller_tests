package com.malina.controller_tests.controllers;

import com.malina.controller_tests.model.Faculty;
import com.malina.controller_tests.model.Student;
import com.malina.controller_tests.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("/faculty/student")
public class StudentController {

    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.add(student);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public List<Student> getAllStudent() {
        return studentService.getAll();
    }

    @GetMapping(path = "getAge/{minAge}/{maxAge}")
    public List<Student> getAllStudentsAge(@PathVariable int minAge, @PathVariable int maxAge) {
        return studentService.getAllStudentsAgeBetween(minAge, maxAge);
    }

    @GetMapping(path = "/faculty/{id}")
    public Faculty getFacultyById(@PathVariable long id) {
        StudentService studentService = (StudentService) getStudent(id);
        return studentService.getFaculty(id);
    }

    @GetMapping(path = "/name-starts-with/{letter}")
    public List<String> getStudentsWhoseNameStartsWith(@PathVariable String letter) {
        if (letter.length() > 1) {
            throw new IllegalArgumentException("Не более одной буквы");
        }
        return studentService.getStudentsWhoseNameStartsWith(letter);
    }

    @GetMapping(path = "/average-age")
    public Double getAverageAgeStudents() {
        return studentService.getAverageAgeStudents();
    }

    @GetMapping(path = "/long-value")
    public Long getLongValue() {
        return studentService.getLongValue();
    }

    @GetMapping(path = "/print-parallel")
    public void getStudentsNamePrintParallel() {
        studentService.getStudentsNamePrintParallel();
    }

    @GetMapping(path = "/print-synchronized")
    public void getStudentsNamePrintSynchronized() {
        studentService.getStudentsNamePrintSynchronized();
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Long id, @RequestBody Student student) {
        Student editStudent = studentService.edit(id, student);
        if (editStudent != null) {
            return ResponseEntity.ok(editStudent);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        Student deletedObject = studentService.delete(id);
        if (deletedObject != null) {
            return ResponseEntity.ok(deletedObject);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
