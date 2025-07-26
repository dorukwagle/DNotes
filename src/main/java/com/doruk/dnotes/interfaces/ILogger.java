package com.doruk.dnotes.interfaces;

public interface ILogger {
    void info(String message);
    void error(Thread t, Exception e);
}
