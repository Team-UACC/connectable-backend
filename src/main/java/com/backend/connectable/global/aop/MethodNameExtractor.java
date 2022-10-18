package com.backend.connectable.global.aop;

import org.aspectj.lang.Signature;

public class MethodNameExtractor {
    private MethodNameExtractor() {}

    public static String generate(Signature joinPointSignature) {
        return joinPointSignature.getDeclaringTypeName()
                + "."
                + joinPointSignature.getName()
                + "()";
    }
}
