package com.preapm.agent.util;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * 获取方法形参名称(只能获取类方法，不能获取接口方法)
 * @author WangMi
 * @date 2016-11-30 9:30
 */
public class ParameterNameUtil {

    /**
     * 方法一:
     * <dependency>
     <groupId>org.ow2.asm</groupId>
     <artifactId>asm-commons</artifactId>
     <version>5.1</version>
     </dependency>
     * 获取指定类指定方法的参数名
     *
     * @param clazz 要获取参数名的方法所属的类
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表，如果没有参数，则返回null
     */
    public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames[index] = name;
                            }
                            else if (index > 0) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterNames;
    }


    /**
     *
     * <p>比较参数类型是否一致</p>
     *
     * @param types asm的类型({@link Type})
     * @param clazzes java 类型({@link Class})
     * @return
     */
    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
        // 个数不同
        if (types.length != clazzes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if(!Type.getType(clazzes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     *  方法二:
     *  <dependency>
     <groupId>org.ow2.asm</groupId>
     <artifactId>asm-commons</artifactId>
     <version>5.1</version>
     </dependency>
     * <p>获取方法的参数名</p>
     *
     * @param m
     * @return
     */
    public static String[] getMethodParamNames(final Method m) {
        final String[] paramNames = new String[m.getParameterTypes().length];
        final String n = m.getDeclaringClass().getName();
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = null;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cr.accept(new ClassVisitor(Opcodes.ASM4, cw) {
            @Override
            public MethodVisitor visitMethod(final int access,
                                             final String name, final String desc,
                                             final String signature, final String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(desc);
                // 方法名相同并且参数个数相同
                if (!name.equals(m.getName())
                        || !sameType(args, m.getParameterTypes())) {
                    return super.visitMethod(access, name, desc, signature,
                            exceptions);
                }
                MethodVisitor v = cv.visitMethod(access, name, desc, signature,
                        exceptions);
                return new MethodVisitor(Opcodes.ASM4, v) {
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if(Modifier.isStatic(m.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < paramNames.length) {
                            paramNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start,
                                end, index);
                    }

                };
            }
        }, 0);
        return paramNames;
    }

    /**
     *
     * <p>
     * 获取方法参数名称
     * </p>
     *<dependency>
     <groupId>javassist</groupId>
     <artifactId>javassist</artifactId>
     <version>3.12.1.GA</version>
     </dependency>
     * @param cm
     * @return
     */
    protected static String[] getMethodParamNames(CtMethod cm) {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new RuntimeException(cc.getName());
        }

        String[] paramNames = null;
        try {
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    /**
     * 方法三:javassist(三种方法均只能获取类的参数)
     * 获取方法参数名称，按给定的参数类型匹配方法
     *
     * @param clazz
     * @param method
     * @param paramTypes
     * @return
     */
    public static String[] getMethodParamNames(Class<?> clazz, String method,
                                               Class<?>... paramTypes) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = null;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());

            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramTypeNames[i] = paramTypes[i].getName();

            cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return getMethodParamNames(cm);
    }

    /**
     * 方法三:javassist
     * <dependency>
     <groupId>javassist</groupId>
     <artifactId>javassist</artifactId>
     <version>3.12.1.GA</version>
     </dependency>
     * 获取方法参数名称，匹配同名的某一个方法
     *
     * @param clazz
     * @param method
     * @return
     * @throws NotFoundException
     *             如果类或者方法不存在
     *             如果最终编译的class文件不包含局部变量表信息
     */
    public static String[] getMethodParamNames(Class<?> clazz, String method) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(method);
        } catch (NotFoundException e) {
        }
        return getMethodParamNames(cm);
    }

    /**
     *
     * 方法四: 通过spring获取
     * <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-core</artifactId>
     <version>4.2.8.RELEASE</version>
     </dependency>
     * @param method
     * @return
     */
   /* public static String[] getMethodParamNamesBySpring(Method method){
        ParameterNameDiscoverer pnd=new DefaultParameterNameDiscoverer();
        return pnd.getParameterNames(method);
    }
*/

    public static void main1(String[] args) throws Exception {
//        Class<FinancialTransactionServiceImpl> clazz = FinancialTransactionServiceImpl.class;
//        Method method = clazz.getDeclaredMethod("queryFinancialTransactionList", Long.class);
//        String[] parameterNames = ParameterNameUtils.getMethodParamNames(FinancialTransactionServiceImpl.class, "queryFinancialTransactionList", Long.class);
//        System.out.println(Arrays.toString(parameterNames));
//        System.out.println(getMethodParamNamesBySpring(method)[0]);;
    }


}