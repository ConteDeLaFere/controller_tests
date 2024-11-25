package com.malina.controller_tests.services;

import com.malina.controller_tests.model.Avatar;
import com.malina.controller_tests.model.AvatarDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AvatarService{

    AvatarDto getFromBd(Long id);

    Avatar getFromDisk(Long id);

    void save(Long studentId, MultipartFile multipartFile) throws IOException;

    List<Avatar> getAllAvatar(Integer pageNumber, Integer pageSize);
}
