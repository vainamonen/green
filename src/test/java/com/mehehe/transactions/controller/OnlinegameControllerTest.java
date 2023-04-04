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
import com.mehehe.openapi.model.Clan;
import com.mehehe.openapi.model.Players;

class OnlinegameControllerTest {

    private static final String REQUEST_STR = """
            {
              "groupCount": 6,
              "clans": [
                {
                  "numberOfPlayers": 4,
                  "points": 50
                },
                {
                  "numberOfPlayers": 2,
                  "points": 70
                },
                {
                  "numberOfPlayers": 6,
                  "points": 60
                },
                {
                  "numberOfPlayers": 1,
                  "points": 15
                },
                {
                  "numberOfPlayers": 5,
                  "points": 40
                },
                {
                  "numberOfPlayers": 3,
                  "points": 45
                },
                {
                  "numberOfPlayers": 1,
                  "points": 12
                },
                {
                  "numberOfPlayers": 4,
                  "points": 40
                }
              ]
            }
                        """;
    private static final String RESPONSE_STR = """
                     [
                       [
                         {
                           "numberOfPlayers": 2,
                           "points": 70
                         },
                         {
                           "numberOfPlayers": 4,
                           "points": 50
                         }
                       ],
                       [
                         {
                           "numberOfPlayers": 6,
                           "points": 60
                         }
                       ],
                       [
                         {
                           "numberOfPlayers": 3,
                           "points": 45
                         },
                         {
                           "numberOfPlayers": 1,
                           "points": 15
                         },
                         {
                           "numberOfPlayers": 1,
                           "points": 12
                         }
                       ],
                       [
                         {
                           "numberOfPlayers": 4,
                           "points": 40
                         }
                       ],
                       [
                         {
                           "numberOfPlayers": 5,
                           "points": 40
                         }
                       ]
                     ]
            """;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlinegameController onlinegameController = new OnlinegameController();

    @Order(1)
    @Test
    void testSingle() throws JsonProcessingException {
        //given
        final Players request = objectMapper.readValue(REQUEST_STR, new TypeReference<>() {
        });
        final List<List<Clan>> expectedResponse = objectMapper.readValue(RESPONSE_STR, new TypeReference<>() {
        });

        //when
        final ResponseEntity<List<List<Clan>>> result = onlinegameController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Order(2)
    @Test
    void stressTest() throws JsonProcessingException {
        //given
        final Players request = objectMapper.readValue(REQUEST_STR, new TypeReference<>() {
        });
        final List<List<Clan>> expectedResponse = objectMapper.readValue(RESPONSE_STR, new TypeReference<>() {
        });
        final List<Clan> clans = request.getClans();
        final List<Clan> requestMultiplied = new ArrayList<>();
        for (int i = 0; i < (100000 / clans.size()) + 1; i++) {
            requestMultiplied.addAll(clans);
        }
        request.setClans(requestMultiplied);

        //when
        final ResponseEntity<List<List<Clan>>> result = onlinegameController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.size(), result.getBody().size());
    }

    @Order(3)
    @Test
    void stressTest2() throws JsonProcessingException {
        //given
        final Players request = objectMapper.readValue(REQUEST_STR, new TypeReference<>() {
        });
        final List<List<Clan>> expectedResponse = objectMapper.readValue(RESPONSE_STR, new TypeReference<>() {
        });
        final List<Clan> clans = request.getClans();
        final List<Clan> requestMultiplied = new ArrayList<>();
        for (int i = 0; i < (100000 / clans.size()) + 1; i++) {
            requestMultiplied.addAll(clans);
        }
        request.setClans(requestMultiplied);

        //when
        final ResponseEntity<List<List<Clan>>> result = onlinegameController.calculate(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.size(), result.getBody().size());
    }
}