package org.example.repository;

import org.example.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderId(Long readerId);
    List<Loan> findByReaderIdAndReturnedDateIsNull(Long readerId);
    List<Loan> findByReaderIdAndReturnedDateIsNotNull(Long readerId);
    List<Loan> findByReaderIdAndReturnedDateIsNullAndDueDateBefore(Long readerId, LocalDate currentDate);
}
