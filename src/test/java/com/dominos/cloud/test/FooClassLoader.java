package com.dominos.cloud.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lican
 */
public class FooClassLoader extends ClassLoader {

    private static final String NAME = "/Users/lican/git/test/foo/";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            String s = name.substring(name.lastIndexOf(".") + 1) + ".class";
            File file = new File(NAME + s);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] b = new byte[fileInputStream.available()];
                fileInputStream.read(b);
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedClass;
    }

}