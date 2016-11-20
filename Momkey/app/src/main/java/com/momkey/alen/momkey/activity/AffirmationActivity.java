package com.momkey.alen.momkey.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.adapter.MomkeyAdapter;

public class AffirmationActivity extends AppCompatActivity {


    private ListView mListView;
    private MomkeyAdapter mMomkeyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirmation);

        mListView=(ListView)findViewById(R.id.listView);
        mMomkeyAdapter=new MomkeyAdapter(getApplicationContext());

        mListView.setAdapter(mMomkeyAdapter);

    }
}
