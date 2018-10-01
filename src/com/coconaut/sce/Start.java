package com.coconaut.sce;

import com.coconaut.sce.main.Main;

public class Start {
    public static void main(String args[]) {
        String os = System.getProperty("os.name");
        System.out.println("OS: " + os + " v" + System.getProperty("os.version"));
        System.out.println("OS Arch: " + System.getProperty("os.arch"));
        System.out.println("Java version: " +  System.getProperty("java.version"));
        System.out.println("Java vendor: " +  System.getProperty("java.vendor"));
        if(os.equals("Windows 10")) {
            setProperty("sun.java2d.d3d", "true");
            setProperty("sun.java2d.ddforcevram", "true");
        } else {
            setProperty("sun.java2d.opengl", "true");
        }

        new Main().start();
    }

    public static void setProperty(String a, String b) {
        System.setProperty(a, b);
        System.out.println(a + "=" + b);
    }

}
