package io.github.konohiroaki.springbootprotobuf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.konohiroaki.sbp.PersonProtos.Person;

@RestController
public class PersonController {

    private PersonRepository personRepo;

    public PersonController(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/persons/{id}")
    public Person person(@PathVariable int id) {
        return personRepo.getPerson(id);
    }
}
