package com.doruk.dnotes;

/**
 * A separate main class is needed to launch the JavaFX application from a fat JAR.
 * This is a standard workaround for the JavaFX modularity system.
 */
public class Main {
    public static void main(String[] args) {
        // This call will correctly start the JavaFX application.
        App.main(args);
    }
}