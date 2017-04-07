package weather.hanwei.com.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/7.
 */
public class MainUtil {

    public static Map<String, String> geturl() {
        JSONObject jo;
        try {
            jo = new JSONObject(httpURLConnectionTO.doGet("http://v.juhe.cn/weather/index?format=1&cityname=郑州&key=d6257f63e0384eba477a218cc63c9a45"));
            Log.e("ee","===="+jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
