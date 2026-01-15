package com.interview.project.interview.config;

public class Test {
    public static void main(String[] args) {
        System.out.println("Main thread starts");

        Thread worker = new Thread(() -> {
            System.out.println("Worker: Starting work...");
            try {
                Thread.sleep(3000);  // Work for 3 seconds
            } catch (Exception e) {}
            System.out.println("Worker: Work done!");
        });

        worker.start();  // Start worker thread

        System.out.println("Main thread continues immediately!");
        System.out.println("Main thread ends");
    }
}
