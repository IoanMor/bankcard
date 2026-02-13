package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    Card findByNumber(String number);
    Page<Card> findCardsByOwnerId(Long id,Pageable pageable);
    boolean existsByNumber(String number);
    Set<Card> findCardsByStatus (CardStatus status);
}
