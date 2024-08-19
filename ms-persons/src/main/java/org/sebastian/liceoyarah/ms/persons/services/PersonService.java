package org.sebastian.liceoyarah.ms.persons.services;

import org.sebastian.liceoyarah.ms.persons.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.persons.entities.Person;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.update.UpdatePersonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {

    ResponseWrapper<Person> create(CreatePersonDto person);
    Page<Person> findAll(String search, Pageable pageable);
    ResponseWrapper<Person> findById(Long id);
    ResponseWrapper<Person> update(Long id, UpdatePersonDto person);
    ResponseWrapper<Person> delete(Long id);
    List<Long> findPersonIdsByCriteria(String search);

}
