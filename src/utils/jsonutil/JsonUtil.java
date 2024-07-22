/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.jsonutil;

import com.google.gson.Gson;

/**
 *
 * @author Mohammed
 */
    public class JsonUtil {
    private static Gson gson = new Gson();
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}


