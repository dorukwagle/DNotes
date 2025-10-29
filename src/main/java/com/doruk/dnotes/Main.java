package com.doruk.dnotes;

/**
 * A separate main class is needed to launch the JavaFX application from a fat JAR.
 * This is a standard workaround for the JavaFX modularity system.
 */
public class Main {
    static void main(String[] args) {
        // check if another instance is running
        if (!SingleAppInstance.lockInstance()) {
            System.out.println("Another instance of dNotes is already running.");
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(SingleAppInstance::releaseLock));
        // This call will correctly start the JavaFX application.
        App.run(args);
    }
}