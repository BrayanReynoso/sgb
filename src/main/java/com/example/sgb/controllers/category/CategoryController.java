package com.example.sgb.controllers.category;

import com.example.sgb.models.categories.Category;
import com.example.sgb.services.category.CategoryService;
import com.example.sgb.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sgb")
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/category/list")
    public String listCategory(Model model){
        CustomResponse response = this.service.getAll();
        model.addAttribute("categories", response.getData());
        return "category/tableCategory";
    }

    @GetMapping("/category/register")
    public String registerCategory(Model model){
        model.addAttribute("category", new Category());
        return "category/addCategoryForm";
    }

    @GetMapping("/category/update/{id}")
    public String updateCategoryHTML(@PathVariable("id") Long id,Model model){
        CustomResponse response = this.service.getCategoryById(id);

        if (!response.isError()){
            model.addAttribute("category", response.getData());
        }else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "category/updateCategory";
    }

    @PostMapping("/registerCategory")
    public String registerCategory(@ModelAttribute("category") Category category, Model model) {
        CustomResponse<Category> response = this.service.create(category);

        if (response.isError()) {
            // Si hay un error, muestra un mensaje en el modelo
            model.addAttribute("errorMessage", response.getMessage());
        } else {
            // Si el registro es exitoso, muestra un mensaje de éxito
            model.addAttribute("successMessage", "Categoría registrada exitosamente.");
            // Limpia el formulario para permitir el registro de una nueva categoría
            model.addAttribute("category", new Category());
        }

        // Vuelve a cargar la vista del formulario
        return "category/addCategoryForm";
    }

    @PutMapping("/category/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, Category category, Model model) {
        CustomResponse<Category> response = this.service.update(id, category);
        if (!response.isError()) {
            model.addAttribute("successMessage", response.getMessage());
            model.addAttribute("category", response.getData());
        }else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "category/updateCategory";
    }

    @DeleteMapping("/category/changeStatus/{id}")
    public String changeStatus(@PathVariable("id") Long id, Model model){
        CustomResponse response = this.service.changeStatusById(id);

        if(!response.isError()){
            model.addAttribute("message", "Status changed successfully");
            model.addAttribute("categories", response.getData());
        }else {
            model.addAttribute("error", "Category status error");
        }
        return "redirect:/sgb/category/list";
    }
}
