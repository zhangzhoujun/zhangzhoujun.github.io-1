package com.dim.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * @time 创建时间：2019/3/19
 * @explain 类说明:
 */
public class GsonUtils {

    public static <T> T fromJson(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static <T> List<T> listFromJson(String json, Class<T> type) {
        List<T> result = new ArrayList<>();
        try {
            Gson gson = new Gson();

            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                result.add(gson.fromJson(jsonElement, type));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String objectToJsonStr(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String listToJsonStr(ArrayList<String> path) {
        JsonArray result = new JsonArray();
        for (String s : path) {
            result.add(s);
        }
        return result.toString();
    }

    public static <T> String arrayToJsonStr(ArrayList<T> objs) {
        Gson gson = new Gson();
        return gson.toJson(objs);
    }
}
