package com.momkey.alen.momkey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.momkey.alen.momkey.R;

/**
 * Created by Alen on 8/14/2016.
 */
public class UseAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mList;
    private LayoutInflater mLayoutInflater;


// adapter for use explanation
    public UseAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mList = mContext.getResources().getStringArray(R.array.settings_array);
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


        row = mLayoutInflater.inflate(R.layout.momkey_item,parent,false);

        TextView tvTitle = (TextView) row.findViewById(R.id.textViewTitle);
        TextView tvDesce = (TextView) row.findViewById(R.id.textViewDec);

// set from list
        tvTitle.setText(getItem(position).toString());
// set description
        switch (position) {
            case 0:
                tvDesce.setText(R.string.monkey_special);

                break;
            case 1:
                tvDesce.setText(R.string.monkey_benefits);
                break;
            case 2:
                tvDesce.setText(R.string.monkey_record);

                break;
            case 3:
                tvDesce.setVisibility(View.GONE);
            case 4:
                tvDesce.setVisibility(View.GONE);
            case 5:
                tvDesce.setVisibility(View.GONE);
                break;

        }


        return row;
    }
}




