package com.example.sgb.services.category;

import com.example.sgb.models.author.Author;
import com.example.sgb.models.categories.Category;
import com.example.sgb.repositories.category.CategoryRepository;
import com.example.sgb.utils.CustomResponse;
import jdk.jfr.TransitionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public CustomResponse<List<Category>> getAll() {
        List<Category> exist = this.repository.findAll();
        if (!exist.isEmpty()){
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
    public CustomResponse<Category> getCategoryById(Long id) {
        try {
            return this.repository.findById(id)
                    .map(category -> new CustomResponse<>(category, "Categoría encontrada.", false, 200))
                    .orElseGet(() -> new CustomResponse<>(null, "Categoría no encontrada.", true, 404));
        } catch (DataAccessException e) {
            return new CustomResponse<>(null, "Ocurrió un error al buscar la categoría.", true, 500);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Category> create(Category category) {
        if (this.repository.existsByName(category.getName())) {
            return new CustomResponse<>(
                    null,
                    "La categoría ya existe, intente con otro nombre.",
                    true,
                    400
            );
        }

        try {
            Category savedCategory = this.repository.save(category);
            return new CustomResponse<>(savedCategory, "Categoría registrada con éxito.", false, 200);
        } catch (Exception e) {
            return new CustomResponse<>(null, "Ocurrió un error al registrar la categoría.", true, 500);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Category> update(Long categoryId, Category category) {
        Optional<Category> existingCategoryOpt = this.repository.findById(categoryId);

        if (existingCategoryOpt.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    "La categoría no se ha encontrado.",
                    true,
                    404
            );
        }

        // Validar que el nombre no sea duplicado
        Optional<Category> duplicateCategoryOpt = this.repository.findByName(category.getName());
        if (duplicateCategoryOpt.isPresent() && !duplicateCategoryOpt.get().getId().equals(categoryId)) {
            return new CustomResponse<>(
                    null,
                    "Ya existe una categoría con ese nombre.",
                    true,
                    409 // Código HTTP para conflictos
            );
        }

        try {
            Category existingCategory = existingCategoryOpt.get();
            // Actualizamos solo los campos necesarios
            existingCategory.setName(category.getName());
            existingCategory.setStatus(category.getStatus());
            // Puedes agregar más campos según sea necesario

            Category updatedCategory = this.repository.saveAndFlush(existingCategory);
            return new CustomResponse<>(updatedCategory, "Categoría actualizada con éxito.", false, 200);
        } catch (DataAccessException e) {
            // Excepción específica de la base de datos
            return new CustomResponse<>(null, "Error de base de datos al actualizar la categoría.", true, 500);
        } catch (Exception e) {
            // Manejo genérico
            return new CustomResponse<>(null, "Ocurrió un error inesperado.", true, 500);
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Category> changeStatusById(Long id) {
        Optional<Category> exist = this.repository.findById(id);
        if (exist.isPresent()){
            exist.get().setStatus(!exist.get().getStatus());
            return new CustomResponse<>(
                    this.repository.save(exist.get()),
                    "OK",
                    false,
                    200
            );
        }
        return new CustomResponse<>(
                null,
                "Category not found",
                true,
                400
        );
    }

}
