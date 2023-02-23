package com.coconaut.sce.text_area.command;

import com.coconaut.sce.text_area.TextArea;

public class Command {
    protected String name = "Command name not found";
    protected TextArea text_area;

    public Command(TextArea text_area) {
        this.text_area = text_area;
    }


    public void exec(String args[]) {

    }

    public String baseInput() {
        return "";
    }

    public String getName() {
        return this.name;
    }
}
