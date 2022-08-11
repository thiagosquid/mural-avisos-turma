package com.muraldaturma.api.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.muraldaturma.api.dto.UserDTO;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static com.muraldaturma.api.configuration.PropertiesConfiguration.CLOUDINARY_URL;


@RestController
@RequestMapping("/file")
public class FileUploadController {

    @PostMapping
    public String upload(@RequestPart @Nullable MultipartFile file, @RequestPart @Valid UserDTO user) throws IOException {

        return upload2(file, user, "profile");

    }

    private String upload2(@RequestPart @Nullable MultipartFile file, @RequestPart UserDTO userDTO, String folder) throws IOException {
        if (file == null) {
            throw new FileNotFoundException("Arquivo vazio");
        }
        System.out.println(CLOUDINARY_URL);
        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);

        return (String) cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("public_id", String.format("%s/%s", folder, UUID.randomUUID()))).get("url");
    }

    @PostMapping("/attachment")
    public String uploadAttachment(@RequestParam @Nullable MultipartFile file, UserDTO user) throws IOException {

        return upload2(file, user, "post_attachment");
    }

}
