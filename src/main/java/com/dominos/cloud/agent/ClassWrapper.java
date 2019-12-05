package com.dominos.cloud.agent;
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

    public String beginSrc(CtMethod ctMethod) {
        try {
            String template = ctMethod.getReturnType().getName().equals("void")
                ?
                "{\n" +
                "    %s        \n" +
                "    try {\n" +
                "        %s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s\n" +
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s\n" +
                "    }\n" +
                "}"
                :
                "{\n" +
                "    %s        \n" +
                "    Object result=null;\n" +
                "    try {\n" +
                "        result=($w)%s$agent($$);\n" +
                "    } catch (Throwable e) {\n" +
                "        %s            \n" +
                "        throw e;\n" +
                "    }finally{\n" +
                "        %s        \n" +
                "    }\n" +
                "    return ($r) result;\n" +
                "}";

            String insertBeginSrc = this.beginSrc == null ? "" : this.beginSrc;
            String insertErrorSrc = this.errorSrc == null ? "" : this.errorSrc;
            String insertEndSrc = this.endSrc == null ? "" : this.endSrc;
            String result = String.format(template, new Object[]{insertBeginSrc, ctMethod.getName(), insertErrorSrc, insertEndSrc});
            return result;
        } catch (NotFoundException localNotFoundException) {
            throw new RuntimeException(localNotFoundException);
        }
    }
}