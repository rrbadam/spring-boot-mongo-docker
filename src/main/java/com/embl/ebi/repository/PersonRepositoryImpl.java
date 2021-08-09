package com.embl.ebi.repository;

import com.embl.ebi.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepositoryImpl implements CustomPersonRepository{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PersonRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Person> getAllMatchingPersons(List<Criteria> criteria) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, Person.class);
    }
}
