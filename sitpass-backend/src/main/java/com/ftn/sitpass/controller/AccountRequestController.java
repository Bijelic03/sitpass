package com.ftn.sitpass.controller;

import com.ftn.sitpass.model.AccountRequest;
import com.ftn.sitpass.service.AccountRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-requests")
public class AccountRequestController {

    private final AccountRequestService accountRequestService;

    @GetMapping
    public ResponseEntity<List<AccountRequest>> getAllAccountRequests() {
        return ResponseEntity.ok(accountRequestService.getAllAccountRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountRequest> getAccountRequestById(@PathVariable Long id) {
    return ResponseEntity.ok(accountRequestService.getAccountRequestById(id));
    }

    @PostMapping
    public ResponseEntity<AccountRequest> createAccountRequest(@RequestParam AccountRequest accountRequest) {
        return ResponseEntity.ok(accountRequestService.createAccountRequest(accountRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountRequest> acceptAccountRequest(@PathVariable Long id) {
        return ResponseEntity.ok(accountRequestService.acceptAccountRequest(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> rejectAccountRequest(@PathVariable Long id,
                                                     @RequestParam String reason) {
        accountRequestService.rejectAccountRequest(id, reason);
        return ResponseEntity.noContent().build();
    }

}