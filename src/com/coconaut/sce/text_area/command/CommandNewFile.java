package com.coconaut.sce.text_area.command;

import com.coconaut.sce.text_area.TextArea;

public class CommandNewFile extends Command {
    public  CommandNewFile(TextArea text_area) {
        super(text_area);
        this.name = "Create New File";
    }

    public void exec(String args[]) {
        this.text_area.new_file(args[0]);
    }
}
