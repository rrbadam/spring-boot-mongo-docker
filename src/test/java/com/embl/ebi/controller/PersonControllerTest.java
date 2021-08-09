package com.embl.ebi.controller;

import com.embl.ebi.model.Person;
import com.embl.ebi.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllPersons() throws Exception {
        Person person = new Person("firstName", "lastName", 20, "white");
        when(personRepository.findAll()).thenReturn(Collections.singletonList(person));
        this.mvc.perform(get("/persons").with(user("user1").password("user1Pass"))).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].first_name", is(person.getFirstName())));
    }

    @Test
    void getAllPersons_NotPresent() throws Exception {
        Person person = new Person("firstName", "lastName", 20, "white");
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        this.mvc.perform(get("/persons").with(user("user1").password("user1Pass"))).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAllPersons_UnauthorizedUser() throws Exception {
        this.mvc.perform(get("/persons")).andExpect(status().is(401)).andExpect(content().string("HTTP Status 401 - Full authentication is required to access this resource"+System.lineSeparator()));
    }

    @Test
    void addPerson() throws Exception {
        Person person = new Person("firstName", "lastName", 20, "white");
        when(personRepository.getAllMatchingPersons(anyList())).thenReturn(new ArrayList<>());
        this.mvc.perform(post("/persons").with(user("user1").password("password")).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(person))).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void addPerson_DuplicatePerson() throws Exception {
        Person existingPerson = new Person("firstName", "lastName", 20, "red");
        existingPerson.setId("456");
        when(personRepository.getAllMatchingPersons(anyList())).thenReturn(Arrays.asList(existingPerson));

        Person person = new Person("firstName", "lastName", 20, "white");
        this.mvc.perform(post("/persons").with(user("user1").password("password")).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(person))).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void deletePerson_PersonNotPresent() throws Exception {
        String id = "123";
        when(personRepository.existsById(eq(id))).thenReturn(Boolean.FALSE);
        this.mvc.perform(delete("/persons/" + id).with(user("user1").password("user1Pass"))).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message", is("Person not found"))).andExpect(jsonPath("errorcode", is("DELETE_INVALID_ID")));
    }

    @Test
    void deletePerson() throws Exception {
        String id = "123";
        when(personRepository.existsById(eq(id))).thenReturn(Boolean.TRUE);
        this.mvc.perform(delete("/persons/" + id).with(user("user1").password("user1Pass"))).andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void deletePerson_PersonNullId() throws Exception {
        String id = "";
        when(personRepository.existsById(Mockito.anyString())).thenThrow(new IllegalArgumentException("Cannot be null"));
        this.mvc.perform(delete("/persons/").with(user("user1").password("user1Pass"))).andDo(print()).andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()))
               ;
    }

    @Test
    void getPersonDetails_detailsPresent() throws Exception {
        String id = "123";
        Person person = new Person("firstName", "lastName", 20, "white");
        person.setId(id);
        when(personRepository.findById(eq(id))).thenReturn(Optional.of(person));
        this.mvc.perform(get("/persons/"+id).with(user("user1").password("user1Pass"))).andExpect(status().isOk()).andExpect(jsonPath("first_name", is(person.getFirstName())))
                .andExpect(jsonPath("id", is(person.getId())));
    }

    @Test
    void getPersonDetails_detailsNotPresent() throws Exception {
        String id = "123";
        when(personRepository.findById(eq(id))).thenReturn(Optional.empty());
        this.mvc.perform(get("/persons/"+id).with(user("user1").password("user1Pass"))).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("message", is("Person not found"))).andExpect(jsonPath("errorcode", is("GET_INVALID_ID")));
    }
    @Test
    void updatePerson() throws Exception {
        String id = "123";
        when(personRepository.existsById(eq(id))).thenReturn(Boolean.TRUE);
        Person existingPerson = new Person("firstName", "lastName", 20, "red");
        existingPerson.setId(id);
        when(personRepository.getAllMatchingPersons(anyList())).thenReturn(Arrays.asList(existingPerson));

        Person personToUpdate = new Person("firstName", "lastName", 20, "white");
        personToUpdate.setId(id);

        when(personRepository.save(eq(personToUpdate))).thenReturn(personToUpdate);
        this.mvc.perform(put("/persons/"+id).with(user("user1").password("password")).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(personToUpdate))).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updatePerson_duplicatePersondata() throws Exception {
        String id = "123";
        when(personRepository.existsById(eq(id))).thenReturn(Boolean.TRUE);
        Person existingPerson = new Person("firstName", "lastName", 20, "red");
        existingPerson.setId("456");
        when(personRepository.getAllMatchingPersons(anyList())).thenReturn(Arrays.asList(existingPerson));

        Person personToUpdate = new Person("firstName", "lastName", 20, "white");
        personToUpdate.setId(id);

        when(personRepository.save(eq(personToUpdate))).thenReturn(personToUpdate);
        this.mvc.perform(put("/persons/"+id).with(user("user1").password("password")).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(personToUpdate))).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
    @Test
    void updatePerson_PersonNotFound() throws Exception {
        String id = "123";
        when(personRepository.existsById(eq(id))).thenReturn(Boolean.FALSE);
        Person person = new Person("firstName", "lastName", 20, "white");
        this.mvc.perform(put("/persons/"+id).with(user("user1").password("password")).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(person))).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("message", is("Person not found"))).andExpect(jsonPath("errorcode", is("UPDATE_INVALID_ID")));

    }
}