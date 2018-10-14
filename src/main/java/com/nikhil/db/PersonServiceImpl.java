package com.nikhil.db;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepo;

    @Autowired
    PersonServiceImpl(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }
    
    /**
     * Convert {@link PersonEntity} Object to {@link PersonEntity} object Set the
     * 
     * personId if present else return object with id null/0
     * 
     */
    private final Function<PersonEntity, PersonEntity> personToEntity = new Function<PersonEntity, PersonEntity>() {
        @Override
        public PersonEntity apply(PersonEntity person) {
            if (person.getPersonId() == 0) {
                return new PersonEntity(person.getFirstName(), person.getLastName(), person.getEmail());
            } else {
                return new PersonEntity(person.getPersonId(), person.getFirstName(), person.getLastName(),
                        person.getEmail());
            }
        }
    };
    /**
     * 
     * Convert {@link PersonEntity} to {@link PersonEntity} object
     * 
     */
    private final Function<PersonEntity, PersonEntity> entityToPerson = new Function<PersonEntity, PersonEntity>() {
        @Override
        public PersonEntity apply(PersonEntity entity) {
            return new PersonEntity(entity.getPersonId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
        }
    };
    /**
     * 
     * If record is present then convert the record else return the empty
     * {@link Optional}
     * 
     */
    @Override
    public Optional<PersonEntity> getPersonById(int personId) {
        return personRepo.findById(personId).map(s -> entityToPerson.apply(s));
    }
    @Override
    public List<PersonEntity> getAllPersons() {
        return personRepo.findAll().parallelStream()
                .map(s -> entityToPerson.apply(s))
                .collect(Collectors.toList());
    }
    @Override
    public boolean removePerson(int personId) {
        personRepo.deleteById(personId);
        return true;
    }
    @Override
    public Optional<PersonEntity> saveUpdatePerson(PersonEntity person) {
        if (person.getPersonId() == 0 || personRepo.existsById(person.getPersonId())) {
            PersonEntity entity = personRepo.save(personToEntity.apply(person));
            return Optional.of(entityToPerson.apply(entity));
        } else {
            return Optional.empty();
        }
    }
}
