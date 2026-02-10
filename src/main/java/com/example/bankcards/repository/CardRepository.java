package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    Card findByNumber(String number);
    Set<Card> findCardsByOwnerId(Long userId);
    boolean existsByNumber(String number);
    Set<Card> findCardsByStatus (CardStatus status);
}
