package io.github.konohiroaki.springbootprotobuf;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.konohiroaki.sbp.PersonProtos.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringbootProtobufApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void callPerson1() {
        ResponseEntity<Person> person =
                restTemplate.getForEntity("/persons/1", Person.class);
        System.out.println(person.toString());
        Assert.assertTrue(person.getStatusCode().is2xxSuccessful());
        Assert.assertEquals("Foo", person.getBody().getName());
    }

    @Test
    public void callPerson2() {
        ResponseEntity<Person> person =
                restTemplate.getForEntity("/persons/2", Person.class);
        System.out.println(person.toString());
        Assert.assertTrue(person.getStatusCode().is2xxSuccessful());
        Assert.assertEquals("Bar", person.getBody().getName());
    }
}
