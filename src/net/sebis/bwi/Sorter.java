package net.sebis.bwi;

import java.util.HashMap;

public class Sorter implements Runnable {

    private final int gameId; // which game to distribute
    private final int gameCount; // how many games to distribute
    private final Main main;

    public Sorter(int id, int count) {
        this.gameId = id;
        this.gameCount = count;
        main = Main.getInstance();
    }

    @Override
    public void run() {

        HashMap<Integer, Integer> bagGameCounts = new HashMap<>(); // key = bagId; value = games contained;

        int gamesLeft = gameCount;
        int bagId = main.getLastBag();

        while (gamesLeft > 0) {
            //System.out.println("left: " + gamesLeft);
            if (!bagGameCounts.containsKey(bagId)) {
                bagGameCounts.put(bagId, 1);
            } else {
                bagGameCounts.put(bagId, bagGameCounts.get(bagId) + 1);
            }
            gamesLeft--;
            if (bagId < main.getBagCount()-1) {
                bagId++;
            } else {
                bagId = 0;
            }
        }

        main.setLastBag(bagId);
        System.out.print("GameId: " + gameId + " | " + gameCount + " | ");
        bagGameCounts.forEach((k, v) -> System.out.print(k + ": " + v + ", "));
        System.out.println("NextBag: " + bagId);

        bagGameCounts.forEach((k, v) -> putGamesInBag(k, gameId, v));
    }

    private void putGamesInBag(int bagId, int gameId, int gameCount) {
        if (!main.getBags().containsKey(bagId)) {
            main.getBags().put(bagId, new HashMap<>());
        }
        main.getBags().get(bagId).put(gameId, gameCount);
    }
}
