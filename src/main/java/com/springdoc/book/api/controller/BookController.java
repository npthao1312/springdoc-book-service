package com.springdoc.book.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springdoc.book.api.model.Book;
import com.springdoc.book.api.repository.BookRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookController {
	@Autowired
	BookRepository bookRepository;
	
	@PostMapping("/")
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
	    try {
	    	Book _book = bookRepository
	          .save(new Book(book.getTitle(), book.getAuthor(), book.getDescription()));
	      return new ResponseEntity<>(_book, HttpStatus.CREATED);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Book>> getAllBooks() {
	    try {
	      List<Book> books = new ArrayList<Book>();
	      bookRepository.findAll().forEach(books::add);
	      
	      if (books.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }

	      return new ResponseEntity<>(books, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	  @GetMapping("/{id}")
	  public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
	    Optional<Book> bookData = bookRepository.findById(id);

	    if (bookData.isPresent()) {
	      return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }
	  
	  @PutMapping("/{id}")
	  public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
	    Optional<Book> bookData = bookRepository.findById(id);

	    if (bookData.isPresent()) {
	      Book _book = bookData.get();
	      _book.setTitle(book.getTitle());
	      _book.setAuthor(book.getAuthor());
	      _book.setDescription(book.getDescription());	      
	      return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/{id}")
	  public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id) {
	    try {
	      bookRepository.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
}
