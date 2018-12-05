package and.elvis.androiddevsearch.chapter2;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import and.elvis.androiddevsearch.BookManagerActivity;
import and.elvis.androiddevsearch.R;

/**
 * :remote：当前应用私有进程
 * .remote：没有：的，属于全局进程，其他应用可通过shareUID来共享进程
 * 不同进程拥有独立的虚拟机、内存、application
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private Socket mClientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("test");
            Log.e(TAG, "oncreate:" + test);
        }
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
        Log.e(TAG, "userId:" + (++UserManager.userId));
    }

    public void connectServer(View view) {
        new Thread() {
            @Override
            public void run() {
                while (mClientSocket == null) {
                    try {
                        mClientSocket = new Socket("localhost", 8788);
                        // 向服务端写内容
                        final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                        new Thread() {
                            @Override
                            public void run() {
                                int i = 0;
                                while (i < 10) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    printWriter.println(i);
                                    Log.e(TAG, "Client Send:" + i);
                                    i++;
                                }
                            }
                        }.start();
                        // 读取服务端发送的内容
                        BufferedReader reader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
                        while (!MainActivity.this.isFinishing()) {
                            if (reader.ready()) {
                                String str = reader.readLine();
                                if (str != null) {
                                    Log.e(TAG, "Client Receive:" + str);
                                }
                            }
                        }
                        Log.e(TAG, "Client Quit:");
                        printWriter.close();
                        reader.close();
                        mClientSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Client Receive:" + e.getMessage());
//                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void onAnimation(View view) {
        ObjectAnimator.ofFloat(view, "translationX", 0,100).setDuration(1000).start();
        ObjectAnimator.ofFloat(view, "translationY", 0,100).setDuration(1000).start();
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

    public void toIPC(View view) {
        startActivity(new Intent(this, BookManagerActivity.class));
    }

    public void toProvider(View view) {
        startActivity(new Intent(this, ProviderActivity.class));
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
        Log.e(TAG, "onDestroy");
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

}
