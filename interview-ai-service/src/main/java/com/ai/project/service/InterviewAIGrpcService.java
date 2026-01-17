package com.ai.project.service;


import com.ai.project.exception.InvalidExperienceException;
import com.interview.project.proto.InterviewAIServiceGrpc;
import com.interview.project.proto.InterviewRequest;
import com.interview.project.proto.InterviewResponse;
import com.interview.project.proto.StringResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
public class InterviewAIGrpcService extends InterviewAIServiceGrpc.InterviewAIServiceImplBase {



    @Override
    public void evaluateCandidate(InterviewRequest request,
                                  StreamObserver<InterviewResponse> responseObserver) {

        if(request.getExperienceYears() < 3 ){
            throw new InvalidExperienceException();
        }


        // Simple AI evaluation logic
        String candidateName = request.getCandidateName();
        int experience = request.getExperienceYears();

        String result;
        if (experience > 5) {
            result = "Candidate " + candidateName + " is highly recommended.";
        } else {
            result = "Candidate " + candidateName + " needs more experience.";
        }

        InterviewResponse response = InterviewResponse.newBuilder()
                .setResult(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void evaluateCandidateServerStreaming( com.google.protobuf.Empty request,StreamObserver<StringResponse> responseObserver) {

        for (int i=0; i<=10;i++) {
            StringResponse response = StringResponse.newBuilder().setValue(String.valueOf(i)).build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<InterviewRequest> evaluateCandidateClientStreaming(StreamObserver<InterviewResponse> responseObserver) {
        return new StreamObserver<InterviewRequest>() {

            int candidateCount = 0;

            @Override
            public void onNext(InterviewRequest request) {
                System.out.println("Received InterviewRequest: " + request.getCandidateName());
                candidateCount++;
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                if (candidateCount == 0) {
                    responseObserver.onError(
                            io.grpc.Status.INVALID_ARGUMENT
                                    .withDescription("No candidates received")
                                    .asRuntimeException()
                    );
                    return;
                }
                InterviewResponse response = InterviewResponse.newBuilder()
                        .setResult("Processed Items: "+candidateCount)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}

