package com.mehehe.transactions.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehehe.openapi.model.ATM;
import com.mehehe.openapi.model.Task;

class AtmServiceControllerTest {

    private static final String REQUEST_STR_1 = """
            [
              {
                "region": 4,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 1,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 2,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 3,
                "requestType": "PRIORITY",
                "atmId": 2
              },
              {
                "region": 3,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 2,
                "requestType": "SIGNAL_LOW",
                "atmId": 1
              },
              {
                "region": 5,
                "requestType": "STANDARD",
                "atmId": 2
              },
              {
                "region": 5,
                "requestType": "FAILURE_RESTART",
                "atmId": 1
              }
            ]
                        """;
    private static final String REQUEST_STR_2 = """
            [
              {
                "region": 1,
                "requestType": "STANDARD",
                "atmId": 2
              },
              {
                "region": 1,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 2,
                "requestType": "PRIORITY",
                "atmId": 3
              },
              {
                "region": 3,
                "requestType": "STANDARD",
                "atmId": 4
              },
              {
                "region": 4,
                "requestType": "STANDARD",
                "atmId": 5
              },
              {
                "region": 5,
                "requestType": "PRIORITY",
                "atmId": 2
              },
              {
                "region": 5,
                "requestType": "STANDARD",
                "atmId": 1
              },
              {
                "region": 3,
                "requestType": "SIGNAL_LOW",
                "atmId": 2
              },
              {
                "region": 2,
                "requestType": "SIGNAL_LOW",
                "atmId": 1
              },
              {
                "region": 3,
                "requestType": "FAILURE_RESTART",
                "atmId": 1
              }
            ]
                        """;
    private static final String RESPONSE_STR_1 = """
            [
              {
                "region": 1,
                "atmId": 1
              },
              {
                "region": 2,
                "atmId": 1
              },
              {
                "region": 3,
                "atmId": 2
              },
              {
                "region": 3,
                "atmId": 1
              },
              {
                "region": 4,
                "atmId": 1
              },
              {
                "region": 5,
                "atmId": 1
              },
              {
                "region": 5,
                "atmId": 2
              }
            ]
            """;
    private static final String RESPONSE_STR_2 = """
            [
              {
                "region": 1,
                "atmId": 2
              },
              {
                "region": 1,
                "atmId": 1
              },
              {
                "region": 2,
                "atmId": 3
              },
              {
                "region": 2,
                "atmId": 1
              },
              {
                "region": 3,
                "atmId": 1
              },
              {
                "region": 3,
                "atmId": 2
              },
              {
                "region": 3,
                "atmId": 4
              },
              {
                "region": 4,
                "atmId": 5
              },
              {
                "region": 5,
                "atmId": 2
              },
              {
                "region": 5,
                "atmId": 1
              }
            ]
            """;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtmServiceController atmServiceController = new AtmServiceController();

    @Order(1)
    @Test
    void testSingle1() throws JsonProcessingException {
        //given
        final List<Task> request = objectMapper.readValue(REQUEST_STR_1, new TypeReference<>() {
        });
        final List<ATM> expectedResponse = objectMapper.readValue(RESPONSE_STR_1, new TypeReference<>() {
        });

        //when
        final ResponseEntity<List<ATM>> result = atmServiceController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Order(2)
    @Test
    void testSingle2() throws JsonProcessingException {
        //given
        final List<Task> request = objectMapper.readValue(REQUEST_STR_2, new TypeReference<>() {
        });
        final List<ATM> expectedResponse = objectMapper.readValue(RESPONSE_STR_2, new TypeReference<>() {
        });

        //when
        final ResponseEntity<List<ATM>> result = atmServiceController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Order(3)
    @Test
    void stressTest1() throws JsonProcessingException {
        //given
        final List<Task> request = objectMapper.readValue(REQUEST_STR_1, new TypeReference<>() {
        });
        final List<ATM> expectedResponse = objectMapper.readValue(RESPONSE_STR_1, new TypeReference<>() {
        });
        multiplyRequest(request);

        //when
        final ResponseEntity<List<ATM>> result = atmServiceController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.size(), result.getBody().size());
    }

    @Order(4)
    @Test
    void stressTest2() throws JsonProcessingException {
        //given
        final List<Task> request = objectMapper.readValue(REQUEST_STR_2, new TypeReference<>() {
        });
        final List<ATM> expectedResponse = objectMapper.readValue(RESPONSE_STR_2, new TypeReference<>() {
        });
        multiplyRequest(request);

        //when
        final ResponseEntity<List<ATM>> result = atmServiceController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.size(), result.getBody().size());
    }

    private static void multiplyRequest(final List<Task> request) {
        final List<Task> requestMultiplied = new ArrayList<>(100000);
        for (int i = 0; i < (100000 / request.size()) + 1; i++) {
            requestMultiplied.addAll(request);
        }
        request.clear();
        request.addAll(requestMultiplied);
    }
}