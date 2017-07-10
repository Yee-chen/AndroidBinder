package cn.edu.zstu.bindservicedemo.bindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PlayService extends Service {
    private static final String TAG = "PlayService";
    private boolean mPlayMusic = false;
    private MyBinder mMyBinder = new MyBinder();

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    public void playMusic() {
        mPlayMusic = true;
        Toast.makeText(getApplication(), "Music start play!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "[playMusic()]: Music start play!");
    }

    public void stopMusic() {
        mPlayMusic = false;
        Toast.makeText(getApplication(), "Music stop play!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "[stopMusic()]: Music stop play!");
    }

    public class MyBinder extends Binder {
        public PlayService getPlayService() {
            return PlayService.this;
        }

        public boolean isPlaying() {
            return mPlayMusic;
        }
    }
}
