
package org.example.service;

import org.example.dto.ReaderRequest;
import org.example.dto.ReaderResponse;
import org.example.dto.LoanResponse;

import org.example.model.Loan;
import org.example.model.Reader;

import org.example.repository.LoanRepository;
import org.example.repository.ReaderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final LoanRepository loanRepository;

    public ReaderService(
            ReaderRepository readerRepository,
            LoanRepository loanRepository) {

        this.readerRepository = readerRepository;
        this.loanRepository = loanRepository;
    }

    public ReaderResponse registerReader(ReaderRequest request) {

        if (request.getFullName() == null ||
                request.getFullName().isBlank()) {

            throw new IllegalArgumentException(
                    "Full name is required"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        String normalizedEmail =
                request.getEmail().trim().toLowerCase();

        if (!normalizedEmail.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            throw new IllegalArgumentException(
                    "Invalid email format"
            );
        }

        if (readerRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(
                    "Reader with this email already exists"
            );
        }

        Reader reader = new Reader(
                request.getFullName().trim(),
                normalizedEmail
        );

        Reader saved = readerRepository.save(reader);

        return toResponse(saved);
    }

    public List<ReaderResponse> getAllReaders() {

        List<Reader> readers =
                readerRepository.findAll();

        List<ReaderResponse> result =
                new ArrayList<>();

        for (Reader reader : readers) {
            result.add(toResponse(reader));
        }

        return result;
    }

    public Reader getReaderEntityById(Long id) {

        return readerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reader not found with id: " + id
                        )
                );
    }

    public boolean isReaderReliable(Long readerId) {

        getReaderEntityById(readerId);

        LocalDate today = LocalDate.now();

        LocalDate thirtyDaysAgo =
                today.minusDays(30);

        List<Loan> overdueLoans =
                loanRepository
                        .findByReaderIdAndReturnedDateIsNullAndDueDateBefore(
                                readerId,
                                today
                        );

        if (!overdueLoans.isEmpty()) {
            return false;
        }

        List<Loan> returnedLoans =
                loanRepository
                        .findByReaderIdAndReturnedDateIsNotNull(
                                readerId
                        );

        int returnedCount = 0;

        for (Loan loan : returnedLoans) {

            LocalDate returnedDate =
                    loan.getReturnedDate();

            boolean withinLast30Days =
                    !returnedDate.isBefore(thirtyDaysAgo)
                            && !returnedDate.isAfter(today);

            if (withinLast30Days) {

                if (returnedDate.isAfter(loan.getDueDate())) {
                    return false;
                }

                returnedCount++;
            }
        }

        return returnedCount >= 3;
    }

    public int getAllowedBooksLimit(Long readerId) {

        return isReaderReliable(readerId) ? 5 : 2;
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> getActiveLoans(Long readerId) {

        getReaderEntityById(readerId);

        List<Loan> loans =
                loanRepository
                        .findByReaderIdAndReturnedDateIsNull(
                                readerId
                        );

        List<LoanResponse> result =
                new ArrayList<>();

        for (Loan loan : loans) {

            result.add(new LoanResponse(
                    loan.getId(),
                    loan.getBook().getId(),
                    loan.getBook().getTitle(),
                    loan.getReader().getId(),
                    loan.getReader().getFullName(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnedDate()
            ));
        }

        return result;
    }

    public ReaderResponse toResponse(Reader reader) {

        boolean reliable =
                isReaderReliable(reader.getId());

        int maxBooks = reliable ? 5 : 2;

        int activeLoans =
                loanRepository
                        .findByReaderIdAndReturnedDateIsNull(
                                reader.getId()
                        ).size();

        return new ReaderResponse(
                reader.getId(),
                reader.getFullName(),
                reader.getEmail(),
                reliable,
                maxBooks,
                activeLoans
        );
    }
}
