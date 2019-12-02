package com.example.uploadfile.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oleg Pavlyukov
 * on 29.11.2019
 * cpabox777@gmail.com
 */
@Data
public class PersonDTO {

    private long id;
    private String name;
    private MultipartFile file;
}
