package com.example.uploadfile.repository;

import com.example.uploadfile.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oleg Pavlyukov
 * on 28.11.2019
 * cpabox777@gmail.com
 */
@Repository
public interface ImageRepo extends JpaRepository<Person, Long> {
}
