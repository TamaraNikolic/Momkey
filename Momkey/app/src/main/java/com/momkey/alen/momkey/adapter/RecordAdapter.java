package com.momkey.alen.momkey.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.activity.MainActivity;
import com.momkey.alen.momkey.data.Record;
import com.momkey.alen.momkey.fragment.ListenFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alen on 8/3/2016.
 */
public class RecordAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener{
    private Context mContext;
    private int mResources;
    private ArrayList<Record> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    public static int delete=0;

//constructor
    public RecordAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);
        mContext = context;
        mList = objects;
        mResources = resource;
        this.mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View row = convertView;
        HolderClass holder = null;

        if (row == null) {
            row = mInflater.inflate(mResources, parent, false);
            holder = new HolderClass();

            holder.title = (TextView) row.findViewById(R.id.textViewAdapter);
            holder.date=(TextView) row.findViewById(R.id.textViewDatum);
            holder.time=(TextView) row.findViewById(R.id.textViewTime);
            holder.size=(TextView) row.findViewById(R.id.textViewSize);
            holder.ivOpen=(ImageView)row.findViewById(R.id.imageViewOpen);
            holder.update=(RelativeLayout)row.findViewById(R.id.relativeLayoutUpdata);
            holder.icDelete=(ImageView)row.findViewById(R.id.imageViewDelete);
            holder.ivUpdate=(ImageView)row.findViewById(R.id.imageViewUpadte);
            holder.ivPlay=(ImageView)row.findViewById(R.id.imageViewPlay);

            holder.ivOpen.setTag(holder.update);

            row.setTag(mResources, holder);
        } else {
            holder = (HolderClass) row.getTag(mResources);
        }

        final Record dataItem = (Record) getItem(position);

        String timeString=String.format("%02d:%02d ",

                java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes((long) dataItem.getTime()),
                java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds((long) dataItem.getTime()) -
                        java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.
                                toMinutes((long) dataItem.getTime())));

        holder.title.setText(dataItem.getName());
        holder.date.setText(dataItem.getDate());
        holder.time.setText(timeString);
        holder.size.setText(dataItem.getSize());

// for scpecific data set visibility (not be visible for recycler data)
        if(dataItem.isUpdateLayoutVisible){
            holder.update.setVisibility(View.VISIBLE);
        }
        else {
            holder.update.setVisibility(View.GONE);
        }


        holder.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


                // Setting Dialog Title
                alertDialog.setTitle("NAME");


                alertDialog.setMessage("Change name");
                final EditText input = new EditText(mContext);
                alertDialog.setView(input);


                alertDialog.setIcon(R.drawable.update);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dataItem.setName(input.getText().toString()+".wav");
                                ListenFragment.mRecordAdapter.notifyDataSetChanged();
                                MainActivity.mDatabase.updatedetails(dataItem.getId(),input.getText().toString()+".wav");
                                dataItem.isUpdateLayoutVisible =false;
                                Toast.makeText(mContext, "Rename", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
            }

        });

        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        mContext);

                alertDialog2.setTitle("Confirm Delete");

                alertDialog2.setMessage("Are you sure you want delete this file?");

                alertDialog2.setIcon(R.drawable.deleete);

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // delete from list and from database
                                MainActivity.mDatabase.delete(dataItem);
                                ListenFragment.mRecordList.getmList().remove(dataItem);
                                delete++;
                                File file = new File(dataItem.getPath());
                                file.delete();
                                ListenFragment.mRecordAdapter.notifyDataSetChanged();
                                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT)
                                        .show();
                                dataItem.isUpdateLayoutVisible =false;
                            }
                        });


                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();

                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();
            }
        });

        holder.ivOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataItem.isUpdateLayoutVisible){// for specific item set visibility
                    ((View)view.getTag()).setVisibility(View.GONE);
                    dataItem.isUpdateLayoutVisible =false;

                }
                else {
                    ((View)view.getTag()).setVisibility(View.VISIBLE);
                    dataItem.isUpdateLayoutVisible =true;
                }

            }
        });
        if(dataItem.isUpdateLayoutVisiblePlay){// if specific item is visible set imageView play visible
        holder.ivPlay.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivPlay.setVisibility(View.GONE);
        }
        return row;


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class HolderClass {
        private TextView title;
        private TextView date;
        TextView time;
        TextView size;
        RelativeLayout update;
        ImageView ivUpdate;
        ImageView icDelete;
        ImageView ivOpen;
        ImageView ivPlay;

    }


}