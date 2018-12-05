package and.elvis.androiddevsearch.chapter2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import and.elvis.androiddevsearch.Book;
import and.elvis.androiddevsearch.R;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Cursor bookCursor = getContentResolver().query(BookProvider.BOOK_CONTENT_URI, null, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.e(TAG, book.bookId + " : " + book.bookName);
        }
        Cursor userCursor = getContentResolver().query(BookProvider.USER_CONTENT_URI, null, null, null, null);
        while (userCursor.moveToNext()){
            int userId = userCursor.getInt(0);
            String userName = userCursor.getString(1);
            Log.e(TAG, userId + " : " + userName);
        }
//        getContentResolver().query(BookProvider.USER_CONTENT_URI, null, null, null, null);
    }
}
