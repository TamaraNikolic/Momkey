package com.momkey.alen.momkey.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.activity.AffirmationActivity;
import com.momkey.alen.momkey.activity.MainActivity;
import com.momkey.alen.momkey.activity.UseActivity;
import com.momkey.alen.momkey.adapter.RecordAdapter;
import com.momkey.alen.momkey.data.RecordList;

import java.io.IOException;

/**
 * Created by Alen on 8/3/2016.
 */
public class ListenFragment extends Fragment {

    private ListView mListView;
    private SeekBar mSbPlay;
    private Button mbPlay, mbNext, mbBack;
    public static boolean playing = false;
    private TextView tvTimeEnd;
    private TextView tvTimeStart;

    View view=null;

    public static MediaPlayer mediaPlayer;
    private long startTime = 0;
    private long currentTime = 0;
    private Handler myHandler = new Handler();;

    private int playingItem;
    private static String data;

    private final int sampleRate = 8000;
    private final int numSamples = 1 * sampleRate;
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 20; // hz



    public static RecordAdapter mRecordAdapter;
    public static RecordList mRecordList = new RecordList();
    private TextView tvAffiramtion;
    private TextView tvUse;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());
        if(playing==true) {
            mediaPlayer.pause();
            currentTime= mediaPlayer.getCurrentPosition();
            mbPlay.setBackground(getResources().getDrawable(R.drawable.play));
            playing = false;
            mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay =false;
            mRecordAdapter.notifyDataSetChanged();
            play(data);
        }
    }

    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
    view = inflater.inflate(R.layout.listen_fragment, viewGroup);

        mSbPlay = (SeekBar) view.findViewById(R.id.seekBarDuration);
        mbBack = (Button) view.findViewById(R.id.buttonBack);
        mbPlay = (Button) view.findViewById(R.id.buttonPlay);
        mbNext = (Button) view.findViewById(R.id.buttonNext);
        tvTimeEnd=(TextView) view.findViewById(R.id.textViewTimeEnd);
        tvTimeStart=(TextView) view.findViewById(R.id.textViewStart);

        tvUse=(TextView) view.findViewById(R.id.buttonHowYoUse);
        tvAffiramtion=(TextView) view.findViewById(R.id.textViewAff);

        mRecordList.setmList(MainActivity.mDatabase.getList_Id());
        mListView = (ListView) view.findViewById(R.id.listView);
        mRecordAdapter = new RecordAdapter(getContext(), R.layout.record_preview_layout, mRecordList.getmList());
        mListView.setAdapter(mRecordAdapter);
        tvUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().startActivity(new Intent(getActivity(), UseActivity.class));

            }
        });
        tvAffiramtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().startActivity(new Intent(getActivity(), AffirmationActivity.class));

            }
        });

        mbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null) {
                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mbNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playing = false;
                    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay = false;
                    playingItem++;
                    if (playingItem >= mRecordList.getmList().size()) {
                        playingItem = 0;
                    }
                    String data = mRecordList.getmList().get(playingItem).getPath();
                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playing = false;
                    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay = false;
                    playingItem--;

                    if (playingItem < 0) {
                        playingItem = MainActivity.mDatabase.getList_Id().size() - 1;
                    }
                    String data = mRecordList.getmList().get(playingItem).getPath();

                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }

            }


        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int possition, long l) {

                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay =false;

                }
                playing = false;
                playingItem = possition;
                data = mRecordList.getmList().get(possition).getPath();
                //    mListView.getChildAt(possition).findViewById(R.id.relativeLayoutUpdata).setVisibility(View.GONE);

                play(data);



            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        populateViewForOrientation(inflater, (ViewGroup) getView());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//INIT COMPONENTS
         view = inflater.inflate(R.layout.listen_fragment, container, false);

        mSbPlay = (SeekBar) view.findViewById(R.id.seekBarDuration);
        mbBack = (Button) view.findViewById(R.id.buttonBack);
        mbPlay = (Button) view.findViewById(R.id.buttonPlay);
        mbNext = (Button) view.findViewById(R.id.buttonNext);
        tvTimeEnd=(TextView) view.findViewById(R.id.textViewTimeEnd);
        tvTimeStart=(TextView) view.findViewById(R.id.textViewStart);

        tvUse=(TextView) view.findViewById(R.id.buttonHowYoUse);
        tvAffiramtion=(TextView) view.findViewById(R.id.textViewAff);

        mRecordList.setmList(MainActivity.mDatabase.getList_Id());
        mListView = (ListView) view.findViewById(R.id.listView);
        mRecordAdapter = new RecordAdapter(getContext(), R.layout.record_preview_layout, mRecordList.getmList());
        mListView.setAdapter(mRecordAdapter);


//ADD LISTENERS
            tvUse.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        getActivity().startActivity(new Intent(getActivity(), UseActivity.class));// start new acitivity

                }
             });
        tvAffiramtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().startActivity(new Intent(getActivity(), AffirmationActivity.class));

            }
        });
// play record
        mbPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null) {
                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }
            }
        });
// nexrt record
        mbNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playing = false;
                    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay = false;
                    playingItem++;
                    if (playingItem >= mRecordList.getmList().size()) {
                        playingItem = 0;// if playing record is last play first
                    }
                    String data = mRecordList.getmList().get(playingItem).getPath();
                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // previous record
        mbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!=null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playing = false;
                    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay = false;
                    playingItem--;

                    if (playingItem < 0) {
                        playingItem = MainActivity.mDatabase.getList_Id().size() - 1;// if playing record is first play last
                    }
                    String data = mRecordList.getmList().get(playingItem).getPath();

                    play(data);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Pick song",Toast.LENGTH_SHORT).show();
                }

            }


        });

        // click on record in list view play dat record and set changes on UI
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int possition, long l) {

if(mediaPlayer!=null) {
    mediaPlayer.stop();
    mediaPlayer.release();
    mediaPlayer = null;
    mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay =false;

}
                playing = false;
                playingItem = possition;
                data = mRecordList.getmList().get(possition).getPath();
            //    mListView.getChildAt(possition).findViewById(R.id.relativeLayoutUpdata).setVisibility(View.GONE);

               play(data);



            }
        });

        return view;
    }



//update song time in new thread and send changes on main thread
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tvTimeStart.setText(convertTime(startTime));
            mSbPlay.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

//conver time into String
public String convertTime(long time){
    String timeString=String.format("%02d:%02d ",

            java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes((long) time),
            java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds((long) time) -
                    java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.
                            toMinutes((long) time)));

    return  timeString;
}

    public void play(String path) {


        if (!playing) {

            setNotification("Playing "+mRecordList.getmList().get(playingItem).getName());
            mediaPlayer = new MediaPlayer();
            mbPlay.setBackground(getResources().getDrawable(R.drawable.pause));
            mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay =true;// set playing filed true for specific record
            mRecordAdapter.notifyDataSetChanged();

            playing = true;
            try {
                mediaPlayer.setDataSource(path);// set playing record path
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);// playing record will repeat
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.seekTo((int)currentTime);
            mSbPlay.setProgress((int)currentTime);
            mediaPlayer.start();// start playing

            mSbPlay.setMax(mediaPlayer.getDuration());
            mSbPlay.setProgress((int) startTime);// form runnable

            tvTimeEnd.setText(convertTime(mediaPlayer.getDuration()));
            myHandler.postDelayed(UpdateSongTime, 100);

        } else {
            mediaPlayer.pause();// pause playing on specific time
           currentTime= mediaPlayer.getCurrentPosition();
            mbPlay.setBackground(getResources().getDrawable(R.drawable.play));
            playing = false;
            mRecordList.getmList().get(playingItem).isUpdateLayoutVisiblePlay =false;
            mRecordAdapter.notifyDataSetChanged();

        }
    }

    public  void setNotification( String action){

       Intent intent= new Intent(getActivity().getApplicationContext(),MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
               intent, 0);
        Notification.Builder notificationBuilder=new Notification.Builder(getActivity().getApplicationContext()).setTicker(
                "Momkey "+ action).setSmallIcon(R.mipmap.icon).setAutoCancel(true)
                .setContentText(action).setContentInfo(""+1)
                .setContentTitle("MOMKEY")
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = notificationBuilder.build();

        n.defaults = Notification.FLAG_AUTO_CANCEL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(1111,notificationBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
// remove hendler
        myHandler.removeCallbacks(UpdateSongTime);
    }

}


