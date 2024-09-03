package org.sebastian.liceoyarah.ms.users.services;

import org.sebastian.liceoyarah.ms.users.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.users.entities.User;
import org.sebastian.liceoyarah.ms.users.entities.dtos.create.CreateUserDto;
import org.sebastian.liceoyarah.ms.users.entities.dtos.update.UpdateUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    ResponseWrapper<User> create(CreateUserDto sportsMan);
    Page<User> findAll(String search, Pageable pageable);
    ResponseWrapper<User> findById(Long id);
    ResponseWrapper<User> findByNumberDocument(Long documentNumber);
    ResponseWrapper<User> update(Long id, UpdateUserDto sportsMan);
    ResponseWrapper<User> delete(Long id);

}
