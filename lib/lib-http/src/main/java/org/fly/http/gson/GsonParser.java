package org.fly.http.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/15 3:13 下午
 * @description: -
 * @since: 1.0.0
 */
public class GsonParser {

    public static Gson optimize() {
        return new GsonBuilder()
                // 遇到Map类型时，使用我们自己的Adapter
                .registerTypeAdapter(
                        new TypeToken<Map<String, Object>>() {
                        }.getType(),
                        new MapFixTypeAdapter())
                .create();
    }
}
