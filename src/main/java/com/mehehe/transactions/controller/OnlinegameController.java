package com.mehehe.transactions.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.mehehe.openapi.api.OnlinegameApi;
import com.mehehe.openapi.model.Clan;
import com.mehehe.openapi.model.Players;

@RestController
public class OnlinegameController implements OnlinegameApi {

    @Override
    public ResponseEntity<List<List<Clan>>> calculate(final Players players) {
        return ResponseEntity.ok(List.of(
                List.of(
                        of(2, 70),
                        of(4, 50)
                ),
                List.of(
                        of(6, 60)
                ),
                List.of(
                        of(3, 45),
                        of(1, 15),
                        of(1, 12)
                ),
                List.of(
                        of(4, 40)
                ),
                List.of(
                        of(5, 40)
                )
        ));
    }

    private static Clan of(final int numberOfPlayers, final int points) {
        final Clan clan = new Clan();
        clan.setNumberOfPlayers(numberOfPlayers);
        clan.setPoints(points);
        return clan;
    }
}
