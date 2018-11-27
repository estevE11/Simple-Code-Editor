package com.coconaut.sce.utils;

public class Log {
    public static void info(String s) {
        System.out.println("[INFO]: " + s);
    }

    public static void info(int s) {
        System.out.println("[INFO]: " + s);
    }

    public static void info(double s) {
        System.out.println("[INFO]: " + s);
    }

    public static void info(float s) {
        System.out.println("[INFO]: " + s);
    }

    public static void debug(String s) {
        System.out.println("[DEBUG]: " + s);
    }

    public static void debug(int s) {
        System.out.println("[DEBUG]: " + s);
    }
    public static void debug(double s) {
        System.out.println("[DEBUG]: " + s);
    }

    public static void debug(long s) {
        System.out.println("[DEBUG]: " + s);
    }

    public static void error(String s) {
        System.out.println("[ERROR]: " + s);
    }

    public static void error(int s) {
        System.out.println("[ERROR]: " + s);
    }

    public static void error(double s) {
        System.out.println("[ERROR]: " + s);
    }

    public static void error(float s) {
        System.out.println("[ERROR]: " + s);
    }

    public static void shut_down(int i) {
        Log.info("Exiting...");
        System.exit(i);
    }

}
