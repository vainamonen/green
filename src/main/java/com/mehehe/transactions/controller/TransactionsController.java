package com.mehehe.transactions.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.TransactionsApi;
import com.mehehe.openapi.model.Account;
import com.mehehe.openapi.model.Transaction;

@RestController
public class TransactionsController implements TransactionsApi {

    @Override
    public ResponseEntity<List<Account>> report(final List<Transaction> transaction) {
        return ResponseEntity.ok(List.of(
                of("06105023389842834748547303", 0, 1, 10.90f),
                of("31074318698137062235845814", 1, 0, -200.90f),
                of("32309111922661937852684864", 1, 1, 39.20f),
                of("66105036543749403346524547", 1, 1, 150.80f)
        ));
    }

    private static Account of(final String accountNumber, final int debitCount, final int creditCount, final float balance) {
        final Account account = new Account();
        account.setAccount(accountNumber);
        account.setDebitCount(debitCount);
        account.setCreditCount(creditCount);
        account.setBalance(balance);
        return account;
    }
}
