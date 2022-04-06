package com.turma20211.mural.dto.request;

import com.turma20211.mural.model.Post;
import com.turma20211.mural.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassRequestDto {

    private Long id;
    private Integer year;
    private Integer semester;
    private Integer courseId;
    private MultipartFile file;
    private Set<User> userList = new HashSet<>();
    private Set<Post> postsList = new HashSet<>();

}
