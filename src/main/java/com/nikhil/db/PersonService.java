package com.nikhil.db;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

    /**
     * @param personId
     * @return {@link Optional} {@link Person} objects if present in database for
     *         supplied person ID
     */
    public Optional<PersonEntity> getPersonById(int personId);

    /**
     * @return {@link List} of {@link Person} model class fo rall available entities
     */
    public List<PersonEntity> getAllPersons();

    /**
     * @param personId
     * @return Delete the person from database for supplied id
     */
    public boolean removePerson(int personId);

    /**
     * @param person
     * @return {@link Optional} {@link Person} objects after save or update Save
     *         if no personId present else update
     */
    public Optional<PersonEntity> saveUpdatePerson(PersonEntity person);
}