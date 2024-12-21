package com.example.sgb.controllers.author;

import com.example.sgb.models.author.Author;
import com.example.sgb.services.author.AuthorService;
import com.example.sgb.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/sgb")
public class AuthorController {
    @Autowired
    private AuthorService service;

    @GetMapping("/authors")
    public ResponseEntity<CustomResponse<List<Author>>> getAllAuthors() {
        return new ResponseEntity<>(
                this.service.getAllAuthors(), HttpStatus.OK
        );
    }
    @GetMapping("/author/list")
    public String tableList(Model model) {
        CustomResponse response = this.service.getAllAuthors();
        model.addAttribute("authors", response.getData());
        return "author/tableAuthor";
    }

    @GetMapping("/author-form")
    public String authorForm(Model model) {
        return "author/addAuthor";
    }
    @GetMapping("/author/update-form/{id}")
    public String authorUpdateForm(@PathVariable Long id, Model model) {
        CustomResponse response = this.service.getAuthorById(id);
        if (!response.isError()) {
            model.addAttribute("successMessage", response.getMessage());
            model.addAttribute("author", response.getData());
        }else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "author/updateAuthor";
    }

    @PostMapping("/author/saved")
    public String savedAuthor(Author author, Model model) {
        CustomResponse<Author> response = this.service.createAuthor(author);
        if (!response.isError()) {
            // Mensaje de éxito
            model.addAttribute("successMessage", response.getMessage());
            model.addAttribute("author", new Author()); // Resetea el formulario
        } else {
            // Mensaje de error
            model.addAttribute("errorMessage", response.getMessage());
            model.addAttribute("author", author); // Retorna los datos ingresados por el usuario
        }
        return "author/addAuthor";
    }

    @PutMapping("/author/update/{id}")
    public String editForm(@PathVariable("id")Long id,Author author ,Model model) {
        CustomResponse response = this.service.updateAuthor(id, author);
        if (!response.isError()) {
            model.addAttribute("successMessage", response.getMessage());
            model.addAttribute("author", response.getData());
        }else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "author/updateAuthor";
    }
    @DeleteMapping("/author/changeStatus/{id}")
    public String changeStatus(@PathVariable Long id, Model model) {
        CustomResponse response = this.service.changeStatusById(id);
        if (!response.isError()){
            model.addAttribute("author", response.getData());
        }else {
            model.addAttribute("errorMessage", "Error al intentar cambiar el estado");
        }

        return "redirect:/sgb/author/list";
    }
}
