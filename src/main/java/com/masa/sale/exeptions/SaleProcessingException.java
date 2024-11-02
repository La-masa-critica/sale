package com.masa.sell.exeptions;

public class SaleProcessingException extends RuntimeException {
    public SaleProcessingException(String message) {
        super(message);
    }
}
