package com.malina.controller_tests.services.impl;

import com.malina.controller_tests.model.Faculty;
import com.malina.controller_tests.repository.FacultyRepository;
import com.malina.controller_tests.services.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public final class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty add(Faculty faculty) {
        logger.info("The Faculty has been added to application");
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty get(Long id) {
        logger.info("Received the Faculty of ID");
        return facultyRepository.findById(id)
                                .orElse(null);
    }

    @Override
    public Faculty edit(Long id, Faculty faculty) {
        logger.info("The Faculty has been updated");
        return facultyRepository.findById(id)
                                .map(facultyDb -> {
                                  faculty.setId(id);
                                  return facultyRepository.save(faculty);
                                 })
                                .orElse(null);
    }

    @Override
    public Faculty delete(Long id) {
        logger.info("The Faculty of ID has been deleted");
        return facultyRepository.findById(id)
                                .map(faculty -> {
                                    facultyRepository.deleteById(id);
                                    return faculty;
                                })
                                .orElse(null);
    }

    @Override
    public List<Faculty> getAll() {
        logger.info("All faculties have been received");
        return facultyRepository.findAll();
    }

    @Override
    public List<Faculty> getAllFacultyColor(String color) {
        logger.info("Faculty by name and color found");
        return facultyRepository.findFacultiesByColor(color);
    }

    @Override
    public List<Faculty> findFacultyByName(String filter) {
        logger.debug("Find faculties by name {}", filter);
        List<Faculty> filteredFaculties = new ArrayList<>();
        for (Faculty faculty : getAll()) {
            if (faculty.getName().contains(filter)) {
                filteredFaculties.add(faculty);
            }
        }
        return filteredFaculties;
    }

    @Override
    public String getFacultyWithLongName() {
        logger.info("Called method getFacultyWithLongName");
        return facultyRepository.findAll().stream()
                .parallel()
                .map(Faculty::getName)
                .sorted(String::compareTo)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }
}
