package com.muraldaturma.api.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/file")
public class FileUploadController {
//Teste
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "hccttzbso",
            "api_key", "424215662562113",
            "api_secret", "1hP3OYpEEVevh4-VGld2ab_6mvE"));

    @GetMapping
    public String test() {
        return error();
    }

    @PostMapping
    public String testPost(@RequestBody @Valid BodyTest bodyTest) {

        return "";
    }

//    @PostMapping
//    public void upload(@RequestParam @Nullable MultipartFile file, ClassRequestDto classToSave) throws IOException {
//        if (file == null){
//            return;
//        }
//        classToSave.setFile(file);
//        System.out.println(file.getOriginalFilename().contains('.' + file.getContentType().split("/")[1]));
//        String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - file.getContentType().split("/")[1].length() - 1);
//        System.out.println(fileName);
//        System.out.println(file.getContentType());
//        System.out.println(classToSave);
//        String url = (String) cloudinary.uploader().upload(classToSave.getFile().getBytes(),
//                ObjectUtils.asMap("public_id", fileName + "_" + UUID.randomUUID())).get("url");
//
//        System.out.println(url);
//    }

    private String error(){
        throw new RuntimeException("Deu erro");
    }

    public static class BodyTest{
        @NotBlank
        private String name;

        @NotBlank
        private String city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}
