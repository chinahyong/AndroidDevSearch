package and.elvis.androiddevsearch.chapter2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author Elvis
 * 单独一个进程中（多进程可以认为是多应用）
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITIES = "and.elvis.ipc.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/user");
    public static final int BOOK_CONTENT_CODE = 0;
    public static final int USER_CONTENT_CODE = 1;
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Context mContext;
    private SQLiteDatabase mDb;

    static {
        mUriMatcher.addURI(AUTHORITIES, "book", BOOK_CONTENT_CODE);
        mUriMatcher.addURI(AUTHORITIES, "user", USER_CONTENT_CODE);
    }

    @Override
    public boolean onCreate() {
        Log.e(TAG, "onCreate");
        mContext = getContext();
        // 数据库插入数据
        initProviderData();
        return false;
    }

    private void initProviderData() {
        mDb = new DBOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("DELETE FROM " + DBOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("DELETE FROM " + DBOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("INSERT INTO book values(1,'Book1');");
        mDb.execSQL("INSERT INTO book values(2,'Book2');");
        mDb.execSQL("INSERT INTO book values(3,'Book3');");
        mDb.execSQL("INSERT INTO user values(1,'User1',1);");
        mDb.execSQL("INSERT INTO user values(2,'User2',1);");
        mDb.execSQL("INSERT INTO user values(3,'User3',0);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.e(TAG, "query : " + Thread.currentThread());
        String table = getTableName(uri);
        return mDb.query(table, strings, s, strings1, s1, null, null);
    }

    private String getTableName(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case USER_CONTENT_CODE:
                return DBOpenHelper.USER_TABLE_NAME;
            case BOOK_CONTENT_CODE:
                return DBOpenHelper.BOOK_TABLE_NAME;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(TAG, "getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.e(TAG, "insert : " + Thread.currentThread());
        String table = getTableName(uri);
        mDb.insert(table, null, contentValues);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.e(TAG, "delete : " + Thread.currentThread());
        return mDb.delete(getTableName(uri), s, strings);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.e(TAG, "update : " + Thread.currentThread());
        return mDb.update(getTableName(uri), contentValues, s, strings);
    }
}
