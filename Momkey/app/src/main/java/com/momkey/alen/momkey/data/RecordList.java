package com.momkey.alen.momkey.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tamara on 8/3/2016.
 */

// class which save ArrayList of records

public class RecordList implements Serializable {
    private ArrayList<Record> mList=new ArrayList<>();

    public ArrayList<Record> getmList() {
        return mList;
    }

    public void setmList(ArrayList<Record> mList) {
        this.mList = mList;
    }
}
