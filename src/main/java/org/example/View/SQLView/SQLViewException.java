package org.example.View.SQLView;

class NoCredentialsFoundForURL extends RuntimeException {
    public NoCredentialsFoundForURL(String str) {
        super("str");
    }
}

class NoURLFoundException extends RuntimeException {
    public NoURLFoundException() {
        super();
    }
}