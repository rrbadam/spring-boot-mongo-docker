package com.embl.ebi.controller;

import com.embl.ebi.model.Person;
import com.embl.ebi.service.PersonService;
import com.embl.ebi.util.PersonNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/persons")
@Api(value="Person Registry")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @ApiOperation("Lists all the persons available in registry")
    public List<Person> getAllPersons() {
        return service.getAllPersons();
    }

    @PostMapping()
    @ApiOperation("Adds a new person to the registry")
    public Person addPerson(@RequestBody @Valid Person person) {
        return service.addPerson(person);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a person existing in registry based on id")
    public void deletePerson(@PathVariable String id) throws PersonNotFoundException {
        service.deletePerson(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieves person details existing in registry based on id")
    public Person getPersonDetails(@PathVariable String id) throws PersonNotFoundException {
        return service.getPersonDetails(id);
    }


    @PutMapping("/{id}")
    @ApiOperation("Updates person existing in registry based on id")
    public Person updatePerson(@PathVariable String id, @RequestBody @Valid Person person) throws PersonNotFoundException {
        return service.updatePerson(id, person);
    }
}
