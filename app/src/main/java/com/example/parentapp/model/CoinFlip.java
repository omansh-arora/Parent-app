package com.example.parentapp.model;

import android.annotation.TargetApi;
import android.os.Build;

import java.time.*;
import java.time.format.*;

public class CoinFlip {

    private String childName;
    private Integer childPicked;
    private Integer tossResult;
    private String gameCreatedDate;

    public CoinFlip(String childName, Integer childPicked, Integer tossResult) {
        this.childName = childName;
        this.childPicked = childPicked;
        this.tossResult = tossResult;
        this.gameCreatedDate = getFlipCreatedDate();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public String getFlipCreatedDate() {
        /*
        DataTimeFormat function token from:
        https://developer.android.com/reference/java/time/format/DateTimeFormatter
        */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime datetime = LocalDateTime.now();
        String output = formatter.format(datetime);
        return output;
    }

    @Override
    public String toString() {
        assert childName != null;
        return (childPicked == tossResult ? "[WIN] " : "[LOSE] ") + childName + " chose " + (childPicked == 0 ? "HEAD" : "TAIL") + "; Actual side: " + (tossResult == 0 ? "HEAD" : "TAIL") + " @" + gameCreatedDate;
    }

}
