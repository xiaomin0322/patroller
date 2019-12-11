package com.preapm.agent.test;
import java.lang.reflect.Method;

/**
 * @author lican
 */
public class ClassLoaderTest {

    private Object fooTestInstance;
    private FooClassLoader fooClassLoader = new FooClassLoader();


    public static void main(String[] args) throws Exception {
        ClassLoaderTest classLoaderTest = new ClassLoaderTest();
        classLoaderTest.initAndLoad();
        Object fooTestInstance = classLoaderTest.getFooTestInstance();
        System.out.println(fooTestInstance.getClass().getClassLoader());


        Method getFoo = fooTestInstance.getClass().getMethod("getFoo");
        System.out.println(getFoo.invoke(fooTestInstance));

        System.out.println(classLoaderTest.getClass().getClassLoader());
    }

    private void initAndLoad() throws Exception {
        Class<?> aClass = Class.forName("com.example.test.FooTest", true, fooClassLoader);
        fooTestInstance = aClass.newInstance();
    }

    public Object getFooTestInstance() {
        return fooTestInstance;
    }
}