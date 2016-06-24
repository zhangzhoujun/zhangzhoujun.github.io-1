package com.android.myvolley.json;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.android.myvolley.utils.BaseComFunc;

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String strTmp = json.getAsJsonPrimitive().getAsString();
        if (strTmp.equals("null") || strTmp.equals("")) {
            return null;
        }
        try {
            if (strTmp.contains("-")) {
                return BaseComFunc.SetWebServerSplitDate(strTmp, "yyyy-MM-dd HH:mm:ss");
            } else {
                return BaseComFunc.SetWebServerSplitDate(strTmp, "yyyy/MM/dd HH:mm:ss");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }

    }
}