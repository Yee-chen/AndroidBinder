// BookManager.aidl
package cn.edu.zstu.aidlservicedemo;
import cn.edu.zstu.aidlservicedemo.bean.Book;
// Declare any non-default types here with import statements

interface BookManager {

        List<Book> getBooks();
        Book getBook(String bookName);
        int getBookCount();

        // 传参时除了Java基本类型以及String，CharSequence之外的类型
        // 都需要在前面加上定向tag(in/out/inout)
        // in   : 表示数据流向为从客户端到服务器；
        // out  ：表示数据流向为从服务器到客户端；
        // inout：表示数据流向为双向。
        // 具体加什么根据函数功能而定
        void addBook(in Book book);
}
