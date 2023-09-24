package com.careerbooker.server.util.enums;

public enum TimeSlot {
    ONE("1", "8am - 9am"),
    TWO("2", "9am - 10am"),
    THREE("3", "10am - 11am"),
    FOUR("4", "11am - 12pm"),
    FIVE("5", "12pm - 13pm"),
    SIX("6", "13pm - 14pm"),
    SEVEN("7", "14pm - 15pm"),
    EIGHT("8", "15am - 16am");


    private String code;
    private String description;

    TimeSlot(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TimeSlot getTimeSlot(String code) {
        switch (code) {
            case "1":
                return ONE;
            case "2":
                return TWO;
            case "3":
                return THREE;
            case "4":
                return FOUR;
            case "5":
                return FIVE;
            case "6":
                return SIX;
            case "7":
                return SEVEN;
            case "8":
                return EIGHT;
            default:
                return null;
        }
    }
}
