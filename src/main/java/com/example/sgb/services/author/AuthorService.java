package com.example.sgb.services.author;

import com.example.sgb.models.author.Author;
import com.example.sgb.models.categories.Category;
import com.example.sgb.repositories.author.AuthorRepository;
import com.example.sgb.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository repository;

    @Transactional(readOnly = true)
    public CustomResponse<List<Author>> getAllAuthors() {
        List<Author> authors = repository.findAll();
        if (!authors.isEmpty()){
            return new CustomResponse<>(
                    this.repository.findAll(),
                    "OK",
                    false,
                    200
            );
        }
        return new CustomResponse<>(
                null,
                "Data is empty",
                true,
                400

        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Author> getAuthorById(Long id) {
        try {
            return  this.repository.findById(id)
                    .map(author -> new CustomResponse<>(author, "Autor encontrado", false, 200))
                    .orElseGet(() -> new CustomResponse<>(null, "Autor no encontrado", true, 404));
        }catch (DataAccessException e){
            return new CustomResponse<>(null, "Ocurrio un error al intentar buscar el author", true, 500);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Author> createAuthor(Author author) {
        try {
            // Validar si el autor ya existe
            Optional<Author> existingAuthor = this.repository.findByName(author.getName());
            if (existingAuthor.isPresent()) {
                return new CustomResponse<>(
                        null,
                        "El autor con el nombre '" + author.getName() + "' ya está registrado.",
                        true,
                        400
                );
            }

            // Guardar el nuevo autor
            Author savedAuthor = this.repository.save(author);
            return new CustomResponse<>(
                    savedAuthor,
                    "El autor '" + savedAuthor.getName() + "' se ha registrado con éxito.",
                    false,
                    201 // Código HTTP para "Creado"
            );
        } catch (DataAccessException e) {
            return new CustomResponse<>(
                    null,
                    "Error al acceder a la base de datos al intentar registrar el autor.",
                    true,
                    500
            );
        } catch (Exception e) {
            return new CustomResponse<>(
                    null,
                    "Ocurrió un error inesperado al intentar registrar el autor.",
                    true,
                    500
            );
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Author> updateAuthor(Long id, Author author) {
        // Validar si el autor existe
        Optional<Author> existingAuthorOpt = this.repository.findById(id);
        if (existingAuthorOpt.isEmpty()) {
            return new CustomResponse<>(null, "El autor no existe.", true, 404);
        }

        // Validar duplicados, excluyendo el autor actual
        Optional<Author> duplicateAuthorOpt = this.repository.findByName(author.getName());
        if (duplicateAuthorOpt.isPresent() && !duplicateAuthorOpt.get().getId().equals(id)) {
            return new CustomResponse<>(null, "Ya existe un autor con ese nombre.", true, 409);
        }

        try {
            // Actualizar solo los campos necesarios
            Author existingAuthor = existingAuthorOpt.get();
            existingAuthor.setName(author.getName());
            //existingAuthor.setBirthDate(author.getBirthDate());
            existingAuthor.setStatus(author.getStatus());

            // Guardar los cambios
            Author savedAuthor = this.repository.saveAndFlush(existingAuthor);
            return new CustomResponse<>(savedAuthor, "El autor se ha actualizado exitosamente.", false, 200);
        } catch (DataAccessException e) {
            return new CustomResponse<>(null, "Error de base de datos al actualizar el autor.", true, 500);
        } catch (Exception e) {
            return new CustomResponse<>(null, "Ocurrió un error inesperado.", true, 500);
        }
    }

    @Transactional
    public CustomResponse<Author> changeStatusById(Long id) {
        try {
            return this.repository.findById(id)
                    .map(author -> {
                        boolean newStatus = !author.getStatus();
                        author.setStatus(newStatus);
                        Author savedAuthor = this.repository.saveAndFlush(author);
                        String statusMessage = newStatus ? "activado" : "desactivado";
                        return new CustomResponse<>(savedAuthor, "El autor ha sido " + statusMessage + " exitosamente.", false, 200);
                    })
                    .orElseGet(() -> new CustomResponse<>(null, "El autor no existe.", true, 404));
        } catch (DataAccessException e) {
            return new CustomResponse<>(null, "Error al acceder a la base de datos.", true, 500);
        } catch (Exception e) {
            return new CustomResponse<>(null, "Ocurrió un error inesperado.", true, 500);
        }
    }
}
