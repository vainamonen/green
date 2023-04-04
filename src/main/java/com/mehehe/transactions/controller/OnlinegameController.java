package com.mehehe.transactions.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

    @Override
    public ResponseEntity<List<List<Clan>>> calculate(final Players players) {
        final Integer groupSize = players.getGroupCount();
        final List<Clan> clansSorted = players.getClans().stream()
                .sorted(BY_POINTS_DESC.thenComparing(BY_NO_OF_PLAYERS_ASC))
                .collect(Collectors.toList());
        final List<List<Clan>> result = new ArrayList<>();
        final List<Clan> clansQualified = new ArrayList<>();
        while (clansQualified.size() < clansSorted.size()) {
            final List<Clan> group = new ArrayList<>();
            final Iterator<Clan> iterator = clansSorted.iterator();
            int currentSize = 0;
            while (currentSize < groupSize && iterator.hasNext()) {
                final Clan clan = iterator.next();
                final Integer numberOfPlayers = clan.getNumberOfPlayers();
                if (clansQualified.contains(clan)) {
                    continue;
                }
                if (currentSize + numberOfPlayers <= groupSize) {
                    group.add(clan);
                    clansQualified.add(clan);
                    currentSize += numberOfPlayers;
                }
            }
            if (group.isEmpty()) {
                break;
            }
            result.add(new ArrayList<>(group));
            group.clear();
        }
        return ResponseEntity.ok(result);
    }
}
