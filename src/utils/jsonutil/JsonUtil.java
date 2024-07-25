/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import models.OnlinePlayer;

/**
 *
 * @author Mohammed
 */
    public class JsonUtil {
    private static Gson gson = new Gson();
    
    
    public static List<OnlinePlayer> fromJsonToList(String json, Class<OnlinePlayer> clazz) {
        Type listType = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, listType);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}


