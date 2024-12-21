package com.example.sgb.repositories.book;

import com.example.sgb.models.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    List<Book> findAllByStatusIsTrue();

    Optional<Book> findById(long id);
}
