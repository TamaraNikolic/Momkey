package com.momkey.alen.momkey.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.adapter.UseAdapter;

public class UseActivity extends AppCompatActivity {

    // Activity with explanation how to use it

    private ListView mListView;
    private UseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);


        mListView=(ListView)findViewById(R.id.listView);
        mAdapter=new UseAdapter(getApplicationContext());

        mListView.setAdapter(mAdapter);


    }
}
