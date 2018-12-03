package and.elvis.androiddevsearch.chapter1;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import and.elvis.androiddevsearch.R;

/**
 * Activity启动模式，生命周期
 * intent-filter：action、category、data：mimetype+URI
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("test");
            Log.e(TAG, "oncreate:" + test);
        }
    }

    // SingleTop:新创建Activity在栈顶存在实例，会直接打开旧的实例，并进入onNewIntent->onResume
    // SingleTask:当前插入栈存在Activity实例，直接clearTop，将其置于栈顶，onNewIntent->onResume
    // SingleInstance:系统针对当前Activity创建单独的任务栈，然后进行复用，除非任务栈被删除，否则不会重新创建
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent:" + intent.getLongExtra("time", 0));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState");
        String test = savedInstanceState.getString("test");
        Log.e(TAG, "onRestoreInstanceState:" + test);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged:" + newConfig.orientation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
        outState.putString("test", "onSaveInstance");
    }

    public void toSecond(View view) {
        startActivity(new Intent(this, SecondAcitivty.class));
    }

    public void toSelf(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.putExtra("time", System.currentTimeMillis()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

}
