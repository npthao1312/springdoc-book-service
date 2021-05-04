package com.springdoc.book.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springdoc.book.api.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
	List<Book> findByTitle(String title);
	List<Book> findByAuthor(String author);
}
