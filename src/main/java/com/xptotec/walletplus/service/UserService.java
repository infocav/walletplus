package com.xptotec.walletplus.service;

import com.xptotec.walletplus.dto.UserRequest;
import com.xptotec.walletplus.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserRequest user);
    User getUserByCpf(String cpf);
    List<User> getAllUsers();
    User updateUser(String cpf, UserRequest user);
    void deleteUser(String cpf);
}