package com.metalancer.backend.common.aop;


public class TransactionContext {

    private static final ThreadLocal<String> transactionId = new ThreadLocal<>();

    public static String getTransactionId() {
        return transactionId.get();
    }

    public static void setTransactionId(String value) {
        transactionId.set(value);
    }

    public static void clear() {
        transactionId.remove();
    }
}


