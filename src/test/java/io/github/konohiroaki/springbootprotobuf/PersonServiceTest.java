package io.github.konohiroaki.springbootprotobuf;

import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import io.github.konohiroaki.sbp.Empty;
import io.github.konohiroaki.sbp.Existence;
import io.github.konohiroaki.sbp.Person;
import io.github.konohiroaki.sbp.PersonServiceGrpc;
import io.grpc.stub.StreamObserver;

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

    @Test
    public void listPersons() {
        Iterator<Person> persons = PersonServiceGrpc.newBlockingStub(channel)
                .listPersons(Empty.getDefaultInstance());
        List<Person> personList = new ArrayList<>();
        persons.forEachRemaining(personList::add);
        List<Integer> idList = personList.stream().map(p -> p.getId().getId()).collect(Collectors.toList());

        System.out.println("[Client] Received Response: " + idList);
    }

    @Test
    public void allPersonExisting() throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Existence> responseObserver = new StreamObserver<Existence>() {
            @Override public void onNext(Existence existence) {
                System.out.println("[Client] Response from server was: " + existence.getExist());
            }

            @Override public void onError(Throwable throwable) {
                System.out.println("[Client] Error: " + throwable.getMessage());
                finishLatch.countDown();
            }

            @Override public void onCompleted() {
                System.out.println("[Client] onCompleted is called");
                finishLatch.countDown();
            }
        };
        StreamObserver<Person.PersonId> requestObserver =
                PersonServiceGrpc.newStub(channel).allPersonExisting(responseObserver);
        for (int i = 1; i <= 2; i++) {
            Person.PersonId personId = Person.PersonId.newBuilder().setId(i).build();
            requestObserver.onNext(personId);
            System.out.println("[Client] Sent request: " + personId.getId());
        }
        requestObserver.onCompleted();
        System.out.println("[Client] Informed request completed");
        finishLatch.await();
    }

    @Test
    public void getPersons() throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Person> responseObserver = new StreamObserver<Person>() {
            @Override public void onNext(Person person) {
                System.out.println("[Client] Got response: " + person.getName());
            }

            @Override public void onError(Throwable throwable) {
                System.out.println("[Client] Error: " + throwable.getMessage());
                finishLatch.countDown();
            }

            @Override public void onCompleted() {
                System.out.println("[Client] onCompleted is called");
                finishLatch.countDown();
            }
        };
        StreamObserver<Person.PersonId> requestObserver =
                PersonServiceGrpc.newStub(channel).getPersons(responseObserver);
        for (int i = 1; i <= 2; i++) {
            Person.PersonId personId = Person.PersonId.newBuilder().setId(i).build();
            requestObserver.onNext(personId);
            System.out.println("[Client] Sent request: " + personId.getId());
        }
        requestObserver.onCompleted();
        System.out.println("[Client] Informed request completed");
        finishLatch.await();
    }
}