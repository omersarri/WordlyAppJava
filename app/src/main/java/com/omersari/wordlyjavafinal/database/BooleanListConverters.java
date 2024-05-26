package com.omersari.wordlyjavafinal.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class BooleanListConverters {
    @TypeConverter
    public static String fromBooleanList(List<Boolean> list) {
        if (list == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Boolean> toBooleanList(String value) {
        if (value == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Boolean>>() {}.getType();
        return gson.fromJson(value, listType);
    }
}