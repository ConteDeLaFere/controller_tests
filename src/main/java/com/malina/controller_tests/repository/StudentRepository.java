package com.malina.controller_tests.repository;


import com.malina.controller_tests.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findStudentsByAgeBetween(int minAge, int maxAge);

    List<Student> findStudentsByAge(int age);

}