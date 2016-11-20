package com.momkey.alen.momkey.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.View.VisualizerView;
import com.momkey.alen.momkey.activity.MainActivity;
import com.momkey.alen.momkey.data.Record;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RecordFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS = 20;


    private FloatingActionButton mFabRecord;
    private TextView mTvState;
    private TextView mTvSoundName;
    private ImageView mIvSave;
    private ImageView mivDelete;
    private TextView mTvRecordingTime;

    private long startTime = 0;
    private long currentTime = 0;
    public static Handler myHandler = new Handler();;
    public static final int REPEAT_INTERVAL = 40;

    public static Handler handler;
    VisualizerView visualizerView;

    private MediaRecorder myAudioRecorder;
    private static final String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String outputFile = null;
    private boolean recording = false;
    private long time;
    private long count;
    String path;
    long end;
    public static String stringFileName;
    private int left;
    private boolean startrec=false;
    View view = null;

    long start = 0;
    private TextView mTvStorage;



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // INIT COMPONENTS
        view = inflater.inflate(R.layout.fragment_record, container, false);
        mFabRecord = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonRecord);
        mFabRecord.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.floating_button_back)));
        mTvState = (TextView) view.findViewById(R.id.textViewState);
        mTvSoundName = (TextView) view.findViewById(R.id.textViewSoundName);
        mTvStorage = (TextView) view.findViewById(R.id.textViewStorage);
        mIvSave = (ImageView) view.findViewById(R.id.imageViewaSave);
        mivDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
        mTvRecordingTime = (TextView) view.findViewById(R.id.textViewRecordingTime);

        visualizerView = (VisualizerView) view.findViewById(R.id.visualizer);
        handler = new Handler();

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;
        left = (int) ((megAvailable * 60) / 9000);

        mTvStorage.setText("" + left + "h left on storage");

        if (MainActivity.mDatabase.getList_Id().size() > 0) {
            count = MainActivity.mDatabase.getList_Id().get(MainActivity.mDatabase.getList_Id().size() - 1).getId()+1;
        } else {
            count = 1;
        }

        mTvSoundName.setText(getResources().getString(R.string.recording_title) + count + ".wav");
        // LISTNERES


        //start recording
        mFabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTvSoundName.setText(getResources().getString(R.string.recording_title) + count + ".wav");
                if (!recording) {
                    if (MainActivity.mDatabase.getList_Id().size() > 0) {
                        count = MainActivity.mDatabase.getList_Id().get(MainActivity.mDatabase.getList_Id().size() - 1).getId()+1;
                    } else {
                        count = 1;
                    }
                    //start time
                    start = new Date().getTime();
                    recording = true;
                    visualizerView.setVisibility(View.VISIBLE);// visualiser of frequnecy
                    mTvRecordingTime.setVisibility(View.VISIBLE);

                    stringFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    path = storage + "/" + stringFileName + ".wav";// path in storage
                    myAudioRecorder = new MediaRecorder();// start media recorder
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(path);


                    try {
                        myAudioRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setNotification("Recording",R.drawable.record);// set notification
                    myAudioRecorder.start();
                    handler.post(updateVisualizer);// send effects to UI
                    myHandler.postDelayed(UpdateSongTime, 100);

                    mFabRecord.setImageResource(R.drawable.floating_pause);
                    mTvState.setText(getResources().getString(R.string.is_recording));



                } else {
                    end = new Date().getTime();// end time
                    recording = false;
                    setNotification("Stop recording",R.drawable.floating_pause);
                    handler.removeCallbacks(updateVisualizer);
                    visualizerView.clear();
                    myHandler.removeCallbacks(UpdateSongTime);
                    mFabRecord.setImageResource(R.drawable.record);
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder=null;
                    mIvSave.setVisibility(View.VISIBLE);
                    mivDelete.setVisibility(View.VISIBLE);
                    mFabRecord.setClickable(false);// clickable false because user has to pick save or discard when he do that record will be clickable again

                }


            }


        });



        // add new file into list and database
        mIvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = end - start;
                if (time > 0) {
                    double size = time * 2.5 / 60000;
                    String str = String.format("%1.2f", size);
                    String name = getString(R.string.song_name_dafault) + count + ".wav";
                    String timeStamp = new SimpleDateFormat("MMMM dd, HH:mm").format(new Date());
                    Record record = new Record(name, path, timeStamp, "" + str + "MB", time);
                    ListenFragment.mRecordList.getmList().add(record);
                    MainActivity.mDatabase.insertRecord(record);
                    ListenFragment.mRecordAdapter.notifyDataSetChanged();
                    setNotification("Saved",R.drawable.save_not);
                    visualizerView.setVisibility(View.INVISIBLE);
                    if (MainActivity.mDatabase.getList_Id().size() > 0) {
                        count = MainActivity.mDatabase.getList_Id().get(MainActivity.mDatabase.getList_Id().size() - 1).getId()+1;
                    } else {
                        count = 1;
                    }
                    mTvSoundName.setText(getResources().getString(R.string.recording_title) + count + ".wav");
                    mTvState.setText(getResources().getString(R.string.no_record));
                    mIvSave.setVisibility(View.GONE);
                    mivDelete.setVisibility(View.GONE);
                    visualizerView.setVisibility(View.INVISIBLE);
                    mTvRecordingTime.setVisibility(View.INVISIBLE);
                    mFabRecord.setClickable(true);

                }
            }
        });
        //delete file
        mivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path);
                file.delete();
                setNotification("Delete",R.drawable.discard);
                mIvSave.setVisibility(View.GONE);
                mivDelete.setVisibility(View.GONE);
                visualizerView.setVisibility(View.INVISIBLE);
                mTvRecordingTime.setVisibility(View.INVISIBLE);
                mFabRecord.setClickable(true);
            }
        });


        return view;
    }
// set visible on UI visualiser effects
    Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (recording) // if we are already recording
            {
                // get the current amplitude

                int x = myAudioRecorder.getMaxAmplitude();
                visualizerView.addAmplitude(x); // update the VisualizeView
                visualizerView.invalidate(); // refresh the VisualizerView

                // update in 40 milliseconds
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };


//convert time into String
    public String convertTime(long time) {
        String timeString = String.format("%02d:%02d ",

                java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes((long) time),
                java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds((long) time) -
                        java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.
                                toMinutes((long) time)));

        return timeString;
    }
    // send song time inot Main Thread every 100 miliseconds
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = new Date().getTime()-start;
            mTvRecordingTime.setText(convertTime(startTime));
            myHandler.postDelayed(this, 100);
        }
    };

    //set notifications for applications acitons
    public  void setNotification( String action, int icon){
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
                new Intent(getActivity().getApplicationContext(),MainActivity.class), 0);
        Notification.Builder notificationBuilder=new Notification.Builder(getActivity().getApplicationContext()).setTicker(
                "Momkey "+ action).setSmallIcon(icon).setAutoCancel(true)
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
}











































































































































































































































