package com.mehehe.transactions.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.OnlinegameApi;
import com.mehehe.openapi.model.Clan;
import com.mehehe.openapi.model.Players;

@RestController
public class OnlinegameController implements OnlinegameApi {

    public static final Comparator<Clan> BY_POINTS_DESC = Comparator.comparing(Clan::getPoints).reversed();
    public static final Comparator<Clan> BY_NO_OF_PLAYERS_ASC = Comparator.comparing(Clan::getNumberOfPlayers);
    public static final Function<Clan, BigDecimal> EXTRACT_PTS_PER_PLAYER = clan -> new BigDecimal(clan.getPoints())
            .divide(new BigDecimal(clan.getNumberOfPlayers()), MathContext.DECIMAL64);

    @Override
    public ResponseEntity<List<List<Clan>>> calculate(final Players players) {
        final Integer groupSize = players.getGroupCount();
        final List<Clan> clansSorted = players.getClans().stream()
                .sorted(BY_POINTS_DESC.thenComparing(BY_NO_OF_PLAYERS_ASC))
                .collect(Collectors.toList());
        final Map<BigDecimal, List<Clan>> collect = clansSorted.stream()
                .collect(Collectors.groupingBy(EXTRACT_PTS_PER_PLAYER));
        final List<BigDecimal> bigDecimals = collect
                .keySet().stream()
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.ok(List.of(

        ));
    }

    private static Clan of(final int numberOfPlayers, final int points) {
        final Clan clan = new Clan();
        clan.setNumberOfPlayers(numberOfPlayers);
        clan.setPoints(points);
        return clan;
    }
}
