package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.entity.Book;
import com.rookies3.myspringbootlab.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("")
    public String redirectToIndex() {
        // "redirect:" 접두사를 붙여 리다이렉트할 경로를 반환합니다.
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String showAddForm(@ModelAttribute("bookForm") Book book) {
        return "add-book";
    }

    @PostMapping("/addbook")
    public String addUser(@Valid @ModelAttribute("bookForm") Book book,
                          Errors result, Model model) {
        if (result.hasErrors()) {
            return "add-book";
        }
        bookRepository.save(book);
        model.addAttribute("books", bookRepository.findAll());
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id,
                                 Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "update-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "update-book";
        }
        bookRepository.save(book);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 id: " + id));
        bookRepository.delete(book);
        return "redirect:/index";
    }
}