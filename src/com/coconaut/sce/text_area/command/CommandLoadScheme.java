package com.coconaut.sce.text_area.command;

import com.coconaut.sce.text_area.TextArea;

public class CommandLoadScheme extends Command {
    public  CommandLoadScheme(TextArea text_area) {
        super(text_area);
        this.name = "Create Load Scheme";
    }

    public void exec(String args[]) {
        this.text_area.load_scheme(args[0]);
    }
}
