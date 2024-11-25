package com.malina.controller_tests.services;


import com.malina.controller_tests.model.Faculty;

import java.util.List;

public interface FacultyService extends GeneralService<Faculty> {

    List<Faculty> getAllFacultyColor(String color);

    List<Faculty> findFacultyByName(String filter);

    String getFacultyWithLongName();
}