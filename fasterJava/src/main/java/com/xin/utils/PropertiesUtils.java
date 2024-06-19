package com.xin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 10:22
 */
public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String, String> PROPER_MAP = new ConcurrentHashMap();
    static {
        InputStream is = null;
        try{
            //读取配置文件
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            //加载配置文件
            props.load(is);
            //遍历配置文件
            Iterator<Object> iterator = props.keySet().iterator();
            while (iterator.hasNext()){
                //把对应的key和value塞入到map中
                String key = iterator.next().toString();
                PROPER_MAP.put(key,props.getProperty(key));
            }

        }catch (Exception e){

        }finally {
            if (is != null){
                try {
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 通过对应的key拿到相应的配置文件的值
     * @param key
     * @return
     */
    public static String getProperty(String key){
        return PROPER_MAP.get(key);
    }



}
