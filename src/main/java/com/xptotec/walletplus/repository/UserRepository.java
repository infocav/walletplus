package com.xptotec.walletplus.repository;

import com.xptotec.walletplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    User findByName(String name);
    User findByEmail(String email);
    User findByNameOrEmail(String name, String email);
    Optional<User> findByCpf(String cpf);
    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}