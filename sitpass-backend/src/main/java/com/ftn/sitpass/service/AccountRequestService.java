package com.ftn.sitpass.service;

import com.ftn.sitpass.model.AccountRequest;

import java.util.List;

public interface AccountRequestService {

    List<AccountRequest> getAllAccountRequests();

    AccountRequest getAccountRequestById(Long id);

    AccountRequest createAccountRequest(AccountRequest accountRequest);

    AccountRequest acceptAccountRequest(Long id);

    AccountRequest rejectAccountRequest(Long id, String rejectReason);
}