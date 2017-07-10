package cn.edu.zstu.aidlservicedemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chenlei on 2017/7/7.
 */
public class Book implements Parcelable {

    private String bookName;
    private String bookPrice;
    private String bookAuthor;

    public Book() {
    }

    protected Book(Parcel in) {
        this.bookName = in.readString();
        this.bookPrice = in.readString();
        this.bookAuthor = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "BookName:" + bookName + "\nBookAuthor:" + bookAuthor + "\nBookPrice:" + bookPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.bookName);
        parcel.writeString(this.bookPrice);
        parcel.writeString(this.bookAuthor);
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
