package com.careerbooker.server.util.enums;


public enum ClientStatusEnum
{
    ACTIVE("active", "Active"),
    INACTIVE("inactive", "Inactive"),
    PENDING("pending", "Pending"),
    CANCEL("cancel", "Cancel"),
    HOLD("hold", "Hold");


    private String code;
    private String description;

    ClientStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClientStatusEnum getEnum(String code) {
        switch (code) {
            case "active":
                return ACTIVE;
            case "inactive":
                return INACTIVE;
            case "pending":
                return PENDING;
            case "cancel":
                return CANCEL;
            case "hold":
                return HOLD;
            default:
                return INACTIVE;
        }
    }
}
