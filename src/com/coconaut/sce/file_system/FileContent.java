package com.coconaut.sce.file_system;

import com.coconaut.sce.utils.Utils;

public class FileContent {
    private String[] text = new String[1];
    private String path = "NaN";

    public void save() {
        Utils.write_file(this.getRawText(), this.path);
    }

    public String getName() {
        return Utils.get_file_from_path(this.path);
    }

    public String getExtension() {
        return Utils.get_file_extension_name_from_path(this.path);
    }

    public String getLine(int line) {
        return this.text[line];
    }

    public void setLine(int line, String text) {
        this.text[line] = text;
    }

    public String getChar(int idx, int line) {
        return String.valueOf(this.text[line].charAt(idx));
    }

    public String[] getText() {
        return this.text;
    }

    public String getRawText() {
        String text = "";
        for(String ln : this.text) {
            text += ln + "\n";
        }
        return text;
    }

    public void setTextFromRawText(String text) {
        this.setText(text.split("\n"));
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public int length() {
        return this.text.length;
    }

    public void setPath(String path) {
        if(Utils.file_exists(path)) {
            this.setText(Utils.read_file_by_line(path));
        } else{
            Utils.create_file(path);
            this.setTextFromRawText(" ");
        }
        this.path = path;
    }
}
