package com.example.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentapp.R;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TossHistoryActivity extends AppCompatActivity {

    private CoinFlipManager coinFlipManager;
    private List<CoinFlip> gamesList;
    private static final String PREFS_NAME = "CoinPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toss_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("View All Flips");

        coinFlipManager = CoinFlipManager.getInstance();

        //Get CoinFlip Instance
        if(CoinFlipActivity.getCoinManager(this)!=null)
            coinFlipManager = getCoinManager(this);

        gamesList = coinFlipManager.getFlips();

        populateListView();
    }

    protected void onStart(){
        coinFlipManager = CoinFlipManager.getInstance();

        //Get CoinFlip Instance
        if(getCoinManager(this)!=null)
            coinFlipManager = getCoinManager(this);
        saveCoinFlipManager(coinFlipManager);
        populateListView();
        super.onStart();
    }

    private void populateListView() {

        //build adapter
        ArrayAdapter<CoinFlip> adapter = new MyListAdapter();

        //configure the list view
        ListView list = (ListView) findViewById(R.id.listAllGames);
        list.setAdapter(adapter);

    }

    private class MyListAdapter extends ArrayAdapter<CoinFlip> {
        public MyListAdapter() {
            super(TossHistoryActivity.this, R.layout.item_view, gamesList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // need to check if view is null or not before use it
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //find the toss game to work with
            CoinFlip currentFlip = gamesList.get(position);

            //fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            Integer imgResource = currentFlip.getGameResult().equals("Win") ? R.drawable.ic_baseline_happy_35 : R.drawable.ic_baseline_mood_bad_35_pink;
            imageView.setImageResource(imgResource);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String output =  "["+ currentFlip.getGameResult()+"] " + currentFlip.getChildName() + " chose " + (currentFlip.getChildPicked() == 0 ? "HEAD" : "TAIL") + "; Actual side: " + (currentFlip.getTossResult() == 0 ? "HEAD" : "TAIL") + " @" + currentFlip.getGameCreatedDate();
            outputTV.setText(output);

            return itemView;
        }
    }

    private void saveCoinFlipManager(CoinFlipManager cfm) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cfm);
        editor.putString("CoinManager", json);
        editor.commit();
    }

    static public CoinFlipManager getCoinManager(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("CoinManager", "");
        CoinFlipManager cfm = gson.fromJson(json, CoinFlipManager.class);
        return cfm;
    }

}