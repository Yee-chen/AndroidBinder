package cn.edu.zstu.bindservicedemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.zstu.bindservicedemo.R;
import cn.edu.zstu.bindservicedemo.bindservice.PlayService;

public class BindActivity extends AppCompatActivity {
    private static final String TAG = "BindActivity";
    private PlayService.MyBinder mMyBinder;
    private PlayService mPlayService;
    private TextView mStatusTV;
    private Button mStartBtn;
    private Button mStopBtn;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mBound) {
            Intent intent = new Intent(this, PlayService.class);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initUI() {
        mStatusTV = (TextView) findViewById(R.id.statusTv);
        mStartBtn = (Button) findViewById(R.id.start_play);
        mStopBtn = (Button) findViewById(R.id.stop_play);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processStartPlay();
                    }
                });
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processStopPlay();
                    }
                });
            }
        });
    }

    public void processStartPlay() {
        mStatusTV.setText("正在播放");
        if (!mMyBinder.isPlaying()) {
            mPlayService.playMusic();
        } else {
            Log.d(TAG, "[processStartPlay()]: Already play!");
            Toast.makeText(this, "已经在播放", Toast.LENGTH_SHORT).show();
        }
    }

    public void processStopPlay() {
        mStatusTV.setText("已停止播放");
        if (mMyBinder.isPlaying()) {
            mPlayService.stopMusic();
        } else {
            Log.d(TAG, "[processStartPlay()]: Already stop!");
            Toast.makeText(this, "已经停止", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBound = true;
            Log.d(TAG, "[onServiceConnected()]: Connect server!");
            mMyBinder = (PlayService.MyBinder) iBinder;
            mPlayService = mMyBinder.getPlayService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "[onServiceConnected()]: Disconnect server!");
            mBound = false;

            if (mMyBinder != null)
                mMyBinder = null;

            if (mPlayService != null)
                mPlayService = null;
        }
    };
}
