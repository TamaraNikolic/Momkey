package com.momkey.alen.momkey.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.momkey.alen.momkey.R;
import com.momkey.alen.momkey.adapter.PagerAdapter;
import com.momkey.alen.momkey.adapter.Tablet2Adapter;
import com.momkey.alen.momkey.adapter.TabletAdapter;
import com.momkey.alen.momkey.data.Database;
import com.momkey.alen.momkey.fragment.ListenFragment;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private ViewPager viewPager2;
    private ViewPager viewPager3;
    private PagerAdapter adapter;
    private TabLayout tabLayout;

    private ImageView mImageChange;

    private DrawerLayout mDrawerLayout;

    public static Database mDatabase;
    private TabletAdapter adapter2;
    private Tablet2Adapter adapter3;

    // sceen orientation not destroy activity
    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {

        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        addListeners();


    }

    private void addListeners() {

        //change fragment on scroll and on click in tabLayout
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.getItem(tab.getPosition()).onAttach(getApplicationContext());

                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    mImageChange.setImageResource(R.drawable.list);
                    mImageChange.setClickable(false);
                } else if (tab.getPosition() == 1) {
                    mImageChange.setImageResource(R.drawable.pen);
                    mImageChange.setClickable(true);

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                adapter.getItem(tab.getPosition()).onAttach(getApplicationContext());

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initComponents() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerRoot);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Listen"));
        tabLayout.addTab(tabLayout.newTab().setText("Record"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mImageChange = (ImageView) findViewById(R.id.imageViewChange);



        mDatabase = new Database(getApplicationContext());

        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        viewPager2 = (ViewPager) findViewById(R.id.pager2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager3 = (ViewPager) findViewById(R.id.pager3);

        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        //for tablets
        adapter2 = new TabletAdapter
                (getSupportFragmentManager());

        adapter3 = new Tablet2Adapter
                (getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        //for tablets
        viewPager2.setAdapter(adapter2);
        viewPager3.setAdapter(adapter3);
    }


    public  void setNotification( String action, int icon){
        //notification which shows current action in app

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(),MainActivity.class), 0);
        Notification.Builder notificationBuilder=new Notification.Builder(getApplicationContext()).setTicker(
                "Momkey "+ action).setSmallIcon(icon).setAutoCancel(true)
                .setContentText(action).setContentInfo(""+1)
                .setContentTitle("MOMKEY")
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = notificationBuilder.build();

        n.defaults = Notification.FLAG_AUTO_CANCEL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(1111,notificationBuilder.build());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //close databse and musicPlayer
        mDatabase.closeDataBase();
        if(ListenFragment.mediaPlayer!=null){
            ListenFragment.mediaPlayer.stop();
            ListenFragment.mediaPlayer.release();
            ListenFragment.mediaPlayer=null;
        }

        //notification for come back
 setNotification("Come back",R.drawable.comeback);

    }

}