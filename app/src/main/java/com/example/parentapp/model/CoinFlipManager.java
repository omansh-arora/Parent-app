package com.example.parentapp.model;

import java.util.ArrayList;
import java.util.List;

public class CoinFlipManager {

    private static CoinFlipManager instance;
    private List<CoinFlip> flips = new ArrayList<>();

    public static CoinFlipManager getInstance() {

        if (instance == null) {

            instance = new CoinFlipManager();

        }
        return instance;

    }

}
