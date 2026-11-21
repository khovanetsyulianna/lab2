
package org.example.controller;

import org.example.dto.LoanRequest;
import org.example.dto.LoanResponse;
import org.example.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> issueBook(
            @RequestBody LoanRequest request) {

        LoanResponse response = loanService.issueBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{loanId}/return")
    public ResponseEntity<LoanResponse> returnBook(
            @PathVariable Long loanId) {

        return ResponseEntity.ok(
                loanService.returnBook(loanId)
        );
    }

    @GetMapping
    public List<LoanResponse> getAllLoans() {
        return loanService.getAllLoans();
    }
}
