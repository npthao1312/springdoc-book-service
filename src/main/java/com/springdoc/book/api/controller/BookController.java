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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookController {
	@Autowired
	BookRepository bookRepository;
	
	@Operation(summary = "Add a new book", description = "Add a new book", tags = { "book" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = Book.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }),
			@ApiResponse(responseCode = "405", description = "Invalid input")
	})
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
	
	@Operation(summary = "Get all Books", description = "Get all Books", tags = { "book" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))
	})
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
	
	@Operation(summary = "Find book by ID", description = "Returns a single book", tags = { "book" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Book.class))),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
	    Optional<Book> bookData = bookRepository.findById(id);
	    if (bookData.isPresent()) {
	      return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@Operation(summary = "Update an existing book", description = "Update an existing book by Id", tags = { "book" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful operation",
					content =
							{ @Content(mediaType = "application/xml", schema = @Schema(implementation = Book.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }
			),
			@ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Book not found"),
			@ApiResponse(responseCode = "405", description = "Validation exception") })
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

	@Operation(summary = "Deletes a book", description = "Deletes a book by Id", tags = { "book" })
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid book value") })
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
