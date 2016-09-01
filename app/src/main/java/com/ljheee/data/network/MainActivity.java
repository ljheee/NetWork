package com.ljheee.data.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textViewShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewShow = (TextView) findViewById(R.id.textView_show);

        new NetThread().start();
    }


    /**
     * 子线程
     * 处理网络操作
     */
    class NetThread extends  Thread{

        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL("https://www.baidu.com");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setConnectTimeout(5000);
                con.setReadTimeout(3000);
                con.setRequestMethod("GET");//大写
//                con.setDoInput(true);
//                con.setDoOutput(true);



                int code = con.getResponseCode();
                if(code == 200){
                    char[] buf = new char[1024];
                    CharArrayWriter charArrayWriter = new CharArrayWriter();

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    int size;
                    while(-1 !=(size = br.read(buf))){
                        charArrayWriter.write(buf,0,size);
                    }

                    final String line = new String(charArrayWriter.toCharArray());


                    //子线程访问UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewShow.setText(line);
                        }
                    });
                }

                con.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
