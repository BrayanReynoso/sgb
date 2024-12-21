package com.example.sgb.controllers.book;

import com.example.sgb.models.book.Book;
import com.example.sgb.repositories.book.BookRepository;
import com.example.sgb.services.author.AuthorService;
import com.example.sgb.services.book.BookService;
import com.example.sgb.services.category.CategoryService;
import com.example.sgb.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sgb")
@CrossOrigin("*")
public class BookController {
    @Autowired
    private BookRepository repository;
    @Autowired
    private BookService service;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String  listBooks(Model model) {
        CustomResponse response = this.service.getAllByStatusActive();
        model.addAttribute("books", response.getData());
        return "index";
    }
    @GetMapping("/admin")
    public String adminList(Model model) {
        CustomResponse response = this.service.getAll();
        model.addAttribute("books", response.getData());
        return "book/tableBooks";
    }
    @GetMapping("/addBook")
    public String addBook(Model model) {
        CustomResponse response = this.authorService.getAllAuthors();
        CustomResponse categoryResponse = this.categoryService.getAll();

        model.addAttribute("book", new Book());
        model.addAttribute("authors", response.getData());
        model.addAttribute("categories", categoryResponse.getData());
        return "book/addBook";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        CustomResponse<Book> response = service.getById(id);

        if (response.isError()) {
            model.addAttribute("errorMessage", response.getMessage());
            return "redirect:/sgb/admin";
        }

        // Cargar datos necesarios cuando no haya error
        model.addAttribute("book", response.getData());
        model.addAttribute("authors", authorService.getAllAuthors().getData());
        model.addAttribute("categories", categoryService.getAll().getData());

        return "/book/updateBook";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        CustomResponse response = this.service.changeStatusById(id);

        if(!response.isError()){
            model.addAttribute("message", "El status del libro se ha cambiado con exito");
            model.addAttribute("books", service.getAll());
        }else {
            model.addAttribute("error", "Error al cambiar el status del libro");
        }
        return "redirect:/sgb/admin";
    }

    @PostMapping("/register")
    public String registerBook(@ModelAttribute("book") Book book, Model model) {
        CustomResponse response = this.service.create(book);

        if (!response.isError()) {
            // Agregar mensaje de éxito y limpiar el formulario
            model.addAttribute("successMessage", "Libro registrado con éxito");
            model.addAttribute("book", new Book()); // Reinicia el formulario con un nuevo objeto vacío
        } else {
            // Agregar mensaje de error y mantener los datos actuales del formulario
            model.addAttribute("errorMessage", "Error al registrar el libro: " + response.getMessage());
        }

        // Siempre cargar nuevamente la vista del formulario
        return "book/addBook";
    }

    @PutMapping("/update/{id}")
    public String updateBook(@PathVariable long id, @ModelAttribute Book book, Model model) {

        CustomResponse<Book> response = service.updateBook(id, book);

        if (!response.isError()) {
            model.addAttribute("message", "Libro actualizado correctamente.");
        } else {
            model.addAttribute("error", response.getMessage());
        }

        // Redirige a la página principal de administración
        return "redirect:/sgb/admin";
    }

    @DeleteMapping("/book/changeStatus/{id}")
    public String deleteBook(@PathVariable long id, Model model) {
        CustomResponse<Book> response = service.changeStatusById(id);

        if(!response.isError()){
            model.addAttribute("successMessage", "Status changed successfully");
            model.addAttribute("book", response.getData());
        }else {
            model.addAttribute("errorMessage", "Category status error");
        }
        return "redirect:/sgb/admin";
    }
}
