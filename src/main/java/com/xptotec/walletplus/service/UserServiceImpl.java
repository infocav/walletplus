package com.xptotec.walletplus.service;

import com.xptotec.walletplus.dto.UserRequest;
import com.xptotec.walletplus.exception.UserNotFoundException;
import com.xptotec.walletplus.model.User;
import com.xptotec.walletplus.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional

    public User createUser(UserRequest userRequest) {
        User user = new User().builder()
                .cpf(userRequest.getCpf())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByCpf(String cpf) {
        return userRepository.findByCpf(cpf).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String cpf, UserRequest user) {
        User existingUser = getUserByCpf(cpf);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.saveAndFlush(existingUser);
    }

    @Override
    public void deleteUser(String cpf) {
        userRepository.deleteByCpf(cpf);
    }
}