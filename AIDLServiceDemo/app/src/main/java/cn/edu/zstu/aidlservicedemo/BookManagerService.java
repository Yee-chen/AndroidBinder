package cn.edu.zstu.aidlservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zstu.aidlservicedemo.bean.Book;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private List<Book> mBooks = new ArrayList<>();
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBookManager;
    }

    @Override
    public void onCreate() {
        Book book = new Book();
        book.setBookName("Android开发");
        book.setBookPrice("28");
        book.setBookAuthor("XXX");
        mBooks.add(book);
        super.onCreate();
    }

    private BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public Book getBook(String bookName) throws RemoteException {
            synchronized (this){
                if (mBooks == null || mBooks.size() <= 0 ){
                    return null;
                }
                for (int i=0; i < mBooks.size();i++){
                    if (mBooks.get(i).equals(bookName)){
                        return mBooks.get(i);
                    }
                }
                return null;
            }
        }

        @Override
        public int getBookCount() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBookCount() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks.size();
                }
                return 0;
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if(book != null){
                    mBooks.add(book);
                    Log.e(TAG,"invoking addBook() method , now the list is : " + mBooks.toString());
                }
            }
        }
    };
}
