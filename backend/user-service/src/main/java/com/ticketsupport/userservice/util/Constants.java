package com.ticketsupport.userservice.util;

public final class Constants {
    private Constants() {
    }

    public static final String JWT_SECRET = "fake-secret-key-replace-with-firebase-later";
    public static final long JWT_EXPIRY_MS = 24L * 60L * 60L * 1000L;
}
