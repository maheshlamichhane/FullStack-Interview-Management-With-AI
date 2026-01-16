package com.core.project.interview.service;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.interview.project.proto.InterviewAIServiceGrpc;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InterviewAiGrpcService {

    @GrpcClient("ai-service")

    private InterviewAIServiceGrpc.InterviewAIServiceStub stub;

    public Mono<InterviewResponse> getAiInformation(InterviewRequestDTO interviewRequest) {

        InterviewRequest request = InterviewRequest.newBuilder()
                .setCandidateName(interviewRequest.getCandidateName())
                .setExperienceYears(interviewRequest.getExperienceYears())
                .build();

        return Mono.create(sink -> {
            stub.evaluateCandidate(request, new StreamObserver<>() {
                @Override
                public void onNext(InterviewResponse response) {
                    sink.success(response);
                }

                @Override
                public void onError(Throwable t) {
                    sink.error(t);
                }

                @Override
                public void onCompleted() {
                }
            });
        });
    }
}
