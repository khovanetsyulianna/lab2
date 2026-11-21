package org.example.repository;

import org.example.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    boolean existsByEmail(String email);
    Optional<Reader> findByEmail(String email);
}