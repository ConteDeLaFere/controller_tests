package com.malina.controller_tests.services;



import com.malina.controller_tests.model.Student;

import java.util.List;

public interface ExpenseService {

    List<Student> getAllStudents();

    void createExpenses(Student student);

    void deleteExpenses(Long id);
}
