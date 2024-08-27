package com.ftn.sitpass.service.impl;

import com.ftn.sitpass.exception.BadRequestException;
import com.ftn.sitpass.exception.NotFoundException;
import com.ftn.sitpass.model.AccountRequest;
import com.ftn.sitpass.model.RequestStatus;
import com.ftn.sitpass.repository.AccountRequestRepository;
import com.ftn.sitpass.service.AccountRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {

    private final AccountRequestRepository accountRequestRepository;

    @Override
    public List<AccountRequest> getAllAccountRequests() {
        return accountRequestRepository.findAll();
    }

    @Override
    public AccountRequest getAccountRequestById(Long id) {
        return accountRequestRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Account request with id: %s not found!", id)));
    }

    @Override
    public AccountRequest createAccountRequest(AccountRequest accountRequest) {
        accountRequestRepository.findByEmail(accountRequest.getEmail()).ifPresent(request -> {
            if(request.getStatus() == RequestStatus.ACCEPTED || request.getStatus() == RequestStatus.PENDING){
                throw new BadRequestException("Request already exists in the system");
            }
        });

    return accountRequestRepository.save(accountRequest);
    }

    @Override
    public AccountRequest acceptAccountRequest(Long id) {
        AccountRequest accountRequest = getAccountRequestById(id);
        accountRequest.setStatus(RequestStatus.ACCEPTED);

        return accountRequestRepository.save(accountRequest);
    }

    @Override
    public AccountRequest rejectAccountRequest(Long id, String rejectReason) {
        AccountRequest accountRequest = getAccountRequestById(id);
        accountRequest.setStatus(RequestStatus.REJECTED);
        accountRequest.setRejectReason(rejectReason);

        return accountRequestRepository.save(accountRequest);
    }

}