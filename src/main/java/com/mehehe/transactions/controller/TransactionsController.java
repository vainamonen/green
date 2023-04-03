package com.mehehe.transactions.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.TransactionsApi;
import com.mehehe.openapi.model.Account;
import com.mehehe.openapi.model.Transaction;
import com.mehehe.transactions.controller.TransactionsController.Event.Type;

@RestController
public class TransactionsController implements TransactionsApi {

    @Override
    public ResponseEntity<List<Account>> report(final List<Transaction> transaction) {
        final List<Account> result = transaction.stream()
                .flatMap(TransactionsController::mapToEvents)
                .collect(Collectors.groupingBy(e -> e.account))
                .entrySet()
                .stream()
                .map(TransactionsController::mapToAccount)
                .sorted(Comparator.comparing(Account::getAccount))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private static Account mapToAccount(final Entry<String, List<Event>> entry) {
        final List<Event> events = entry.getValue();
        final AtomicInteger debitCount = new AtomicInteger();
        final AtomicInteger creditCount = new AtomicInteger();
        final AtomicReference<BigDecimal> balance = new AtomicReference<>(new BigDecimal(0));
        events.forEach(e -> {
            switch (e.type) {
                case DEBIT -> {
                    debitCount.getAndIncrement();
                    balance.getAndUpdate(b -> b.subtract(e.amount));
                }
                case CREDIT -> {
                    creditCount.getAndIncrement();
                    balance.getAndUpdate(b -> b.add(e.amount));
                }
            }
        });
        return of(entry.getKey(), debitCount.get(), creditCount.get(), balance.get().floatValue());
    }

    private static Stream<Event> mapToEvents(final Transaction t) {
        return Stream.of(
                new Event(t.getCreditAccount(), Type.CREDIT, BigDecimal.valueOf(t.getAmount())),
                new Event(t.getDebitAccount(), Type.DEBIT, BigDecimal.valueOf(t.getAmount())));
    }

    private static Account of(final String accountNumber, final int debitCount, final int creditCount, final float balance) {
        final Account account = new Account();
        account.setAccount(accountNumber);
        account.setDebitCount(debitCount);
        account.setCreditCount(creditCount);
        account.setBalance(balance);
        return account;
    }

    public record Event(String account, Type type, BigDecimal amount) {

        enum Type {
            DEBIT, CREDIT
        }
    }
}
