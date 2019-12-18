package com.preapm.agent.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class YMLMappingUtil {

    static {
        String path = ClassLoaderUtil.getJARPath() + "/test3.yml";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        yamlReader = new YamlReader(fileReader);
    }

    private static YamlReader yamlReader;
    private static YMLMappingUtil configMappingUtil;


    private static Object reader(Class<?> clazz) throws YamlException {
        return yamlReader.read(clazz);
    }

//    public static void main(String[] args) throws YamlException, FileNotFoundException {
////        YMLMappingUtil instance = YMLMappingUtil.reader();
//
//
//        System.out.println( YMLMappingUtil.reader(PluginsBean.class));
//
//    }
}
