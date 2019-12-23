package com.preapm.agent.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Logger;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.preapm.agent.bean.PatternsYaml;
import com.preapm.agent.bean.PluginConfigYaml;

public class YMLMappingUtil {

	
	private static Logger log = LogManager.getLogger(YMLMappingUtil.class);
	
	
    private static YamlReader yamlReader;


    public static Object reader(Class<?> clazz) throws YamlException {
        if(yamlReader == null){
            init();
        }
        return yamlReader.read(clazz);
    }

    private static void init() {
        String path = PathUtil.getProjectPath() + "/pre.yml";
        log.info("load yaml path:"+path);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            yamlReader = new YamlReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception{
    	PluginConfigYaml patternsYaml = (PluginConfigYaml) YMLMappingUtil.reader(PluginConfigYaml.class);
    	System.out.println(patternsYaml.getPlugins());
	
    	PatternsYaml aa = (PatternsYaml) YMLMappingUtil.reader(PatternsYaml.class);
    	System.out.println(aa.getPatterns());
    }
    

}
