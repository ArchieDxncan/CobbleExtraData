package com.cobblemon.fabric.example;

import java.util.HashMap;
import java.util.Map;

public class OriginGameMapper {
    private static final Map<String, String> ORIGIN_GAME_TO_REGION = new HashMap<>();

    static {
        // Gen 3
        ORIGIN_GAME_TO_REGION.put("cxd", "Orre");
        ORIGIN_GAME_TO_REGION.put("r", "Hoenn");
        ORIGIN_GAME_TO_REGION.put("s", "Hoenn");
        ORIGIN_GAME_TO_REGION.put("e", "Hoenn");
        ORIGIN_GAME_TO_REGION.put("fr", "Kanto");
        ORIGIN_GAME_TO_REGION.put("lg", "Kanto");

        // Gen 4
        ORIGIN_GAME_TO_REGION.put("d", "Sinnoh");
        ORIGIN_GAME_TO_REGION.put("p", "Sinnoh");
        ORIGIN_GAME_TO_REGION.put("pt", "Sinnoh");
        ORIGIN_GAME_TO_REGION.put("hg", "Johto");
        ORIGIN_GAME_TO_REGION.put("ss", "Johto");

        // Gen 5
        ORIGIN_GAME_TO_REGION.put("b", "Unova");
        ORIGIN_GAME_TO_REGION.put("w", "Unova");
        ORIGIN_GAME_TO_REGION.put("b2", "Unova");
        ORIGIN_GAME_TO_REGION.put("w2", "Unova");

        // Gen 6
        ORIGIN_GAME_TO_REGION.put("x", "Kalos");
        ORIGIN_GAME_TO_REGION.put("y", "Kalos");
        ORIGIN_GAME_TO_REGION.put("or", "Hoenn");
        ORIGIN_GAME_TO_REGION.put("as", "Hoenn");

        // Gen 7
        ORIGIN_GAME_TO_REGION.put("sn", "Alola");
        ORIGIN_GAME_TO_REGION.put("mn", "Alola");
        ORIGIN_GAME_TO_REGION.put("us", "Alola");
        ORIGIN_GAME_TO_REGION.put("um", "Alola");
        ORIGIN_GAME_TO_REGION.put("rd", "Kanto");
        ORIGIN_GAME_TO_REGION.put("bu", "Kanto");
        ORIGIN_GAME_TO_REGION.put("gn", "Kanto");
        ORIGIN_GAME_TO_REGION.put("yw", "Kanto");
        ORIGIN_GAME_TO_REGION.put("gd", "Johto");
        ORIGIN_GAME_TO_REGION.put("si", "Johto");
        ORIGIN_GAME_TO_REGION.put("c", "Johto");
        ORIGIN_GAME_TO_REGION.put("gp", "Kanto");
        ORIGIN_GAME_TO_REGION.put("ge", "Kanto");

        // Gen 8
        ORIGIN_GAME_TO_REGION.put("sw", "Galar");
        ORIGIN_GAME_TO_REGION.put("sh", "Galar");
        ORIGIN_GAME_TO_REGION.put("bd", "Sinnoh");
        ORIGIN_GAME_TO_REGION.put("sp", "Sinnoh");
        ORIGIN_GAME_TO_REGION.put("pla", "Hisui");

        // Gen 9
        ORIGIN_GAME_TO_REGION.put("sl", "Paldea");
        ORIGIN_GAME_TO_REGION.put("vl", "Paldea");
        ORIGIN_GAME_TO_REGION.put("za", "Kalos");

        // Misc
        ORIGIN_GAME_TO_REGION.put("cobblemon", "Cobblemon");
        ORIGIN_GAME_TO_REGION.put("any", "Cobblemon");
        ORIGIN_GAME_TO_REGION.put("go", "GO");
    }

    public static String getRegion(String originGame) {
        return ORIGIN_GAME_TO_REGION.getOrDefault(originGame.toLowerCase(), "???");
    }
}