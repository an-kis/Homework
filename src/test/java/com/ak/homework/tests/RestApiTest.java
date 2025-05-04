package com.ak.homework.tests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestApiTest {

    private static final Logger logger = LoggerFactory.getLogger(RestApiTest.class);
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testUsersApi() throws IOException {
       
        logger.info("Sending GET request to https://jsonplaceholder.typicode.com/users");
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/users")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("GET request failed with status code: {}", response.code());
                throw new IOException("Unexpected code " + response.code());
            }
            logger.info("GET request successful, status code: {}", response.code());

            
            logger.info("Parsing response to JSON");
            String responseBody = response.body().string();
            User[] users = mapper.readValue(responseBody, User[].class);
            logger.info("Response parsed, found {} users", users.length);

           
            logger.info("Logging names and emails");
            for (User user : users) {
                logger.info("{} | {}", user.getName(), user.getEmail());
            }

            
            logger.info("Verifying first email contains @");
            String firstEmail = users[0].getEmail();
            assertTrue(firstEmail.contains("@"), "First email does not contain @ symbol: " + firstEmail);
            logger.info("First email verified: {}", firstEmail);
        }
    }

    // Helper class to parse JSON user data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class User {
        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}