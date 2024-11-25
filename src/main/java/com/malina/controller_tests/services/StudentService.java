package com.malina.controller_tests.services;



import com.malina.controller_tests.model.Faculty;
import com.malina.controller_tests.model.Student;

import java.util.List;

public interface StudentService extends GeneralService<Student>{

    List<Student> getAllStudentsAgeBetween(int minAge, int maxAge);

    Faculty getFaculty(Long id);

    List<String> getStudentsWhoseNameStartsWith(String letter);

    Double getAverageAgeStudents();

    Long getLongValue();

    void getStudentsNamePrintParallel();

    void getStudentsNamePrintSynchronized();
}