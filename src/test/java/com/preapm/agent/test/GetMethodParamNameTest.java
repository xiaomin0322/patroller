package com.preapm.agent.test;

import java.util.Arrays;

import com.preapm.agent.util.ReflectUtil;

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
	/*	Method method1 = DruidDataSource.class.getDeclaredMethod("getConnection", long.class);
		System.out.println(Arrays.toString(ReflectMethodUtil.getMethodParamNames(method1)));*/

		/*ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("com.dominos.cloud.test.GetMethodParamNameTest");
		CtClass[] paramTypes = { pool.get(String.class.getName()), pool.get(Object.class.getName()) };
		CtMethod m = cc.getDeclaredMethod("method", paramTypes);

		System.out.println(Arrays.toString(ReflectMethodUtil.getMethodParamNames(cc, m)));*/
		
		/*ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("com.alibaba.druid.pool.DruidDataSource");
		CtClass[] paramTypes = { pool.get(long.class.getName()) };
		CtMethod m = cc.getDeclaredMethod("getConnection", paramTypes);

		System.out.println(Arrays.toString(ReflectUtil.getParamNameList(m).toArray()));*/
		
		Thread currentThread = Thread.currentThread();
		
		Class.forName("com.dominos.cloud.test.GetMethodParamNameTest");

		Class.forName("com.dominos.cloud.test.GetMethodParamNameTest");
		
		
		Class.forName("com.dominos.cloud.test.GetMethodParamNameTest",false,currentThread.getContextClassLoader());

		Class.forName("com.dominos.cloud.test.GetMethodParamNameTest",false,currentThread.getContextClassLoader());
		
		
		
		
	}

}