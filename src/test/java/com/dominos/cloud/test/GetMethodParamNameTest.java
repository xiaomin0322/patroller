package com.dominos.cloud.test;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.dominos.cloud.agent.util.ReflectMethodUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/** * 使用ASM获得JAVA类方法参数名 */
public class GetMethodParamNameTest {
	class Test {
		void method(String name, Object value) {
		}
	}

	void method(String name, Object value) {
	}

	public static void main(String[] args) throws SecurityException, NoSuchMethodException, Exception {
		Method method1 = Test.class.getDeclaredMethod("method", String.class, Object.class);
		System.out.println(Arrays.toString(ReflectMethodUtil.getMethodParamNames(method1)));

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("com.dominos.cloud.test.GetMethodParamNameTest");
		CtClass[] paramTypes = { pool.get(String.class.getName()), pool.get(Object.class.getName()) };
		CtMethod m = cc.getDeclaredMethod("method", paramTypes);

		System.out.println(Arrays.toString(ReflectMethodUtil.getMethodParamNames(cc, m)));
	}

}