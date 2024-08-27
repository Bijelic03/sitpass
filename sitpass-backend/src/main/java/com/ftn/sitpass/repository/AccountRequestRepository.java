package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

    Optional<AccountRequest> findByEmail(String email);

}