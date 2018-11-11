package io.github.konohiroaki.springbootprotobuf;

import java.util.Map;

import io.github.konohiroaki.sbp.Person;

public class PersonRepository {

    Map<Person.PersonId, Person> persons;

    public PersonRepository(Map<Person.PersonId, Person> persons) {
        this.persons = persons;
    }

    public Person getPerson(Person.PersonId id) {
        return persons.get(id);
    }

}
