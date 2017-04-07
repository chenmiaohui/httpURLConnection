package weather.hanwei.com.http;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        content = (TextView) findViewById(R.id.tv_centent);
        btn.setOnClickListener(new OnBtnClickListener());
    }

    private class OnBtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                Map<String,String> resutl = MainUtil.geturl();
            }
        }.start();
    }

    String baseUrl = "http://v.juhe.cn/weather/index?format=1&cityname="+"郑州"+"&key=d6257f63e0384eba477a218cc63c9a45";
    String city = "郑州";
    private void requestGet(HashMap<String, String> paramsMap) {
        try {
            String baseUrl = "http://v.juhe.cn/weather/index?format=1&cityname="+city+"&key=d6257f63e0384eba477a218cc63c9a45";;
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e("", "Get方式请求成功，result--->" + result);
            } else {
                Log.e("", "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }


    private void requestPost(HashMap<String, String> paramsMap) {
        try {
            String baseUrl = "";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params =tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e("", "Post方式请求成功，result--->" + result);
            } else {
                Log.e("", "Post方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e("", e.toString());
            return null;
        }
    }

    private boolean isSuccess = false;
    public String doGet(String httpUrl) {
        String result;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Charset", "UTF-8");
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                result = readFromStream(is);
            } else {
                isSuccess = false;
                result = "网络响应状态码不为200!";
            }
        } catch(IOException e) {
            isSuccess = false;
            result = "网络访问错误:" + e.getMessage();
        }

        return result;
    }

    /**
     * 输入流获取字符串
     *
     * @param is 输入流
     * @return String 返回的字符串
     * @throws IOException
     */
    public String readFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String result = baos.toString();
        baos.close();
        return result;
    }

    public String doPost(String httpUrl, Map<String, String> paramMap) {
        String result;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 配置连接的Content-type
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoOutput(true);    // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true);     // 发送POST请求必须设置允许输入

            String data = "";
            boolean firstParam = true;
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if(firstParam) {
                    data += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                    firstParam = false;
                } else {
                    data += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                }
            }
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                result = readFromStream(is);
            } else {
                isSuccess = false;
                result = "网络响应状态码不为200!";
            }
        } catch (IOException e) {
            isSuccess = false;
            result = "网络访问错误:" + e.getMessage();
        }

        return result;
    }

}
