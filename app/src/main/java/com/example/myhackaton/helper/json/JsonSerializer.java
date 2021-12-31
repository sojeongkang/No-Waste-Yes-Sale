package com.example.myhackaton.helper.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonSerializer<T> {
    private Gson gson = null;
    private TypeToken<T> typetoken;

    public JsonSerializer(TypeToken<T> typetoken) {
        gson = new Gson();
        this.typetoken = typetoken;
    }

    public String serialize(T obj) {
        return gson.toJson(obj, typetoken.getType());
    }

    public T deserialize(String s) {
        return gson.fromJson(s, typetoken.getType());
    }

    public String convert(T obj) {
        return serialize(obj);
    }

    public T convert(String str) {
        return deserialize(str);
    }
}
