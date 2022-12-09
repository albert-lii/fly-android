package org.fly.http.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * Gson解析工具类
 */
public class GsonParser {

    public static Gson getGson() {
        return new GsonBuilder()
                // 遇到Map类型时，使用我们自己的Adapter
                .registerTypeAdapter(
                        new TypeToken<Map<String, Object>>() {
                        }.getType(),
                        new MapTypeAdapter())
                .create();
    }
}
