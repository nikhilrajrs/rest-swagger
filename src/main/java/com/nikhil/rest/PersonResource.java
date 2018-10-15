package com.nikhil.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.rest.db.PersonEntity;
import com.nikhil.rest.db.PersonService;

@RestController
@RequestMapping("/person")
public class PersonResource {

    private final PersonService personService;

    /**
     * Constructor to autowire PersonService instance. Look we have declared
     * personService as final without initialization
     */
    @Autowired
    PersonResource(PersonService personService) {
        this.personService = personService;
    }

    /**
     * 
     * @return expose GET endpoint to return {@link List} of all available persons
     */
    @GetMapping
    public List<PersonEntity> getAllPerson() {
        return personService.getAllPersons();
    }

    /**
     * 
     * @param personId supplied as path variable
     * @return expose GET endpoint to return {@link Person} for the supplied person
     *         id return HTTP 404 in case person is not found in database
     */
    @GetMapping(value = "/{personId}")
    public ResponseEntity<PersonEntity> getPerson(@PathVariable("personId") int personId) {
        return personService.getPersonById(personId).map(person -> {
            return ResponseEntity.ok(person);
        }).orElseGet(() -> {
            return new ResponseEntity<PersonEntity>(HttpStatus.NOT_FOUND);
        });
    }

    /**
     * 
     * @param person JSON body
     * @return expose POST mapping and return newly created person in case of
     *         successful operation return HTTP 417 in case of failure
     */
    @PostMapping
    public ResponseEntity<PersonEntity> addNewPerson(@RequestBody PersonEntity person) {
        return personService.saveUpdatePerson(person).map(p -> {
            return ResponseEntity.ok(p);
        }).orElseGet(() -> {
            return new ResponseEntity<PersonEntity>(HttpStatus.EXPECTATION_FAILED);
        });
    }

    /**
     * 
     * @param person JSON body
     * @return expose PUT mapping and return newly created or updated person in case
     *         of successful operation return HTTP 417 in case of failure
     * 
     */
    @PutMapping
    public ResponseEntity<PersonEntity> updatePerson(@RequestBody PersonEntity person) {
        return personService.saveUpdatePerson(person).map(p -> {
            return ResponseEntity.ok(p);
        }).orElseGet(() -> {
            return new ResponseEntity<PersonEntity>(HttpStatus.EXPECTATION_FAILED);
        });
    }

    /**
     * 
     * @param personId person id to be deleted
     * @return expose DELETE mapping and return success message if operation was
     *         successful. return HTTP 417 in case of failure
     * 
     */
    @DeleteMapping(value = "/{personId}")
    public ResponseEntity<String> deletePerson(@PathVariable("personId") int personId) {
        if (personService.removePerson(personId)) {
            return ResponseEntity.ok("Person with id : " + personId + " removed");
        } else {
            return new ResponseEntity<String>("Error deleting enitty ", HttpStatus.EXPECTATION_FAILED);
        }
    }
}