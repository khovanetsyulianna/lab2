package org.example.service;

import org.example.dto.BookRequest;
import org.example.dto.BookResponse;
import org.example.model.Book;
import org.example.model.BookStatus;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse addBook(BookRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (request.getAuthor() == null || request.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book author is required");
        }

        Book book = new Book(request.getTitle().trim(), request.getAuthor().trim(), BookStatus.AVAILABLE);
        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> result = new ArrayList<>();
        for (Book book : books) {
            result.add(toResponse(book));
        }
        return result;
    }

    public Book getBookEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + id));
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getStatus());
    }
}