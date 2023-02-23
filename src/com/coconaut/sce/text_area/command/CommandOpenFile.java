package com.coconaut.sce.text_area.command;

import com.coconaut.sce.text_area.TextArea;

public class CommandOpenFile extends Command {
    public CommandOpenFile(TextArea text_area) {
        super(text_area);
        this.name = "Open File";
    }

    public void exec(String args[]) {
        this.text_area.open_file(args[0]);
    }

    public String baseInput() {
        return System.getProperty("user.dir");
    }
}
