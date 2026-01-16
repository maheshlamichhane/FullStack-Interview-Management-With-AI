package com.core.project.interview.service;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.google.protobuf.Empty;
import com.interview.project.proto.InterviewAIServiceGrpc;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import com.interview.project.proto.StringResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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


    public Flux<String> getServerStreamingData() {
        return Flux.<StringResponse>create(sink -> {
                    // Pass Empty request since your proto uses google.protobuf.Empty
                    stub.evaluateCandidateServerStreaming(
                            com.google.protobuf.Empty.getDefaultInstance(),
                            new StreamObserver<StringResponse>() {

                                @Override
                                public void onNext(StringResponse response) {
                                    sink.next(response);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    sink.error(t);
                                }

                                @Override
                                public void onCompleted() {
                                    sink.complete();
                                }
                            });
                })
                .map(StringResponse::getValue)
                .delayElements(Duration.ofSeconds(1));
    }



}
