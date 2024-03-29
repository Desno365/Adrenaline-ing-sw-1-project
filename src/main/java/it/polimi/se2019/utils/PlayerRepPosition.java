package it.polimi.se2019.utils;

import it.polimi.se2019.model.player.PlayerRep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a sharable version of the leaderboard.
 *
 * @author Desno365
 */
public class PlayerRepPosition implements Serializable {

    private final ArrayList<PlayerRep> playerReps;


    public PlayerRepPosition() {
        playerReps = new ArrayList<>();
    }


    public List<PlayerRep> getPlayerReps() {
        return playerReps;
    }

    public void addInPosition(PlayerRep playerRep) {
        playerReps.add(playerRep);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PlayerRep playerRep : playerReps) {
            stringBuilder.append(playerRep.getPlayerName());
            stringBuilder.append(" - ");
        }
        return stringBuilder.toString();
    }
}
