package com.preapm.agent.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javassist.CtClass;
import javassist.CtMethod;

/** * 使用ASM获得JAVA类方法参数名 */
public class ReflectMethodUtil {

	private static Logger log = LogManager.getLogger(ReflectMethodUtil.class);

	class Test {
		void method(String name, Object value) {
		}
	}

	void method(String name, Object value) {
	}

	public static final int OPCODES = Opcodes.ASM7;

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(CtClass cc, CtMethod method) {
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
			log.severe("methodName: " + method.getLongName() + "\n "
					+ org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		return new String[] {};

	}

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(ClassLoader classLoader, CtClass cc, CtMethod method) {
		try {
			String name = method.getName();

			CtClass[] parameterTypes = method.getParameterTypes();
			List<Class<?>> classes = new ArrayList<>();
			for (CtClass c : parameterTypes) {
				classes.add(Class.forName(c.getName(), false, classLoader));
			}
			Method method1 = Class.forName(cc.getName(), false, classLoader).getDeclaredMethod(name,
					classes.toArray(new Class<?>[] {}));
			return getMethodParamNames(method1);
		} catch (Exception e) {
			log.severe("methodName: " + method.getLongName() + "\n "
					+ org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		return new String[] {};

	}

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(ClassLoader classLoader, byte[] bytes, CtClass cc, CtMethod method) {
		try {

			/*
			 * if (isContextClassLoader(classLoader)) { return getMethodParamNames(cc,
			 * method); }
			 */
			CtClass[] parameterTypes = method.getParameterTypes();
			List<Class<?>> classes = new ArrayList<>();
			for (CtClass c : parameterTypes) {
				classes.add(getClass(classLoader, c.getName()));
			}
			return getMethodParamNames(classLoader, method, bytes);
		} catch (Exception e) {
			log.severe("methodName: " + method.getLongName() + "\n "
					+ org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
		return new String[] {};

	}

	private static Map<String, Class<?>> m = new HashMap<String, Class<?>>();
	static {

		m.put("int", int.class);
		m.put("boolean", boolean.class);
		m.put("float", float.class);
		m.put("long", long.class);
		m.put("short", short.class);
		m.put("byte", byte.class);
		m.put("double", double.class);
		m.put("char", char.class);

	}

	public static Class<?> getClass(ClassLoader classLoader, String className) throws Exception {
		Class<?> c = m.get(className);
		if (c != null) {
			return c;
		}

		if (classLoader != null) {
			// return Class.forName(className, false, classLoader);
			return classLoader.loadClass(className);
		} else {
			return Class.forName(className);
		}
	}

	public static boolean isContextClassLoader(ClassLoader classLoader) {
		Thread currentThread = Thread.currentThread();
		ClassLoader contextClassLoader = currentThread.getContextClassLoader();
		System.out.println("当前getContextClassLoader:" + contextClassLoader + " 参数ClassLoader:" + classLoader);
		if (contextClassLoader != classLoader) {
			return false;
		}
		return true;
	}

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(final Method method, byte[] bytes) throws IOException {
		final String methodName = method.getName();
		final Class<?>[] methodParameterTypes = method.getParameterTypes();
		final int methodParameterCount = methodParameterTypes.length;
		final String className = method.getDeclaringClass().getName();
		final boolean isStatic = Modifier.isStatic(method.getModifiers());
		final String[] methodParametersNames = new String[methodParameterCount];
		// ClassReader cr = new ClassReader(className);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new ClassVisitor(OPCODES) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				// 只处理指定的方法
				Type[] argumentTypes = Type.getArgumentTypes(desc);
				if (!method.getName().equals(name) || !matchTypes(argumentTypes, methodParameterTypes)) {
					return null;
				}
				return new MethodVisitor(OPCODES) {
					@Override
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
							int index) {
						// 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
						/*
						 * if (Modifier.isStatic(method.getModifiers())) { methodParametersNames[index]
						 * = name; } else if (index > 0) { methodParametersNames[index - 1] = name; }
						 */
					}
				};

			}
		}, 0);
		return methodParametersNames;
	}

	/** 使用字节码工具ASM来获取方法的参数名 */
	public static String[] getMethodParamNames(ClassLoader classLoader, final CtMethod method, byte[] bytes)
			throws Exception {
		final String methodName = method.getName();
		final CtClass[] methodParameterTypes = method.getParameterTypes();
		final int methodParameterCount = methodParameterTypes.length;
		final boolean isStatic = Modifier.isStatic(method.getModifiers());
		final String[] methodParametersNames = new String[methodParameterCount];
		// ClassReader cr = new ClassReader(className);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new ClassVisitor(OPCODES) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				// 只处理指定的方法
				Type[] argumentTypes = Type.getArgumentTypes(desc);
				if (!method.getName().equals(name) || !matchTypes(classLoader, argumentTypes, methodParameterTypes)) {
					return null;
				}
				return new MethodVisitor(OPCODES) {
					@Override
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
							int index) {
						// 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
						/*
						 * if (Modifier.isStatic(method.getModifiers())) { methodParametersNames[index]
						 * = name; } else if (index > 0) { methodParametersNames[index - 1] = name; }
						 */

						int methodParameterIndex = isStatic ? index : index - 1;
						if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
							methodParametersNames[methodParameterIndex] = name;
						}

					}
				};

			}
		}, 0);
		return methodParametersNames;
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

		cr.accept(new ClassVisitor(OPCODES) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				// 只处理指定的方法
				Type[] argumentTypes = Type.getArgumentTypes(desc);
				if (!method.getName().equals(name) || !matchTypes(argumentTypes, methodParameterTypes)) {
					return null;
				}
				return new MethodVisitor(OPCODES) {
					@Override
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end,
							int index) {
						// 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
						/*
						 * if (Modifier.isStatic(method.getModifiers())) { methodParametersNames[index]
						 * = name; } else if (index > 0) { methodParametersNames[index - 1] = name; }
						 */

						int methodParameterIndex = isStatic ? index : index - 1;
						if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
							methodParametersNames[methodParameterIndex] = name;
						}
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

	/** * 比较参数是否一致 */
	private static boolean matchTypes(ClassLoader classLoader, Type[] types, CtClass[] parameterTypes) {
		if (types.length != parameterTypes.length) {
			return false;
		}
		for (int i = 0; i < types.length; i++) {

			Class<?> c = null;
			try {
				c = getClass(classLoader, parameterTypes[i].getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!Type.getType(c).equals(types[i])) {
				return false;
			}
		}
		return true;
	}

}