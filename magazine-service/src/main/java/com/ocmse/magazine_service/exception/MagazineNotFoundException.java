package com.ocmse.magazine_service.exception;

public class MagazineNotFoundException extends RuntimeException {
    public MagazineNotFoundException(String message) {
        super(message);
    }
}
