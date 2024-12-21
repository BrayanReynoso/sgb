package com.example.sgb.repositories.author;

import com.example.sgb.models.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    Optional<Author> findById(Long id);

    Optional<Author> findByName(final String name);
}
