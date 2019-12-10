package com.dominos.cloud.agent.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import javassist.CtClass;
import javassist.CtMethod;

/** * 使用ASM获得JAVA类方法参数名 */
public class ReflectMethodUtil {
	class Test {
		void method(String name, Object value) {
		}
	}

	void method(String name, Object value) {
	}

	/*
	 * public static void main(String[] args) throws SecurityException,
	 * NoSuchMethodException, Exception { Method method1 =
	 * Test.class.getDeclaredMethod("method", String.class, Object.class);
	 * System.out.println(Arrays.toString(getMethodParamNames(method1)));
	 * 
	 * ClassPool pool = ClassPool.getDefault(); CtClass cc =
	 * pool.get("com.dominos.cloud.test.GetMethodParamNameTest"); CtClass[]
	 * paramTypes = { pool.get(String.class.getName()),
	 * pool.get(Object.class.getName()) }; CtMethod m =
	 * cc.getDeclaredMethod("method", paramTypes);
	 * 
	 * System.out.println(Arrays.toString(getMethodParamNames(cc, m))); }
	 */

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(CtClass cc, CtMethod method)  {
		try {
			String name = method.getName();

			CtClass[] parameterTypes = method.getParameterTypes();
			List<Class<?>> classes = new ArrayList<>();
			for (CtClass c : parameterTypes) {
				classes.add(Class.forName(c.getName()));
			}
			Method method1 = Class.forName(cc.getName()).getDeclaredMethod(name, classes.toArray(new Class<?>[] {}));
			return getMethodParamNames(method1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new String[] {};

	}

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(final Method method) throws IOException {
		final String methodName = method.getName();
		final Class<?>[] methodParameterTypes = method.getParameterTypes();
		final int methodParameterCount = methodParameterTypes.length;
		final String className = method.getDeclaringClass().getName();
		final boolean isStatic = Modifier.isStatic(method.getModifiers());
		final String[] methodParametersNames = new String[methodParameterCount];
		ClassReader cr = new ClassReader(className);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new ClassAdapter(cw) {
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				final Type[] argTypes = Type.getArgumentTypes(desc); // 参数类型不一致
				if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
					return mv;
				}
				return new MethodAdapter(mv) {
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
							int index) { // 如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
						int methodParameterIndex = isStatic ? index : index - 1;
						if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
							methodParametersNames[methodParameterIndex] = name;
						}
						super.visitLocalVariable(name, desc, signature, start, end, index);
					}
				};
			}
		}, 0);
		return methodParametersNames;
	}

	/** * 比较参数是否一致 */
	private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
		if (types.length != parameterTypes.length) {
			return false;
		}
		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(parameterTypes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}

}