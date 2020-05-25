package com.evolut.payment.utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public final class GSONHelper {

    public static ResponseTransformer toJson() {
        return new Gson()::toJson;
    }

    public static String toJson(Object toJsonObj) {
        return new Gson().toJson(toJsonObj);
    }

    public static <T> T fromJson(String body, Class<T> clazz) {
        T resultObj = new Gson().fromJson(body, clazz);
        return resultObj;
    }
}
