package io.github.konohiroaki.springbootprotobuf;

import org.lognet.springboot.grpc.GRpcService;

import java.util.Map;

import io.github.konohiroaki.sbp.Empty;
import io.github.konohiroaki.sbp.Existence;
import io.github.konohiroaki.sbp.Person;
import io.github.konohiroaki.sbp.PersonServiceGrpc;
import io.grpc.stub.StreamObserver;

@GRpcService
public class PersonService extends PersonServiceGrpc.PersonServiceImplBase {

    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    private PersonRepository personRepo;

    /**
     * Get a person
     */
    @Override
    public void getPerson(Person.PersonId request, StreamObserver<Person> responseObserver) {
        Person person = personRepo.getPerson(request);
        responseObserver.onNext(person);
        responseObserver.onCompleted();
    }

    /**
     * Get all persons
     */
    @Override
    public void listPersons(Empty request, StreamObserver<Person> responseObserver) {
        for (Map.Entry<Person.PersonId, Person> person : personRepo.persons.entrySet()) {
            System.out.println("[Server] Responding person: " + person.getKey().getId());
            responseObserver.onNext(person.getValue());
        }
        responseObserver.onCompleted();
    }

    /**
     * Check if all person exists
     */
    @Override
    public StreamObserver<Person.PersonId> allPersonExisting(StreamObserver<Existence> responseObserver) {
        return new StreamObserver<Person.PersonId>() {
            boolean exists = true;

            @Override public void onNext(Person.PersonId personId) {
                System.out.println("[Server] Received request: " + personId.getId());
                if (!personRepo.persons.containsKey(personId)) {
                    exists = false;
                }
            }

            @Override public void onError(Throwable throwable) {
                System.out.println("[Server] Error: " + throwable.getMessage());
            }

            @Override public void onCompleted() {
                System.out.println("[Server] onCompleted is called");
                responseObserver.onNext(Existence.newBuilder().setExist(exists).build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * Get person for each requested personId
     */
    @Override
    public StreamObserver<Person.PersonId> getPersons(StreamObserver<Person> responseObserver) {
        return new StreamObserver<Person.PersonId>() {
            @Override public void onNext(Person.PersonId personId) {
                System.out.println("[Server] Received request: " + personId.getId());
                if (personRepo.persons.containsKey(personId)) {
                    responseObserver.onNext(personRepo.getPerson(personId));
                }
            }

            @Override public void onError(Throwable throwable) {
                System.out.println("[Server] Error: " + throwable.getMessage());
            }

            @Override public void onCompleted() {
                System.out.println("[Server] onCompleted is called");
                responseObserver.onCompleted();
            }
        };
    }
}
