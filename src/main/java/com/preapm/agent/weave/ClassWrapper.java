package com.preapm.agent.weave;

import java.util.Arrays;
import java.util.List;

import com.preapm.agent.util.ReflectMethodUtil;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ClassWrapper {
	private String beginSrc;
	private String endSrc;
	private String errorSrc;

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

	public static String beforAgent() {
		StringBuilder builder = new StringBuilder();
		builder.append(" org.springframework.cloud.sleuth.Span newSpan = null;\r\n"
				+ "		org.springframework.cloud.sleuth.Tracer tracer  = null;");
		return builder.toString();
	}

	public static String doAgent() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				" tracer = com.dominos.cloud.common.util.SpringBeanUtils.getBean(org.springframework.cloud.sleuth.Tracer.class);\r\n"
						+ "		if (tracer != null) {\r\n"
						+ "				org.springframework.cloud.sleuth.Span currentSpan = tracer.getCurrentSpan();\r\n"
						+ "				newSpan = tracer.createSpan(\"testMethod\", currentSpan);\r\n"
						+ "				newSpan.tag(\"time\", \"time\");\r\n"
						+ "				 System.out.println(\"加入span成功：\");\r\n" + "			}");
		return builder.toString();
	}

	public static String doAgent(String methodName, List<String> argNameList) {
		StringBuilder builder = new StringBuilder();
		builder.append(
				" tracer = com.dominos.cloud.common.util.SpringBeanUtils.getBean(org.springframework.cloud.sleuth.Tracer.class);\r\n"
						+ "		if (tracer != null) {\r\n"
						+ "				org.springframework.cloud.sleuth.Span currentSpan = tracer.getCurrentSpan();\r\n"
						+ "				newSpan = tracer.createSpan(" + toStr(methodName) + ", currentSpan);\r\n"
						+ " System.out.println(\"加入span成功：\"+" + toStr(methodName) + ");\r\n" + "			}");

		if (argNameList != null) {
			for (int i=0;i<argNameList.size();i++) {
				String arg = argNameList.get(i);
				builder.append("newSpan.tag("+toStr("in."+arg)+", "+"com.alibaba.fastjson.JSONObject.toJSONString($args["+i+"])"+");\r\n");
				builder.append(" System.out.println(\"参数名称:\"+"+ toStrto(arg)
				 +"\"参数值：\"+"+"$args["+i+"]"+");\r\n");
			}
		}

		return builder.toString();
	}

	public static String afterAgent() {
		return afterAgent(null);
	}

	public static String afterAgent(String resultName) {
		StringBuilder builder = new StringBuilder();
		if (resultName != null) {
			builder.append(" newSpan.tag(\"out\", "+"com.alibaba.fastjson.JSONObject.toJSONString("+resultName+")"+");\r\n");
			builder.append(" System.out.println(\"返回值：\"+" + resultName + ");\r\n");
		}
		
		builder.append(" if (newSpan != null && tracer!=null) {\r\n"
				+ "				newSpan.logEvent(org.springframework.cloud.sleuth.Span.CLIENT_RECV);\r\n"
				+ "				tracer.close(newSpan);\r\n" + "			}");
		return builder.toString();
	}

	public String beginSrc(ClassLoader classLoader,byte[] classfileBuffer,CtClass ctClass, CtMethod ctMethod) {
		String methodName = ctMethod.getName();
		List<String> paramNameList = Arrays.asList(ReflectMethodUtil.getMethodParamNames(classLoader,classfileBuffer,ctClass, ctMethod));
		try {
			System.out.println("方法名称："+methodName+" 参数类型大小："+ctMethod.getParameterTypes().length+" paramNameList："+paramNameList.toArray());
			
			  String template = ctMethod.getReturnType().getName().equals("void")
                ?
                "{\n" +
                "    %s        \n" + beforAgent()+" \n"+
                "    try {\n" +  doAgent(methodName,paramNameList)+" \n"+
                "        %s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s\n" +
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s\n" + afterAgent()+" \n"+
                "    }\n" +
                "}"
                :
                "{\n" +
                "    %s        \n" + beforAgent()+" \n"+
                "    Object result=null;\n" +
                "    try {\n" +doAgent(methodName,paramNameList)+" \n"+
                "        result=($w)%s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s            \n" +
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s        \n" + afterAgent("result")+" \n"+
                "    }\n" +
                "    return ($r) result;\n" +
                "}";
			  
		

			String insertBeginSrc = this.beginSrc == null ? "" : this.beginSrc;
			String insertErrorSrc = this.errorSrc == null ? "" : this.errorSrc;
			String insertEndSrc = this.endSrc == null ? "" : this.endSrc;
			String result = String.format(template,
					new Object[] { insertBeginSrc, ctMethod.getName(), insertErrorSrc, insertEndSrc });
			return result;
		} catch (NotFoundException localNotFoundException) {
			throw new RuntimeException(localNotFoundException);
		}
	}



	public static String toStr(String val) {
		return "\"" + val + "\"";
	}

	public static String toStrto(String val) {
		return "\"" + val + "\"+";
	}
}