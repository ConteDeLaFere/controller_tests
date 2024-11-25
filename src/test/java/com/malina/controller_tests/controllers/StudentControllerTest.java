package com.malina.controller_tests.controllers;

import com.malina.controller_tests.model.Student;
import com.malina.controller_tests.repository.StudentRepository;
import com.malina.controller_tests.services.StudentService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository repository;

    @SpyBean
    private StudentService service;

    @InjectMocks
    private StudentController controller;

    @Test
    void getStudent() throws Exception {
        final Long id = 1L;
        final String name = "Potter";
        final int age = 21;

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/student/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id))
                        .andExpect(jsonPath("$.name").value(name))
                        .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void getAllStudent() throws Exception {
        List<Student> students = List.of(new Student());

        when(repository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/student"))
                        .andExpect(status().isOk());
    }

    @Test
    void getAllStudentsAge() throws Exception {
        List<Student> students = List.of(new Student());

        when(repository.findStudentsByAge(any(Integer.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/student/search/"))
                        .andExpect(status().isOk());
    }

    @Test
    void addStudent() throws Exception {
        final Long id = 1L;
        final String name = "Potter";
        final int age = 21;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);

        when(repository.save(any(Student.class))).thenReturn(student);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

    }

    @Test
    void editStudent() throws Exception {
        final Long id = 1L;
        final String name = "Potter";
        final int age = 21;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(repository.save(any(Student.class))).thenReturn(student);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void deleteStudent() throws Exception {
        final Long id = 1L;
        final String name = "Potter";
        final int age = 21;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setName(name);
        student.setId(id);
        student.setAge(age);

        when(repository.save(any(Student.class))).thenReturn(student);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + student.getId()))
                .andExpect(status().isOk());
    }
}
