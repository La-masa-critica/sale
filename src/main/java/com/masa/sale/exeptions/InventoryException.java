package com.masa.sell.exeptions;

public class InventoryException extends RuntimeException{
    public InventoryException(String message) {
        super(message);
    }
}
