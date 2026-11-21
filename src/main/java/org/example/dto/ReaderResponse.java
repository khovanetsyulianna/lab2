package org.example.dto;

public class ReaderResponse {
    private Long id;
    private String fullName;
    private String email;
    private boolean reliable;
    private int maxBooksAllowed;
    private int activeLoansCount;

    public ReaderResponse() {
    }

    public ReaderResponse(Long id, String fullName, String email, boolean reliable, int maxBooksAllowed, int activeLoansCount) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.reliable = reliable;
        this.maxBooksAllowed = maxBooksAllowed;
        this.activeLoansCount = activeLoansCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReliable() {
        return reliable;
    }

    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public int getActiveLoansCount() {
        return activeLoansCount;
    }

    public void setActiveLoansCount(int activeLoansCount) {
        this.activeLoansCount = activeLoansCount;
    }
}