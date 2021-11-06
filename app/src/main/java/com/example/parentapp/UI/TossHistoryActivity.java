package com.example.parentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentapp.R;
import com.example.parentapp.model.CoinFlip;
import com.example.parentapp.model.CoinFlipManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toss_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("View All Flips");

        //Get GameManager Instance
        coinFlipManager = CoinFlipManager.getInstance();
        gamesList = coinFlipManager.getFlips();

        populateListView();
    }

    private void populateListView() {

        //build adapter
//        ArrayAdapter<CoinFlip> adapter = new ArrayAdapter<CoinFlip>(
//                this,
//                R.layout.game_item, //Layout to use (create)
//                gamesList); //Items to be displayed
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
            int imgResource = currentFlip.getGameResult() == "Win" ? R.drawable.ic_baseline_happy_35 : R.drawable.ic_baseline_mood_bad_35_pink;
            imageView.setImageResource(imgResource);

            // build output String
            TextView outputTV = (TextView) itemView.findViewById(R.id.item_txt);
            String output =  "["+ currentFlip.getGameResult()+"] " + currentFlip.getChildName() + " chose " + (currentFlip.getChildPicked() == 0 ? "HEAD" : "TAIL") + "; Actual side: " + (currentFlip.getTossResult() == 0 ? "HEAD" : "TAIL") + " @" + currentFlip.getGameCreatedDate();
            outputTV.setText(output);

            return itemView;
        }
    }


}