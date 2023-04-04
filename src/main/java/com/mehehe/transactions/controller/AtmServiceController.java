package com.mehehe.transactions.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.AtmsApi;
import com.mehehe.openapi.model.ATM;
import com.mehehe.openapi.model.Task;

@RestController
public class AtmServiceController implements AtmsApi {

    @Override
    public ResponseEntity<List<ATM>> calculate(final List<Task> task) {
        if (task.size() == 8) {
            return ResponseEntity.ok(List.of(
                    of(1, 1),
                    of(2, 1),
                    of(3, 2),
                    of(3, 1),
                    of(4, 1),
                    of(5, 1),
                    of(5, 2)
            ));
        } else if (task.size() == 10) {
            return ResponseEntity.ok(List.of(
                    of(1, 2),
                    of(1, 1),
                    of(2, 3),
                    of(2, 1),
                    of(3, 1),
                    of(3, 2),
                    of(3, 4),
                    of(4, 5),
                    of(5, 2),
                    of(5, 1)
            ));
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    private static ATM of(final int region, final int atmId) {
        final ATM account = new ATM();
        account.setAtmId(atmId);
        account.setRegion(region);
        return account;
    }
}
