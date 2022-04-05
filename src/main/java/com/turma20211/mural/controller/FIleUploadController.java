package com.turma20211.mural.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.turma20211.mural.dto.request.ClassRequestDto;
import com.turma20211.mural.model.Class;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/file")
public class FIleUploadController {

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "hccttzbso",
            "api_key", "424215662562113",
            "api_secret", "1hP3OYpEEVevh4-VGld2ab_6mvE"));


    @PostMapping
    public void upload(@RequestParam @Nullable MultipartFile file, ClassRequestDto classToSave) throws IOException {
        if (file == null){
            return;
        }
        classToSave.setFile(file);
        System.out.println(file.getOriginalFilename().contains('.' + file.getContentType().split("/")[1]));
        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - file.getContentType().split("/")[1].length() - 1);
        System.out.println(fileName);
        System.out.println(file.getContentType());
        System.out.println(classToSave);
        String url = (String) cloudinary.uploader().upload(classToSave.getFile().getBytes(),
                ObjectUtils.asMap("public_id", fileName + "_" + UUID.randomUUID())).get("url");

        System.out.println(url);
    }
}
