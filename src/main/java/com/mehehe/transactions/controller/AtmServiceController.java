package com.mehehe.transactions.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.AtmsApi;
import com.mehehe.openapi.model.ATM;
import com.mehehe.openapi.model.Task;
import com.mehehe.openapi.model.Task.RequestTypeEnum;

@RestController
public class AtmServiceController implements AtmsApi {

    public static final Comparator<ReducedTask> COMPARING_REGION_ID = Comparator.comparing(ReducedTask::regionId);
    public static final Function<ReducedTask, Integer> COMPARING_REQUEST_TYPE = e -> RequestType.valueOf(e.requestTypeEnum().name()).priority;
    public static final Function<ReducedTask, Integer> COMPARING_ORIGINAL_ORDER = e -> e.originalMinOrder;
    public static final Comparator<ReducedTask> COMPARATOR = COMPARING_REGION_ID
            .thenComparing(COMPARING_REQUEST_TYPE)
            .thenComparing(COMPARING_ORIGINAL_ORDER);

    @Override
    public ResponseEntity<List<ATM>> calculate(final List<Task> tasks) {
        final List<ATM> atmSorted = tasks.stream()
                .collect(Collectors.groupingBy(task -> new Identity(task.getRegion(), task.getAtmId())))
                .entrySet()
                .stream()
                .map(e -> reduceEntry(e, tasks))
                .sorted(COMPARATOR)
                .map(e -> of(e.regionId(), e.atmId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(atmSorted);
    }

    private static ReducedTask reduceEntry(final Entry<Identity, List<Task>> e, final List<Task> tasks) {
        final Identity key = e.getKey();
        final Task value = e.getValue().stream()
                .min(Comparator.comparing(task -> RequestType.valueOf(task.getRequestType().name()).priority))
                .orElseThrow(() -> new IllegalStateException("No task found after reduction"));
        return new ReducedTask(
                key.regionId(),
                key.atmId(),
                tasks.indexOf(value),
                value.getRequestType()
        );
    }

    private static ATM of(final int region, final int atmId) {
        final ATM account = new ATM();
        account.setAtmId(atmId);
        account.setRegion(region);
        return account;
    }

    public enum RequestType {
        FAILURE_RESTART(1),
        PRIORITY(2),
        SIGNAL_LOW(3),
        STANDARD(4);

        public final int priority;

        RequestType(final int priority) {
            this.priority = priority;
        }
    }

    public record Identity(int regionId, int atmId) {

    }

    public record ReducedTask(int regionId, int atmId, int originalMinOrder, RequestTypeEnum requestTypeEnum) {

    }
}
