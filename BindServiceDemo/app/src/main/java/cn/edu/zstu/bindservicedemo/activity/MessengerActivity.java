package cn.edu.zstu.bindservicedemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.edu.zstu.bindservicedemo.R;
import cn.edu.zstu.bindservicedemo.messenger.MessengerService;

public class MessengerActivity extends AppCompatActivity {
    private static final String TAG = "MessengerActivity";
    private Messenger mService = null;
    private boolean mBound = false;
    private Button mSayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        mSayBtn = (Button) findViewById(R.id.sayBtn);
        mSayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processSayHello();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound){
            Intent intent = new Intent(this, MessengerService.class);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound){
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    public void processSayHello() {
        Log.d(TAG, "[processSayHello()]");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msg", "Hello, Service!");
//        message.what = MessengerService.MSG_SAY_HELLO;
        message.setData(bundle);
        message.replyTo = reply;
        try {
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "[onServiceConnected()]");
            if (iBinder == null)
                Log.e(TAG, "[onServiceConnected()]: iBinder == null");
            mService = new Messenger(iBinder);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            mBound = false;
        }
    };

    private Messenger reply = new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) { //用于处理服务端返回的数据
            Log.d(TAG, "[handleMessage()]: 这是来自服务端的回信" + msg.getData().getString("msg"));
            Toast.makeText(MessengerActivity.this, "这是来自服务端的回信\n" + msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
        }
    });
}
