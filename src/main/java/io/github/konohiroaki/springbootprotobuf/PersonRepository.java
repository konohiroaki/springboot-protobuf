package io.github.konohiroaki.springbootprotobuf;

import java.util.Map;

import io.github.konohiroaki.sbp.PersonProtos.Person;

public class PersonRepository {

    Map<Integer, Person> persons;

    public PersonRepository(Map<Integer, Person> persons) {
        this.persons = persons;
    }

    public Person getPerson(int id) {
        return persons.get(id);
    }

}
