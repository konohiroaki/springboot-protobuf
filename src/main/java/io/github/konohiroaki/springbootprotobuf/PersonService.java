package io.github.konohiroaki.springbootprotobuf;

import org.lognet.springboot.grpc.GRpcService;

import io.github.konohiroaki.sbp.Person;
import io.github.konohiroaki.sbp.PersonServiceGrpc;
import io.grpc.stub.StreamObserver;

@GRpcService
public class PersonService extends PersonServiceGrpc.PersonServiceImplBase {

    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    private PersonRepository personRepo;

    @Override
    public void getPerson(Person.PersonId request, StreamObserver<Person> responseObserver) {
        // Construct Person for response.
        Person person = personRepo.getPerson(request);
        // Pass Person to .onNext() to return it as response.
        // For non-stream response, I guess this won't trigger sending back response to client. Instead it should wait until .onCompleted().
        responseObserver.onNext(person);
        // Use .onCompleted() to respond.
        responseObserver.onCompleted();
    }
}
