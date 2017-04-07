package weather.hanwei.com.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/7.
 */
public class httpURLConnectionTO {
    private static boolean isSuccess = false;

    /**
     * @param httpUrl
     * @return
     */
    public static String doGet(String httpUrl) {
        String result;
        try {
            URL url = new URL(httpUrl);
            java.net.HttpURLConnection urlConnection = (java.net.HttpURLConnection) url.openConnection();
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
     * @param httpUrl
     * @param paramMap
     * @return
     */
    public static String doPost(String httpUrl, Map<String, String> paramMap) {
        String result;
        try {
            URL url = new URL(httpUrl);
            java.net.HttpURLConnection urlConnection = (java.net.HttpURLConnection) url.openConnection();
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


    /**
     * 输入流获取字符串
     * @return String 返回的字符串
     * @throws IOException
     */
    public static String readFromStream(InputStream is) throws IOException {
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

}
