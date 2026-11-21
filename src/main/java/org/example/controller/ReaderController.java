
package org.example.controller;

import org.example.dto.ReaderRequest;
import org.example.dto.ReaderResponse;
import org.example.dto.LoanResponse;
import org.example.service.ReaderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping
    public ResponseEntity<ReaderResponse> registerReader(
            @RequestBody ReaderRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readerService.registerReader(request));
    }

    @GetMapping
    public List<ReaderResponse> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/{readerId}/reliability")
    public Map<String, Object> checkReliability(
            @PathVariable Long readerId) {

        boolean reliable =
                readerService.isReaderReliable(readerId);

        return Map.of(
                "readerId", readerId,
                "reliable", reliable,
                "maxBooksAllowed", reliable ? 5 : 2
        );
    }

    @GetMapping("/{readerId}/loans/active")
    public List<LoanResponse> getActiveLoans(
            @PathVariable Long readerId) {

        return readerService.getActiveLoans(readerId);
    }
}
