package com.doruk.dnotes.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import com.doruk.dnotes.DIFactory;
import com.doruk.dnotes.interfaces.IEventManager.InternalEvent;
import com.doruk.dnotes.interfaces.ILogger;

public class FileLogger implements ILogger {
    private final String path = PathUtils.getLogDir() + File.separator + "dNotes.log";
    private final static FileLogger instance = new FileLogger();
    private final PrintWriter flw, console;

    private FileLogger() {
        try {
            this.flw = new PrintWriter(new FileWriter(path, true), true);
            this.console = new PrintWriter(System.out, true);

             // also add cleanup code here
            DIFactory.createEventManager().register(InternalEvent.SHUTDOWN, () -> {
                flw.flush();
                console.flush();
                flw.close();
                console.close();
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize file logger", e);
        }
    }
    
    private synchronized void write(String data) {
        try {
            console.println(data);
            flw.println(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write to file", e);
        }
    }

    @Override
    public void info(String message) {
        write("Info: " + new Date().toString());
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
        write("StackTrace: " + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
        write("--------------------------------");
    }

    public static FileLogger getInstance() {
        return instance;
    }
}
