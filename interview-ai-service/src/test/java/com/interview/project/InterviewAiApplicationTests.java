package com.interview.project;

import com.interview.project.proto.InterviewAIServiceGrpc;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.ai-service.address=in-process:integration-test"
})
class InterviewAIGrpcServiceTest {

    @GrpcClient("ai-service")
    private InterviewAIServiceGrpc.InterviewAIServiceBlockingStub stub;

    @Test
    void experienceYearsTest() {
        var request = InterviewRequest.newBuilder()
                .setExperienceYears(5)
                .setCandidateName("test user")
                .build();

        InterviewResponse response = this.stub.evaluateCandidate(request);
        System.out.println(response);

        Assertions.assertEquals(
                "Candidate " + request.getCandidateName() + " needs more experience.",
                response.getResult()
        );
    }

    @Test
    public void invalidExperienceYearsTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = InterviewRequest.newBuilder()
                    .setExperienceYears(2)
                    .setCandidateName("test user")
                    .build();
           this.stub.evaluateCandidate(request);
        });
        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT,ex.getStatus().getCode());
    }
}

