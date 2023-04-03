package com.mehehe.transactions.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehehe.openapi.model.Account;
import com.mehehe.openapi.model.Transaction;

class TransactionsControllerTest {

    private static final String REQUEST_STR = """
            [
              {
                "debitAccount": "32309111922661937852684864",
                "creditAccount": "06105023389842834748547303",
                "amount": 10.90
              },
              {
                "debitAccount": "31074318698137062235845814",
                "creditAccount": "66105036543749403346524547",
                "amount": 200.90
              },
              {
                "debitAccount": "66105036543749403346524547",
                "creditAccount": "32309111922661937852684864",
                "amount": 50.10
              }
            ]
            """;
    private static final String RESPONSE_STR = """
            [
              {
                "account": "06105023389842834748547303",
                "debitCount": 0,
                "creditCount": 1,
                "balance": 10.90
              },
              {
                "account": "31074318698137062235845814",
                "debitCount": 1,
                "creditCount": 0,
                "balance": -200.90
              },
              {
                "account": "32309111922661937852684864",
                "debitCount": 1,
                "creditCount": 1,
                "balance": 39.20
              },
              {
                "account": "66105036543749403346524547",
                "debitCount": 1,
                "creditCount": 1,
                "balance": 150.80
              }
            ]
                        
                        
            """;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TransactionsController transactionsController = new TransactionsController();

    @Test
    void testSingle() throws JsonProcessingException {
        //given
        final List<Transaction> request = objectMapper.readValue(REQUEST_STR, new TypeReference<>() {
        });
        final List<Account> expectedResponse = objectMapper.readValue(RESPONSE_STR, new TypeReference<>() {
        });

        //when
        final ResponseEntity<List<Account>> result = transactionsController.report(request);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void stressTest() throws JsonProcessingException {
        //given
        final List<Transaction> request = objectMapper.readValue(REQUEST_STR, new TypeReference<>() {
        });
        final List<Account> expectedResponse = objectMapper.readValue(RESPONSE_STR, new TypeReference<>() {
        });
        List<Transaction> requestMultiplied = new ArrayList<>(100000);
        for (int i = 0; i < (100000 / request.size()) + 1; i++) {
            requestMultiplied.addAll(request);
        }

        //when
        final ResponseEntity<List<Account>> result = transactionsController.report(requestMultiplied);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.size(), result.getBody().size());
    }
}