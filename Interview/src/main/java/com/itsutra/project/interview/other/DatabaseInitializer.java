package com.itsutra.project.interview.other;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class DatabaseInitializer {

    private final DatabaseClient databaseClient;

    public DatabaseInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void initializeDatabase() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:sqlData/*");

            Flux.fromArray(resources)
                    .flatMap(resource -> {
                        try {
                            // Read file content as a string
                            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                            // Split by semicolon to get individual statements
                            String[] statements = content.split(";");
                            return Flux.fromIterable(Arrays.asList(statements));
                        } catch (Exception e) {
                            return Flux.error(e);
                        }
                    })
                    .map(String::trim)
                    .filter(sql -> !sql.isEmpty())
                    .flatMap(sql -> databaseClient.sql(sql).then())
                    .doOnError(Throwable::printStackTrace)
                    .subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

