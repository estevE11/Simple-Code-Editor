package com.coconaut.sce.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.util.ArrayList;

public class Utils {
    public static String[] push(String[] array, String push) {
        if(array.length == 1 && array[0] == null) {
            array[0] = push;
            return array;
        }
        String[] longer = new String[array.length + 1];
        System.arraycopy(array, 0, longer, 0, array.length);
        longer[array.length] = push;
        return longer;
    }

    public static String[] push(String[] array, String push, int idx) {
        String[] result = new String[array.length + 1];
        System.arraycopy(array, 0, result, 0, idx+1);
        System.arraycopy(array, idx, result, idx+1, array.length-idx);
        result[idx+1] = push;
        return result;
    }

    public static String[] pop(String[] array) {
        String[] shorter = new String[array.length - 1];
        System.arraycopy(array, 0, shorter, 0, array.length-1);
        return shorter;
    }

    public static String[] pop(String[] array, int idx) {
        String[] result = new String[array.length-1];
        System.arraycopy(array, 0, result, 0, idx);
        System.arraycopy(array, idx+1, result, idx, array.length-idx-1);
        return result;
    }

    public static void copy_to_clipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static String get_clipboard(){
        try {
            return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void create_file(String path) {
        File f = new File(path);
        try {
            if(f.createNewFile()) {
                System.out.println("File created succesfully!");
            } else {
                JOptionPane.showMessageDialog(null, "File already exists!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write_file(String str, String path) {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(str);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    public static String readFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            String str = "";
            String line;
            while((line = reader.readLine()) != null) {
                str += line;
            }

            fr.close();
            reader.close();
            return str;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to read the files!", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return null;
    }
*/
    public static boolean file_exists(String path) {
        return new File(path).exists();
    }

    public static String[] read_file_by_line(String path) {
        System.out.println(path);
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            String[] str = new String[1];
            String line;

            while((line = reader.readLine()) != null) {
                str = Utils.push(str, line);
            }

            fr.close();
            reader.close();
            return str;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Files required not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read the files!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static String[] get_lines_from_raw_text(String text) {
        String[] lines = text.split("\n");
        if(text.endsWith("\n")) {
            Utils.push(lines, "");
        }
        return lines;
    }

    public static String get_file_name_from_path(String path) {
        String[] p_ = path.split("/");
        String[] p = p_[p_.length-1].split("\\.");
        return p[p.length-2];
    }

    public static String get_file_from_path(String path) {
        String[] p_ = path.split("/");
        return p_[p_.length-1];
    }

    public static String get_file_extension_name_from_path(String path) {
        String[] p = path.split("\\.");
        return p[p.length-1];
    }

    public static boolean is_numeric(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean is_positive(int x) {
        if(x > -1) return true;
        return false;
    }
}
