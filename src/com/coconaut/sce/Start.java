package com.coconaut.sce;

import com.coconaut.sce.main.Main;
import com.coconaut.sce.utils.Log;

public class Start {
    public static void main(String args[]) {
        String os = System.getProperty("os.name");
        Log.info("OS: " + os + " v" + System.getProperty("os.version"));
        Log.info("OS Arch: " + System.getProperty("os.arch"));
        Log.info("Java version: " +  System.getProperty("java.version"));
        Log.info("Java vendor: " +  System.getProperty("java.vendor"));
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
        Log.info(a + "=" + b);
    }

}
