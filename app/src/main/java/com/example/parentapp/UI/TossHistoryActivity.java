package com.example.parentapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import com.example.parentapp.R;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TossHistoryActivity extends AppCompatActivity {

    private CoinFlipManager coinFlipManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toss_history);

        //Get GameManager Instance
        coinFlipManager = CoinFlipManager.getInstance();

        populateListView();
    }

    private void populateListView() {
        List<CoinFlip> gamesList = coinFlipManager.getFlips();

        //build adapter
        ArrayAdapter<CoinFlip> adapter = new ArrayAdapter<CoinFlip>(
                this,
                R.layout.game_item, //Layout to use (create)
                gamesList); //Items to be displayed

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listAllGames);
        list.setAdapter(adapter);

    }


}