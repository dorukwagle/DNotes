package com.doruk.dnotes.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import com.doruk.dnotes.interfaces.ILogger;

public class FileLogger implements ILogger {
    
    private void write(String data) {
        var path = PathUtils.getLogDir() + File.separator + "log.txt";
        try (
            PrintWriter flw = new PrintWriter(new FileWriter(path, true));
            PrintWriter console = new PrintWriter(System.out);
            ) {
            flw.println(data);
            console.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void info(String message) {
        write("info: " + new Date().toString());
        write("message: " + message);
        write("--------------------------------");
    }

    @Override
    public void error(Thread t, Exception e) {
        write("Error: " + new Date().toString());
        write("Thread: " + t.getName());
        write("Exception: " + e.getClass().getName());
        write("Message: " + e.getMessage());
        write("Caused By: " + e.getCause());
        write("StackTrace: " + e.getStackTrace().toString());
        write("--------------------------------");
    }
}
