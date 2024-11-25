package com.malina.controller_tests.services.impl;

import com.malina.controller_tests.exceptions.StudentNotFoundException;
import com.malina.controller_tests.model.Avatar;
import com.malina.controller_tests.model.AvatarDto;
import com.malina.controller_tests.model.Student;
import com.malina.controller_tests.repository.AvatarRepository;
import com.malina.controller_tests.repository.StudentRepository;
import com.malina.controller_tests.services.AvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public final class AvatarServiceImpl implements AvatarService {

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    private final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public AvatarDto getFromBd(Long id) {
        logger.debug("Get Avatar by ID from BD {}", id);
        Avatar avatar = avatarRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Аватар с таким Id не существует"));
        return new AvatarDto(avatar.getMediaType(), avatar.getData());
    }

    @Override
    public Avatar getFromDisk(Long id) {
        logger.debug("Get Avatar by ID from Disk {}", id);
        return avatarRepository.findById(id)
                               .orElseThrow(() -> new RuntimeException("Аватар с таким Id не существует"));
    }

    @Override
    public void save(Long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("Save Avatar was invoked");
        Student student = studentRepository.findById(studentId)
                                           .orElseThrow(() -> new StudentNotFoundException("Студента с таким Id не существует"));
        String filePath = saveFileToDisk(multipartFile);
        Avatar avatar = createAvatar(student, multipartFile, filePath);
        avatarRepository.save(avatar);
    }

    private String saveFileToDisk(MultipartFile multipartFile) throws IOException {
        logger.info("File has been save");
        Path filePath = Path.of(avatarsDir, UUID.randomUUID() + "." + getExtension(multipartFile));
        Files.createDirectories(filePath.getParent());

        try (InputStream is = multipartFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            logger.info("Converting bytes ");
            bis.transferTo(bos);
        }
        return filePath.toString();
    }

    private String getExtension(MultipartFile multipartFile) {
        logger.debug("Get file extension {}", multipartFile.getOriginalFilename());
        String fileName = multipartFile.getOriginalFilename();
        if (!fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        logger.error("File does not contain extension");
        throw new RuntimeException("Что-то пошло не так");
    }

    private Avatar createAvatar(Student student, MultipartFile multipartFile, String filePath) throws IOException {
        logger.info("File has been uploaded");
        Avatar avatar = student.getAvatar();
        if (avatar == null) {
            avatar = new Avatar();
        }
        avatar.setStudent(student);
        avatar.setFilePath(filePath);
        avatar.setFileSize(multipartFile.getSize());
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setData(multipartFile.getBytes());
        return avatar;
    }

    @Override
    public List<Avatar> getAllAvatar(@RequestParam("page") Integer pageNumber, @RequestParam("size") Integer pageSize) {
        logger.debug("Get all Avatars {}", pageNumber);
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Номер страницы должен быть больше 0");
        }
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
