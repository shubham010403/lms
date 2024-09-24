package com.example.lms.repository;

import com.example.lms.entities.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity,Long> {
    Optional<BorrowEntity> findByUserEntityIdAndBookEntityId(Long userId, Long bookId);
}
