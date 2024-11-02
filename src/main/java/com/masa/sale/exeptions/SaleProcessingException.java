package com.masa.sale.exeptions;

public class SaleProcessingException extends RuntimeException {
    public SaleProcessingException(String message) {
        super(message);
    }
}
