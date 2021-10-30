package com.example.parentapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)
public final class CoinFlip {

    public static enum Side {
        HEAD,
        TAIL,
    }

    public final Side side;
    public final LocalDateTime dateTime = LocalDateTime.now();
    public final Child picker;
    public final Side guess;
    public final boolean isWin;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CoinFlip(Side side, Child child, CoinFlip.Side guess) {
        this.side = side;
        this.picker = child;
        this.guess = guess;
        this.isWin = this.guess == this.side;
    }
}
