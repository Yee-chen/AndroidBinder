package cn.edu.zstu.bindservicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.edu.zstu.bindservicedemo.activity.AIDLDemoActivity;
import cn.edu.zstu.bindservicedemo.activity.BindActivity;
import cn.edu.zstu.bindservicedemo.activity.MessengerActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mBindAcBtn, mMeaaengerAcBtn, mAIDLBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void initUI() {
        mBindAcBtn = (Button) findViewById(R.id.bind_activity);
        mMeaaengerAcBtn = (Button) findViewById(R.id.messenger_activity);
        mAIDLBtn = (Button) findViewById(R.id.aidl_activity);

        mBindAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BindActivity.class));
            }
        });

        mMeaaengerAcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MessengerActivity.class));
            }
        });

        mAIDLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AIDLDemoActivity.class));
            }
        });
    }
}
