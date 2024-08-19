package org.sebastian.liceoyarah.ms.persons.services.impls;

import org.sebastian.liceoyarah.ms.persons.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.persons.entities.Person;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.liceoyarah.ms.persons.services.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PersonServiceImpl implements PersonService {
    @Override
    public ResponseWrapper<Person> create(CreatePersonDto person) {
        return null;
    }

    @Override
    public Page<Person> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<Person> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<Person> update(Long id, UpdatePersonDto person) {
        return null;
    }

    @Override
    public ResponseWrapper<Person> delete(Long id) {
        return null;
    }

    @Override
    public List<Long> findPersonIdsByCriteria(String search) {
        return List.of();
    }
}
