package com.dominos.cloud.agent;
import javassist.CtClass;

public interface Collector {

    boolean isTarget(String className, ClassLoader classLoader, CtClass ctClass);
    
    boolean isTarget(String className);

    byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass);

}