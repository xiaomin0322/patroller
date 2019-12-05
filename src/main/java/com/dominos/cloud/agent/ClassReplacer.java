package com.dominos.cloud.agent;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import java.io.IOException;

public class ClassReplacer
{
private final String className;
private final ClassLoader classLoader;
private final CtClass ctClass;

public ClassReplacer(String className, ClassLoader classLoader, CtClass ctClass)
{
    this.className = className;
    this.classLoader = classLoader;
    this.ctClass = ctClass;
}

public void replace(CtMethod ctMethod, ClassWrapper paramd) throws CannotCompileException {
    String methodName = ctMethod.getName();
    CtMethod localCtMethod2 = CtNewMethod.copy(ctMethod, methodName, this.ctClass, null);
    localCtMethod2.setName(methodName + "$agent");
    this.ctClass.addMethod(localCtMethod2);
    ctMethod.setBody(paramd.beginSrc(ctMethod));
}

public byte[] replace() throws IOException, CannotCompileException {
    return this.ctClass.toBytecode();
}
}