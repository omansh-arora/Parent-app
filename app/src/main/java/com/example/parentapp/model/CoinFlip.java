package com.example.parentapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class CoinFlip {

    public static enum Side {
        HEAD,
        TAIL,
    }

//    public final Side side;
    //public final LocalDateTime dateTime = LocalDateTime.now();
//    public final Child picker;
//    public final Side guess;
    //public final boolean isWin;


    /******* Tem *************/
    private String childName;
    private Integer childPicked;
    private Integer tossResult;
    /******* Tem *************/

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public CoinFlip(Side side, Child child, CoinFlip.Side guess) {
//        this.side = side;
//        this.picker = child;
//        this.guess = guess;
//        this.isWin = this.guess == this.side;
//    }

    //String childName, Integer childPicked, Integer childPicked
    public CoinFlip(String childName, Integer childPicked, Integer tossResult) {
        this.childName = childName;
        this.childPicked = childPicked;
        this.tossResult = tossResult;
//        this.isWin = this.childPicked == this.tossResult;
    }

    @Override
    public String toString() {
        String output = "";
        assert childName != null;
        return (childPicked == tossResult ? "[WIN] " : "[LOSE] ") + childName + " chose " + (childPicked == 0 ? "HEAD" : "TAIL") + "; actual side: " +  (tossResult == 0 ? "HEAD" : "TAIL");
    }

}
