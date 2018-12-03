package and.elvis.androiddevsearch;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Elvis
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    // CopyOnWriteArrayList:支持并发读写
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            } else {
                Log.e(TAG, "already listener");
            }
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            } else {
                Log.e(TAG, "not found this listeners");
            }
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(0, "开发艺术探索"));
        bookList.add(new Book(1, "群英传"));
        new Thread() {
            @Override
            public void run() {
                // service 未销毁
                while (!mIsServiceDestoryed.get()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int bookId = bookList.size() + 1;
                    String bookName = "new Book" + bookId;
                    Book book = new Book(bookId, bookName);
                    bookList.add(book);
                    for (IOnNewBookArrivedListener listener :
                            listeners) {
                        try {
                            listener.onNewBookArrived(book);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed.set(false);
    }
}
