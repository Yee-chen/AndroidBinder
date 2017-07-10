package cn.edu.zstu.bindservicedemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.zstu.aidlservicedemo.BookManager;
import cn.edu.zstu.aidlservicedemo.bean.Book;
import cn.edu.zstu.bindservicedemo.R;

public class AIDLDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AIDLDemoActivity";
    private TextView mTextView, mBookCount, mBookInfo, mBoookInfoView;
    private BookManager mBookManager;

    // 标志当前与服务端的连接状况，false为未连接，true为已连接
    private boolean mBound = false;
    // Book对象的list
    private List<Book> mBooks;

    private int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidldemo);

        initUI();
    }

    private void initUI() {
        mTextView = (TextView) findViewById(R.id.textview);
        mBookCount = (TextView) findViewById(R.id.books);
        mBookInfo = (TextView) findViewById(R.id.info);
        mBoookInfoView = (TextView) findViewById(R.id.infoView);
        mTextView.setOnClickListener(AIDLDemoActivity.this);
        mBookCount.setOnClickListener(AIDLDemoActivity.this);
        mBookInfo.setOnClickListener(AIDLDemoActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview:
                processAddBook();
                break;
            case R.id.books:
                processGetBookCount();
                break;
            case R.id.info:
                processGetBooks();
                break;
        }
    }

    public void processGetBookCount() {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) return;

        try {
            mBookCount.setText(mBookManager.getBookCount() + "");
            Log.e(TAG, "invoking getBookCount() method: " + mBookManager.getBookCount());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void processAddBook() {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) return;

        Book book = new Book();
        book.setBookName("Android开发基础" + count);
        book.setBookPrice("35");
        book.setBookAuthor("Author" + count);
        count++;
        try {
            mBookManager.addBook(book);
            Log.e(getLocalClassName(), book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void processGetBooks() {
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBookManager == null) return;

        try {
            StringBuilder infoBuilder = new StringBuilder();
            List<Book> books = mBookManager.getBooks();
            Book book;
            if (books == null || books.size() <= 0 ){
                return;
            }
            for (int i = 0; i < books.size(); i++){
                book = books.get(i);

                infoBuilder.append(i + 1);
                infoBuilder.append("\n");
                infoBuilder.append("Name: ");
                infoBuilder.append(book.getBookName());
                infoBuilder.append("\n");
                infoBuilder.append("Author: ");
                infoBuilder.append(book.getBookAuthor());
                infoBuilder.append("\n");
                infoBuilder.append("Price: ");
                infoBuilder.append(book.getBookPrice());
                infoBuilder.append("\n");
            }

            mBoookInfoView.setGravity(0);
            mBoookInfoView.setText(infoBuilder.toString());
            Log.d(TAG, "[processGetBooks()]: \n" + infoBuilder.toString());


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("cn.edu.zstu.aidl");
        intent.setPackage("cn.edu.zstu.aidlservicedemo");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "[onServiceConnected()]");
            Toast.makeText(getApplication(), "Service Connected", Toast.LENGTH_SHORT).show();
            mBookManager = BookManager.Stub.asInterface(iBinder);
            mBound = true;

            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBooks();
                    Log.e(getLocalClassName(), mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };
}
