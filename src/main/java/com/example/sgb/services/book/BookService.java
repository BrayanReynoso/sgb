package com.example.sgb.services.book;

import com.example.sgb.models.book.Book;
import com.example.sgb.repositories.book.BookRepository;
import com.example.sgb.utils.CustomResponse;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public CustomResponse<List<Book>> getAll(){
        List<Book> exist = this.bookRepository.findAll();
        if(!exist.isEmpty()){
            return new CustomResponse<>(
                   this.bookRepository.findAll(),
                   "OK",
                   false,
                   200
            );
        }
        return new CustomResponse<>(
                null,
                "Data is empty",
                false,
                400
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Book> getById(Long id) {
        Optional<Book> exist = this.bookRepository.findById(id);
        if (!exist.isPresent()) {
            return new CustomResponse<>(
                    null, "El libro no se encuentra", true, 404 // Error si no se encuentra el libro
            );
        }
        return new CustomResponse<>(
                exist.get(), "Libro encontrado", false, 200 // No es un error si se encuentra el libro
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Book>> getAllByStatusActive(){
        List<Book> books = this.bookRepository.findAllByStatusIsTrue();
        if (!books.isEmpty()) {
            return new CustomResponse<>(
                    books,
                    "OK",
                    false,
                    200
            );
        }
        return new CustomResponse<>(
                null,
                "Data is empty",
                false,
                400
        );
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Book> create(Book book){
        // Si el libro tiene ID, verificamos que no exista
        if (book.getId() != null) {
            Optional<Book> exist = this.bookRepository.findById(book.getId());
            if(exist.isPresent()){
                return new CustomResponse<>(
                        null,
                        "Error book already exists",
                        true,
                        400
                );
            }
        }

        // Si llegamos aquí, podemos crear el libro
        return new CustomResponse<>(
                this.bookRepository.save(book),
                "Book created successfully",
                false,
                200
        );
    }

    @Transactional
    public CustomResponse<Book> updateBook(Long bookId, Book updatedBook) {
        Optional<Book> existingBook = this.bookRepository.findById(bookId);

        if (!existingBook.isPresent()) {
            return new CustomResponse<>(
                    null,
                    "Book not found",
                    true,
                    404
            );
        }

        Book book = existingBook.get();

        // Validar datos antes de actualizar
        if (updatedBook.getTitle() != null) book.setTitle(updatedBook.getTitle());
        if (updatedBook.getDescription() != null) book.setDescription(updatedBook.getDescription());
        if (updatedBook.getPublishedDate() != null) book.setPublishedDate(updatedBook.getPublishedDate());
        //if (updatedBook.getAuthor() != null) book.setAuthor(updatedBook.getAuthor());
        if (updatedBook.getCategories() != null) book.setCategories(updatedBook.getCategories());
        book.setStatus(updatedBook.isStatus());

        // Guardar cambios
        Book savedBook = this.bookRepository.save(book);

        return new CustomResponse<>(
                savedBook,
                "Book updated successfully",
                false,
                200
        );
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Book> changeStatusById(long id){
        Optional<Book> exist = this.bookRepository.findById(id);
        if(exist.isPresent()){
            exist.get().setStatus(!exist.get().isStatus());
            return new CustomResponse<>(
                    this.bookRepository.save(exist.get()),
                    "Change status succesfull",
                    false,
                    200
            );
        }
        return new CustomResponse<>(
                null,
                "Error",
                true,
                400
        );
    }
}
