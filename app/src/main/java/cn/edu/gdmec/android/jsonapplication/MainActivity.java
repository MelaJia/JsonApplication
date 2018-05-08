package cn.edu.gdmec.android.jsonapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 详见StrictMode文档
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());


        startUrlCheck();
    }

    private void startUrlCheck() {
        StringBuilder builder=new StringBuilder();

        try {
            URL url=new URL("http://192.168.186.1/ch5/json.php");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);//设置连接超时
            connection.setRequestMethod("GET");
            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                connection.connect();
                InputStream is=connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                for (String s=reader.readLine();s!=null;s=reader.readLine()){
                    builder.append(s);
                }
                JSONObject jsonObject=new JSONObject(builder.toString());
                String userName=jsonObject.getString("username");
                String password=jsonObject.getString("password");
                int user_id=jsonObject.getInt("user_id");
                setTitle("用户id："+user_id);
                TextView tv=findViewById(R.id.textView);
                tv.setText("username:"+userName+"\npassword:"+password);
            }
        } catch (IOException e) {
            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
