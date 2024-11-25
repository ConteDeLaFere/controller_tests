package com.malina.controller_tests.services.impl;

import com.malina.controller_tests.model.Faculty;
import com.malina.controller_tests.model.Student;
import com.malina.controller_tests.repository.StudentRepository;
import com.malina.controller_tests.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public final class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final Object syncObject = new Object();

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        logger.info("The Student has been added to application");
        student.setId(null);
        return studentRepository.save(student);
    }

    @Override
    public Student get(Long id) {
        logger.info("Received the student of ID");
        return studentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public Student edit(Long id, Student student) {
        logger.info("The Student has been updated");
        return studentRepository.findById(id)
                .map(facultyDb -> {
                    student.setId(id);
                    return studentRepository.save(student);
                })
                .orElse(null);
    }

    @Override
    public Student delete(Long id) {
        logger.info("The Student of ID has been deleted");
        return studentRepository.findById(id)
                .map(faculty -> {
                    studentRepository.deleteById(id);
                    return faculty;
                })
                .orElse(null);
    }

    @Override
    public List<Student> getAll() {
        logger.info("All students have been received");
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsAgeBetween(int minAge, int maxAge) {
        logger.info("A Students is received between the minimum and maximum years");
        if (minAge > maxAge) {
            throw new IllegalArgumentException("Некорректно задан промежуток возраста");
        }
        return studentRepository.findStudentsByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFaculty(Long id) {
        logger.info("Get Faculty by ID {}", id);
        return studentRepository.findById(id)
                .map(Student::getFacultyStudent)
                .orElse(null);

    }

    @Override
    public List<String> getStudentsWhoseNameStartsWith(String letter) {
        logger.info("Called method getStudentsWhoseNameStartWith");
        return studentRepository.findAll().stream()
                .parallel()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith(letter.toUpperCase()))
                .toList();
    }

    @Override
    public Double getAverageAgeStudents() {
        logger.info("Called method getAverageAgeStudents");
        return studentRepository.findAll().stream()
                .parallel()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public Long getLongValue() {
        long start = System.currentTimeMillis();
        long result = 0;
        for (int i = 1; i <= 1_000_000; i++) {
            result = result + i;
        }
        logger.info("Время выполнения - {} мс", System.currentTimeMillis() - start);
        return result;
    }

    @Override
    public void getStudentsNamePrintParallel() {
        logger.info("Called method getStudentsNamePrintParallel");
        List<Student> students = studentRepository.findAll();

        if (students.size() >= 6) {
            logger.info(students.get(0).getName());
            logger.info(students.get(1).getName());

            Thread thread1 = new Thread(() -> {
                logger.info(students.get(2).getName());
                logger.info(students.get(3).getName());
            });

            Thread thread2 = new Thread(() -> {
                logger.info(students.get(4).getName());
                logger.info(students.get(5).getName());
            });

            thread1.start();
            thread2.start();
        }
    }

    @Override
    public void getStudentsNamePrintSynchronized() {
        logger.info("Called method getStudentsNamePrintSynchronized");
        List<Student> students = studentRepository.findAll();

        printName(students.get(0).getName());
        printName(students.get(1).getName());

        new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        }).start();

        new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        }).start();
    }

    private void printName(String name) {
        synchronized (syncObject) {
            logger.info(name);
        }
    }
}
