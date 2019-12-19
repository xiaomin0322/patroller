package com.preapm.agent.weave;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.preapm.agent.constant.BaseConstants;
import com.preapm.agent.util.LogManager;
import com.preapm.agent.util.ReflectMethodUtil;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class ClassWrapper {
	
	private static Logger log = LogManager.getLogger(ClassWrapper.class);
	
	private String beginSrc=BaseConstants.NULL;
	private String endSrc=BaseConstants.NULL;;
	private String errorSrc=BaseConstants.NULL;;
	

	public ClassWrapper beginSrc(String paramString) {
		this.beginSrc = paramString;
		return this;
	}

	public ClassWrapper endSrc(String paramString) {
		this.endSrc = paramString;
		return this;
	}

	public ClassWrapper errorSrc(String paramString) {
		this.errorSrc = paramString;
		return this;
	}
	//构造方法的insertAfter是不在构造方法结构体内的
	public String beginSrc(ClassLoader classLoader,byte[] classfileBuffer,CtClass ctClass, CtConstructor ctMethod) {
		String methodName=ctMethod.getName();
		try {
			ctMethod.insertBeforeBody(beforAgent(methodName, null));
			ctMethod.insertAfter(afterAgent(com.preapm.agent.constant.BaseConstants.ONLY_AFTER));
			///ctMethod.insertBeforeBody("System.out.println(\"方法名\"+this.i);");//内部没有被初始化完
			//ctMethod.insertAfter("System.out.println(\"方法名end\"+this.i);");//被初始化完可以访问内初始化变量
			//ctMethod.insertBeforeBody("int zzm = 555;");
			//ctMethod.insertAfter("System.out.println(\"方法名end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\");");
			//ctMethod.insertAfter("System.out.println(\"方法名end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\"+zzm);",true);//访问不了构造方法内部的
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	

	public String beginSrc(ClassLoader classLoader,byte[] classfileBuffer,CtClass ctClass, CtMethod ctMethod) {
		String methodName = ctMethod.getName();
		List<String> paramNameList = Arrays.asList(ReflectMethodUtil.getMethodParamNames(classLoader,classfileBuffer,ctClass, ctMethod));
		try {
			 //System.out.println("方法名称："+methodName+" 参数类型大小："+ctMethod.getParameterTypes().length+" paramNameList："+paramNameList.toArray());
			
			  String template = ctMethod.getReturnType().getName().equals("void")
                ?
                "{\n" +
                "    %s        \n" +  beforAgent(methodName,paramNameList)+" \n"+
                "    try {\n" + 
                "        %s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s\n" +doError(BaseConstants.THROWABLE_NAME_STR)+
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s\n" + afterAgent(null)+" \n"+
                "    }\n" +
                "}"
                :
                "{\n" +
                "    %s        \n" +
                "    Object result=null;\n" +beforAgent(methodName,paramNameList)+" \n"+
                "    try {\n" +
                "        result=($w)%s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s            \n" +doError(BaseConstants.THROWABLE_NAME_STR)+
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s        \n" + afterAgent(BaseConstants.RESULT_NAME_STR)+" \n"+
                "    }\n" +
                "    return ($r) result;\n" +
                "}";
			  
		

			String insertBeginSrc = this.beginSrc == null ? "" : this.beginSrc;
			String insertErrorSrc = this.errorSrc == null ? "" : this.errorSrc;
			String insertEndSrc = this.endSrc == null ? "" : this.endSrc;
			String result = String.format(template,
					new Object[] { insertBeginSrc, ctMethod.getName(), insertErrorSrc, insertEndSrc });
			//log.info("result:"+result);
			return result;
		} catch (NotFoundException localNotFoundException) {
			log.severe(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(localNotFoundException));
			throw new RuntimeException(localNotFoundException);
		}
	}


	/**
	 * 做事情的方法
	 * @param methodName 方法名称
	 * @param argNameList 方法参数
	 * @return
	 */
	public abstract String beforAgent(String methodName, List<String> argNameList);
	
	
	/**
	 *  异常的处理方法
	 * @param methodName
	 * @param argNameList
	 * @return
	 */
	public abstract String doError(String error);

	/**
	 * 有返回值的结束方法
	 * @return
	 */
	public abstract String afterAgent(String resultName);



	public static String toStr(String val) {
		return "\"" + val + "\"";
	}
	
	public static String line() {
		return "\n";
	}

	public static String toStrto(String val) {
		return "\"" + val + "\"+";
	}
}