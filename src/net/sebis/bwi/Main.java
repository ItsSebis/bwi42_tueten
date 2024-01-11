package net.sebis.bwi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Main instance; // this
    private int bagCount; // count of bags to fill
    private HashMap<Integer, HashMap<Integer, Integer>> bags = new HashMap<>(); // map of bagId -> bag content
    private final List<Integer> games = new ArrayList<>(); // list of how many instances each game has
    private int lastBag = 0;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        instance = this;

        getData();
        System.out.println("bags: " + bagCount);
        System.out.println(games);

        for (int gameId = 0; gameId < games.size(); gameId++) {
            Sorter gameSort = new Sorter(gameId, games.get(gameId));
            gameSort.run();
        }

        printResults();
    }

    public void getData() {
        URI uri;
        try {
            uri = new URI("https://bwinf.de/fileadmin/user_upload/wundertuete5.txt"); // input data to work with
        } catch (URISyntaxException e) {throw new RuntimeException(e);}

        try {
            Scanner sc = new Scanner(uri.toURL().openStream()); // read over the file
            int line = 0;
            while (sc.hasNextInt()) {
                if (line == 0) {
                    bagCount = sc.nextInt();
                } else if (line > 1) {
                    games.add(sc.nextInt());
                }
                sc.nextLine();
                line++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printResults() {
        System.out.println("----------- Results -----------");

        System.out.println("  Bag Games");
        System.out.print("   ID");
        for (int gameId = 0; gameId < games.size(); gameId++) {
            int repeat = 4-(""+gameId).length();
            System.out.print(" " + " ".repeat(repeat) + gameId);
        }
        System.out.println(" Total");
        for (int bagId = 0; bagId < bags.size(); bagId++) {
            System.out.print(" " + " ".repeat(4-(""+bagId).length()) + bagId);
            int totalGames = 0;

            for (int gameId = 0; gameId < games.size(); gameId++) {
                int rsCount;
                if (bags.get(bagId).get(gameId) == null) {
                    rsCount = 0;
                } else {
                    rsCount = bags.get(bagId).get(gameId);
                }
                totalGames += rsCount;
                System.out.print(" " + " ".repeat(4-(""+rsCount).length()) + rsCount);
            }
            System.out.print(" " + " ".repeat(5-(""+totalGames).length()) + totalGames);
            System.out.println();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public int getBagCount() {
        return bagCount;
    }

    public int getLastBag() {
        return lastBag;
    }

    public void setLastBag(int lastBag) {
        this.lastBag = lastBag;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getBags() {
        return bags;
    }
}