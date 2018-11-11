package io.github.konohiroaki.springbootprotobuf;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import io.github.konohiroaki.sbp.Person;
import io.github.konohiroaki.sbp.PersonServiceGrpc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootProtobufApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonServiceTest extends GrpcServerTestBase {

    @Test
    public void getPersonViaBlockingStub() {
        Person person = PersonServiceGrpc.newBlockingStub(channel)
                .getPerson(Person.PersonId.newBuilder().setId(1).build());
        System.out.println(person);
    }

    @Test
    public void getPersonViaFutureStub() throws ExecutionException, InterruptedException {
        ListenableFuture<Person> personFuture = PersonServiceGrpc.newFutureStub(channel)
                .getPerson(Person.PersonId.newBuilder().setId(1).build());
        System.out.println(personFuture.get());
    }
}