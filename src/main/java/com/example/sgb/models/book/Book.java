package com.example.sgb.models.book;

import com.example.sgb.models.author.Author;
import com.example.sgb.models.categories.Category;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

   @Column(nullable = false, length = 250)
    private String description;

   @Column(nullable = false)
   @Temporal(TemporalType.DATE)
   @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;

   @Column(nullable = false)
   private boolean status;

   //Relacion con author
    @ManyToOne
    @JoinColumn(name = "author_uid")
    private Author author;

    //Relacion con categorias
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Book() {
    }

    public Book(Long id, String title, String description, Date publishedDate, boolean status, Author author, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        this.status = status;
        this.author = author;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
