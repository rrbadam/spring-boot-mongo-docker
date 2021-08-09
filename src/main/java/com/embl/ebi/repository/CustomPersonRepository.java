package com.embl.ebi.repository;

import com.embl.ebi.model.Person;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface CustomPersonRepository {
    List<Person> getAllMatchingPersons(List<Criteria> criteria);
}
