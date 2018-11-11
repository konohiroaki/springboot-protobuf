package io.github.konohiroaki.springbootprotobuf;

import org.junit.After;
import org.junit.Before;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;

import java.util.Optional;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public abstract class GrpcServerTestBase {

    protected ManagedChannel channel;

    @LocalRunningGrpcPort
    private int runningPort;

    @Before
    public final void setupChannels() {
        channel = ManagedChannelBuilder.forAddress("localhost", runningPort).usePlaintext().build();
    }

    @After
    public final void shutdownChannels() {
        Optional.ofNullable(channel).ifPresent(ManagedChannel::shutdownNow);
    }
}