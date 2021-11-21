package com.example.parentapp.model;

import java.util.Iterator;
import java.util.List;

/**
 * As a parent...
 * <p>
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
public class CoinFlipHistory {

    public List<CoinFlip> history;
    private String taskName;

    public CoinFlipHistory(String taskName) {
        this.taskName = taskName;
        history = LocalStorage.getInstance().getHistory(taskName);
    }

    public void addFlipRecord(CoinFlip coinFlip) {
        history.add(coinFlip);
        LocalStorage.getInstance().saveHistory(taskName, history);
    }

    public List<CoinFlip> getFlips() {
        return history;
    }

}
