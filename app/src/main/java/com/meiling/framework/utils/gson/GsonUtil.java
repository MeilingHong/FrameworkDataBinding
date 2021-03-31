package com.meiling.framework.utils.gson;

import com.google.gson.Gson;

/**
 * Created by marisareimu@126.com on 2021-03-31  10:50
 * project FrameworkDataBinding
 */

public class GsonUtil {
    private static final GsonUtil ourInstance = new GsonUtil();

    public static GsonUtil getInstance() {
        return ourInstance;
    }
    private Gson gson;
    private GsonUtil() {
        gson = new Gson();
    }

    public String toJsonStr(Object object){
        return gson.toJson(object);
    }
}
