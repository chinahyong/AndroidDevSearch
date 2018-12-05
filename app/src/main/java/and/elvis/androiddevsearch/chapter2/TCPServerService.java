package and.elvis.androiddevsearch.chapter2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @author Elvis
 */
public class TCPServerService extends Service {
    private final static String TAG = "TCPServerService";
    private boolean mIsServiceOnDestoryed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊，呵呵", "请问你叫什么名字",
            "今天天气不错", "你知道吗，我可以跟很多人同时聊天"
    };

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        new Thread(new TCPServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceOnDestoryed = true;
        super.onDestroy();
    }

    private class TCPServer implements Runnable {
        @Override
        public void run() {
            Log.e(TAG, "TCPServer");
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(8788);
                while (!mIsServiceOnDestoryed) {
                    // 接收客户端请求
                    final Socket client = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, "response Cliet:" + e.getMessage());
                            }
                        }
                    }.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //响应客户端
    private void responseClient(Socket client) throws IOException {
        Log.e(TAG, "responseClient：" + mIsServiceOnDestoryed);
        // 用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 发送数据给客户端
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceOnDestoryed) {
            if (in.ready()) {
                String str = in.readLine();
                Log.e(TAG, "Server Receive Client:" + str);
                if (str == null) {
                    break;
                }
                int i = new Random().nextInt(mDefinedMessages.length);
                String msg = mDefinedMessages[i];
                out.println(msg);
                Log.e(TAG, "Server Send:" + msg);
            }
        }
        Log.e(TAG, "Server Quit:");
        if (in != null) {
            in.close();
            in = null;
        }
        if (out != null) {
            out.close();
            out = null;
        }
        // 连接断开
        client.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
