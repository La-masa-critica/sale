package com.masa.sell.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SaleStatus {
    PENDING("PENDING", "Sale is created but not processed"),
    PROCESSING("PROCESSING", "Sale is being processed"),
    COMPLETED("COMPLETED", "Sale has been successfully completed"),
    CANCELLED("CANCELLED", "Sale was cancelled"),
    FAILED("FAILED", "Sale processing failed");

    private final String code;
    private final String description;

    public static SaleStatus fromCode(String code) {
        return Arrays.stream(SaleStatus.values())
                .filter(status -> status.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid sale status code: " + code));
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED || this == FAILED;
    }

    public boolean canTransitionTo(SaleStatus newStatus) {
        if (this.isTerminal()) {
            return false;
        }

        return switch (this) {
            case PENDING -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == COMPLETED || newStatus == FAILED;
            default -> false;
        };
    }
}
