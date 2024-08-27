package com.ftn.sitpass.service.impl;

import com.ftn.sitpass.model.User;
import com.ftn.sitpass.repository.UserRepository;
import com.ftn.sitpass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserModel(Long id) {
        return userRepository.getReferenceById(id);
    }


}
