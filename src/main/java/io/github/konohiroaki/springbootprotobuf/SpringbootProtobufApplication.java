package io.github.konohiroaki.springbootprotobuf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.HashMap;
import java.util.Map;

import io.github.konohiroaki.sbp.Person;

@SpringBootApplication
public class SpringbootProtobufApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootProtobufApplication.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    public PersonRepository createTestPersons() {
        Map<Person.PersonId, Person> persons = new HashMap<>();
        Person person1 = Person.newBuilder()
                .setName("Foo")
                .setId(Person.PersonId.newBuilder().setId(1).build())
                .setEmail("foo@foo.bar")
                .addPhones(Person.PhoneNumber.newBuilder()
                                   .setNumber("1234567890")
                                   .setType(Person.PhoneNumber.PhoneType.MOBILE)
                                   .build())
                .build();
        Person person2 = Person.newBuilder()
                .setName("Bar")
                .setId(Person.PersonId.newBuilder().setId(2).build())
                .setEmail("bar@foo.bar")
                .addPhones(Person.PhoneNumber.newBuilder()
                                   .setNumber("2345678901")
                                   .setType(Person.PhoneNumber.PhoneType.HOME)
                                   .build())
                .build();
        persons.put(person1.getId(), person1);
        persons.put(person2.getId(), person2);
        return new PersonRepository(persons);
    }
}
