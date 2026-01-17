package com.core.project.interview.service;

import com.core.project.interview.dto.InterviewRequestDTO;
import com.core.project.interview.dto.InterviewResponseDTO;
import com.interview.project.proto.InterviewAIServiceGrpc;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import com.interview.project.proto.StringResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class InterviewAiGrpcService {

    @GrpcClient("ai-service")
    private InterviewAIServiceGrpc.InterviewAIServiceStub stub;

    public Mono<InterviewResponseDTO> getAiInformation(InterviewRequestDTO interviewRequest) {

        InterviewRequest request = InterviewRequest.newBuilder()
                .setCandidateName(interviewRequest.getCandidateName())
                .setExperienceYears(interviewRequest.getExperienceYears())
                .build();

        return Mono.create(sink -> {
            stub.evaluateCandidate(request, new StreamObserver<>() {
                @Override
                public void onNext(InterviewResponse response) {
                    InterviewResponseDTO interviewResponseDTO = new InterviewResponseDTO();
                    interviewResponseDTO.setCandidateName(response.getResult());
                    sink.success(interviewResponseDTO);
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


    public Mono<InterviewResponseDTO> performClientStreaming(int years) {

        return Mono.create(sink -> {

            StreamObserver<InterviewResponse> responseObserver =
                    new StreamObserver<>() {

                        @Override
                        public void onNext(InterviewResponse response) {
                            InterviewResponseDTO dto = new InterviewResponseDTO();
                            dto.setCandidateName(response.getResult());
                            sink.success(dto);
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t);
                        }

                        @Override
                        public void onCompleted() {
                            // nothing needed
                        }
                    };

            // Client-side request stream
            StreamObserver<InterviewRequest> requestObserver =
                    stub.evaluateCandidateClientStreaming(responseObserver);

            try {
                // 👇 convert "years" into multiple stream messages
                for (int experience = 1; experience <= years; experience++) {
                    System.out.println("Processing for Mahesh "+experience);
                    InterviewRequest request = InterviewRequest.newBuilder()
                            .setCandidateName("Mahesh "+experience)
                            .setExperienceYears(experience)
                            .build();

                    requestObserver.onNext(request);
                }
                requestObserver.onCompleted();

            } catch (Exception e) {
                requestObserver.onError(e);
            }
        });
    }

    public Flux<InterviewResponseDTO> performBidirectionalStreaming(int years) {

        return Flux.<InterviewResponseDTO>create(sink -> {

            // 1️⃣ Response observer from server
            StreamObserver<InterviewResponse> responseObserver = new StreamObserver<>() {
                @Override
                public void onNext(InterviewResponse response) {
                    InterviewResponseDTO dto = new InterviewResponseDTO();
                    dto.setCandidateName(response.getResult());
                    sink.next(dto);
                }

                @Override
                public void onError(Throwable t) {
                    sink.error(t);
                }

                @Override
                public void onCompleted() {
                    sink.complete();
                }
            };

            // 2️⃣ Get client-side request observer
            StreamObserver<InterviewRequest> requestObserver =
                    stub.evaluateCandidateBidirectionalStreaming(responseObserver);

            // 3️⃣ Send multiple requests reactively
            Flux.range(1, years)
                    .delayElements(Duration.ofSeconds(1))
                    .doOnNext(experience -> {
                        InterviewRequest request = InterviewRequest.newBuilder()
                                .setCandidateName("Mahesh")
                                .setExperienceYears(experience)
                                .build();
                        requestObserver.onNext(request);
                    })
                    .doOnComplete(requestObserver::onCompleted)
                    .subscribe();

        }, FluxSink.OverflowStrategy.BUFFER);
    }





}
