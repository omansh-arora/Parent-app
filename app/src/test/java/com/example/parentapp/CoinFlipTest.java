package com.example.parentapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CoinFlipTest {
    @Test
    public void flipsRandomUniformDistribution() {
        CoinFlipManager manager = CoinFlipManager.getInstance();
        for (int i = 0; i < 100; i++) {
            System.out.println(manager.flip(null, null));

        }
        assertTrue(40 < manager.getFlips().stream().filter(x -> x.side == CoinFlip.Side.HEAD).count());
        assertTrue(40 < manager.getFlips().stream().filter(x -> x.side == CoinFlip.Side.TAIL).count());
    }
}