package com.masa.sale.exeptions;

public class InventoryException extends RuntimeException{
    public InventoryException(String message) {
        super(message);
    }
}
