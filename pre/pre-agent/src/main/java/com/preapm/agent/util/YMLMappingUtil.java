package com.preapm.agent.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class YMLMappingUtil {

    private static YamlReader yamlReader;


    public static Object reader(Class<?> clazz) throws YamlException {
        if(yamlReader == null){
            init();
        }
        return yamlReader.read(clazz);
    }

    private static void init() {
        String path = ClassLoaderUtil.getJARPath() + "/test3.yml";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            yamlReader = new YamlReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
