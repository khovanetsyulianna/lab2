
package org.example.service;

import org.example.dto.LoanRequest;
import org.example.dto.LoanResponse;

import org.example.model.Book;
import org.example.model.BookStatus;
import org.example.model.Loan;
import org.example.model.Reader;

import org.example.repository.BookRepository;
import org.example.repository.LoanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private static final int LOAN_DAYS = 14;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ReaderService readerService;

    public LoanService(
            LoanRepository loanRepository,
            BookRepository bookRepository,
            BookService bookService,
            ReaderService readerService) {

        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @Transactional
    public LoanResponse issueBook(LoanRequest request) {

        if (request == null ||
                request.getBookId() == null ||
                request.getReaderId() == null) {

            throw new IllegalArgumentException(
                    "bookId and readerId are required"
            );
        }

        Book book =
                bookService.getBookEntityById(
                        request.getBookId()
                );

        Reader reader =
                readerService.getReaderEntityById(
                        request.getReaderId()
                );

        if (book.getStatus() != BookStatus.AVAILABLE) {

            throw new IllegalStateException(
                    "Book is already issued"
            );
        }

        int activeLoansCount =
                loanRepository
                        .findByReaderIdAndReturnedDateIsNull(
                                reader.getId()
                        ).size();

        int maxAllowed =
                readerService.getAllowedBooksLimit(
                        reader.getId()
                );

        if (activeLoansCount >= maxAllowed) {

            throw new IllegalStateException(
                    "Reader reached maximum allowed books limit: "
                            + maxAllowed
            );
        }

        book.setStatus(BookStatus.ISSUED);

        bookRepository.save(book);

        LocalDate loanDate = LocalDate.now();

        LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);

        Loan loan = new Loan(
                book,
                reader,
                loanDate,
                dueDate,
                null
        );

        Loan saved = loanRepository.save(loan);

        return toResponse(saved);
    }

    @Transactional
    public LoanResponse returnBook(Long loanId) {

        Loan loan =
                loanRepository.findById(loanId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Loan not found with id: " + loanId
                                )
                        );

        if (loan.getReturnedDate() != null) {

            throw new IllegalStateException(
                    "Book has already been returned"
            );
        }

        loan.setReturnedDate(LocalDate.now());

        Book book = loan.getBook();

        book.setStatus(BookStatus.AVAILABLE);

        bookRepository.save(book);

        Loan saved = loanRepository.save(loan);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> getAllLoans() {

        List<Loan> loans =
                loanRepository.findAll();

        List<LoanResponse> result =
                new ArrayList<>();

        for (Loan loan : loans) {
            result.add(toResponse(loan));
        }

        return result;
    }

    private LoanResponse toResponse(Loan loan) {

        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getReader().getId(),
                loan.getReader().getFullName(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }
}
