package com.embl.ebi.service;

import com.embl.ebi.model.Person;
import com.embl.ebi.repository.PersonRepository;
import com.embl.ebi.util.BadDataException;
import com.embl.ebi.util.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    /**
     * Gets all persons present in registry
     * @return
     */
    public List<Person> getAllPersons() {
        log.info("Got a request to get all persons");
        return repository.findAll();
    }

    /**
     * Adds person to registry
     * @param person person input to add
     * @return
     * @throws BadDataException when there exists already a person with same names
     */
    public Person addPerson(Person person) throws BadDataException {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("firstName").is(person.getFirstName()));
        criteria.add(Criteria.where("lastName").is(person.getLastName()));
        if(repository.getAllMatchingPersons(criteria).size() > 0) {
            BadDataException exception = new BadDataException("Person not found");
            exception.setErrorCode("DUPLICATE_PERSON_DATA");
            exception.setResource("person");
            throw exception;
        }
        person.setId(String.valueOf(getRepositoryCount()));
        return repository.insert(person);
    }

    private synchronized long getRepositoryCount() {
        return repository.count();
    }

    /**
     * Deletes the person from registry
     * @param id id of the person to delete
     * @throws PersonNotFoundException If no person exists with the id
     */
    public void deletePerson(String id) throws PersonNotFoundException {
        if(repository.existsById(id))
            repository.deleteById(id);
        else {
            PersonNotFoundException exception = new PersonNotFoundException("Person not found");
            exception.setErrorCode("DELETE_INVALID_ID");
            exception.setResource("person");
            throw exception;
        }
    }

    /**
     * Retrieves person details based on id
     * @param id id of the person to retrieve
     * @return
     * @throws PersonNotFoundException If no person exists with the id
     */
    public Person getPersonDetails(String id) throws PersonNotFoundException {
        Optional<Person> person =  repository.findById(id);
        if(person.isPresent())
            return person.get();
        else {
            PersonNotFoundException exception = new PersonNotFoundException("Person not found");
            exception.setErrorCode("GET_INVALID_ID");
            exception.setResource("person");
            throw exception;
        }
    }


    /**
     * Updates person object based on id and person details passed in input
     * @param id id of the person to update
     * @param person person details to update
     * @return
     * @throws BadDataException If there exists already a person with different id with same data
     * @throws PersonNotFoundException If no person exists with the id passed through input
     */
    public Person updatePerson(String id, Person person) throws BadDataException, PersonNotFoundException {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("firstName").is(person.getFirstName()));
        criteria.add(Criteria.where("lastName").is(person.getLastName()));
        if(repository.existsById(id)) {
            List<Person> existingPersons = repository.getAllMatchingPersons(criteria);
            if(existingPersons.get(0).getId().equals(id)) {
                person.setId(id);
                return repository.save(person);
            } else {
                BadDataException exception = new BadDataException("Person not found");
                exception.setErrorCode("DUPLICATE_PERSON_DATA");
                exception.setResource("person");
                throw exception;
            }
        } else {
            PersonNotFoundException exception = new PersonNotFoundException("Person not found");
            exception.setErrorCode("UPDATE_INVALID_ID");
            exception.setResource("person");
            throw exception;
        }
    }
}
