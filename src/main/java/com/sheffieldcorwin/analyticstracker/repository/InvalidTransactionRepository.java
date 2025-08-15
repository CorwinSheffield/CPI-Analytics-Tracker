package com.sheffieldcorwin.analyticstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sheffieldcorwin.analyticstracker.entity.InvalidTransaction;

public interface InvalidTransactionRepository extends JpaRepository<InvalidTransaction, Long> {

}
