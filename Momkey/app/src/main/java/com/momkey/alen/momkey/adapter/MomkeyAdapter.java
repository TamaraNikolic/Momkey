package com.momkey.alen.momkey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.momkey.alen.momkey.R;

/**
 * Created by Tamara on 8/14/2016.
 */
public class MomkeyAdapter extends BaseAdapter {


    private Context mContext;
    private String[] mList;
    private LayoutInflater mLayoutInflater;



    public MomkeyAdapter(Context context) {
      //adapter constructor
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mList = mContext.getResources().getStringArray(R.array.affir_array);
    }


    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;

    }
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        View row;

// View components in Adapter

        row = mLayoutInflater.inflate(R.layout.momkey_item,parent,false);
        TextView tvTitle = (TextView) row.findViewById(R.id.textViewTitle);
        TextView tvDesce = (TextView) row.findViewById(R.id.textViewDec);


        tvTitle.setText(getItem(position).toString());

//set text for descriptions
        switch (position) {
            case 0: tvDesce.setText(R.string.affirmation);
                tvDesce.setTextColor(Color.DKGRAY);
                tvTitle.setVisibility(View.GONE);
                break;
            case 1:
                tvDesce.setText(R.string.affirmation_money);

                break;
            case 2:
                tvDesce.setText(R.string.affirmation_shiness);
                break;
            case 3:
                tvDesce.setText(R.string.affirmation_despession);

                break;

        }


        return row;
    }
}




