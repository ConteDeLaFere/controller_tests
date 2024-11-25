package com.malina.controller_tests.controllers;

import com.malina.controller_tests.exceptions.StudentNotFoundException;
import com.malina.controller_tests.model.Avatar;
import com.malina.controller_tests.model.AvatarDto;
import com.malina.controller_tests.services.AvatarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping(value = "/{id}/db-preview")
    public ResponseEntity<byte[]> getFromBd(@PathVariable Long id) {
        AvatarDto avatarDto = avatarService.getFromBd(id);
        HttpHeaders headers = getAvatarHttpHeaders(avatarDto);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatarDto.bytes());
    }

    private HttpHeaders getAvatarHttpHeaders(AvatarDto avatarDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatarDto.type()));
        headers.setContentLength(avatarDto.bytes().length);
        return headers;
    }

    @GetMapping(value = "/{id}/disk-preview")
    public void getFromDisk(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.getFromDisk(id);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()) {
            response.setStatus(SC_OK);
            response.setContentType(avatar.getMediaType());
            response.setContentLength(Math.toIntExact(avatar.getFileSize()));
            is.transferTo(os);
        }
    }

    @PostMapping(value = "/{studentId}/save", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> save(@PathVariable Long studentId, @RequestParam MultipartFile multipartFile) {
        try {
            avatarService.save(studentId, multipartFile);
            return ResponseEntity.ok("Загрузка файла завершена");
        } catch (StudentNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(exception.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка");
        }
    }

    @GetMapping("/page-list")
    public ResponseEntity<List<Avatar>> getAllAvatar(@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        List<Avatar> avatar = avatarService.getAllAvatar(pageNumber, pageSize);
        return ResponseEntity.ok(avatar);
    }
}
