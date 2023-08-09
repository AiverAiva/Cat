package me.weikuwu.cute.exceptions;

public class TypeMismatchException extends RuntimeException {
    public TypeMismatchException(String expected, String given) {
        super("Expected " + expected + ", got " + given);
    }
}
