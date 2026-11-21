package org.example.dto;

import java.time.LocalDate;

public class LoanResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long readerId;
    private String readerName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public LoanResponse() {
    }

    public LoanResponse(Long id, Long bookId, String bookTitle, Long readerId, String readerName,
                        LocalDate loanDate, LocalDate dueDate, LocalDate returnedDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.readerId = readerId;
        this.readerName = readerName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }
}