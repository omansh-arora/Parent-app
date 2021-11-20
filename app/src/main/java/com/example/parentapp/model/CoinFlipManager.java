package com.example.parentapp.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * As a parent...
 *
 * If there are no configured children, I want to have the app skip allowing someone
 * to pick heads or tails and instead just flip the coin.
 * It is critical that the flip be random (i.e., not repeat the same result each time the app is started!)
 * because, well, isn't it obvious? If not, go ask any parent! :)
 * I want to be able to view a history of coin flips:
 * date and time of the flip
 * who got to pick heads vs tails
 * what the flip came up
 * easy to understand (at a glance) display of if the "picker" won.
 * For example, show a check mark icon if they won, X if they lost
 */
public class CoinFlipManager implements Iterable<CoinFlip> {

    private static Random random = new Random();
    private static CoinFlipManager instance;
    private List<CoinFlip> flipsList = new ArrayList<>();
    private ChildManager childManager;
    private Child selectedChild;

    private CoinFlipManager() {
        childManager = new ChildManager();
    }

    public static CoinFlipManager getInstance() {
        if (instance == null) {
            instance = new CoinFlipManager();
        }
        return instance;
    }

    public void addFlipGame(CoinFlip coinFlip) {
        flipsList.add(coinFlip);
    }


    public List<CoinFlip> getFlips() {
        return flipsList;
    }

    @Override
    public Iterator<CoinFlip> iterator() {
        return flipsList.iterator();
    }


    public void setSelectedChild(Child newSelectedChild) {
        selectedChild = newSelectedChild;
    }

}
