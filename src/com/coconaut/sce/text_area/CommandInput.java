package com.coconaut.sce.text_area;

import com.coconaut.sce.gfx.MainCanvas;
import com.coconaut.sce.input.KeyboardListener;
import com.coconaut.sce.text_area.command.Command;
import com.coconaut.sce.text_area.command.CommandOpenFile;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

/*
*
#COMPLETED - Can be better...

data = [10, 15, 3, 7, 2, 5, 6, 7, 2, 4]

def check_sum(data, k):
    for i in range(0, len(data)-1):
        for j in range(i+1, len(data)):
            if(data[i]+data[j] == k):
                return True
    return False

print(check_sum(data, 11))
* */

public class CommandInput extends KeyboardListener {
    private Hashtable<Integer, Command> commands = new Hashtable<>();

    private Color color_background = new Color(46, 46, 46);
    private Color color_background_line = new Color(73, 73, 73);
    private Color color_idx = new Color(255, 255, 255);
    private Color color_font = new Color(255, 255, 255);
    private Color color_font_on_idx = new Color(46, 46, 46);
    private Color color_selection = new Color(0, 120, 255);

    private boolean onFocus = false;

    private Command command;
    private String input = "";

    private int FONT_SIZE = 16;
    private int FONT_W = 8;
    private int LINE_H = 18;
    private int OFF_LEFT = 7;

    private TextArea text_area;

    public CommandInput(TextArea text_area) {
        this.text_area = text_area;
    }

    public void initCommands() {
        this.addCommand(79, new CommandOpenFile(this.text_area));
    }

    private void addCommand(int keybind, Command cmd) {
        this.commands.put(keybind, cmd);
    }

    public void update() {

    }

    public void render(MainCanvas canvas) {
        canvas.fill(0, canvas.getHeight()-this.LINE_H, canvas.getWidth(), this.LINE_H, this.text_area.scheme.get("txt_area_bg"));
        if(this.command != null)
            canvas.renderText(this.command.getName() + ": " + this.input, 7, canvas.getHeight() - 5, 14, this.text_area.scheme.get("txt_area_font"));
    }

    public void openCommand(int keybind) {
        System.out.println("-" + keybind);
        Command cmd = this.commands.get(keybind);
        if(cmd != null) {
            this.command = cmd;
            this.onFocus = true;
            this.text_area.setFocus(false);
            return;
        }
        this.onFocus = false;
        this.text_area.setFocus(true);
    }

    private void execCommand() {
        this.command.exec(this.input.split(" "));
        this.escape();
    }

    private void procces_input(String input_char, int keycode, boolean ctrl, boolean shift) {
        if(ctrl) return;
        if(keycode == 27) this.escape();
        if(keycode == 10) this.execCommand();

        String to_add = input_char;

        switch(keycode) {
            case 8:
                this.input = this.input.substring(0, this.input.length()-1);
                to_add = "NaN";
                break;

            //NaN Characters :D
            case 33:
            case 34:
            case 16:
            case 17:
            case 18:
            case 27:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 145:
            case 19:
            case 525:
            case 20:
                to_add = "NaN";
                break;
        }

        if(!to_add.equals("NaN")){
            this.input+=to_add;
        }
    }

    public void escape() {
        this.onFocus = false;
        this.text_area.setFocus(true);
        this.command = null;
        this.input = "";
    }

    public void key_pressed(KeyEvent arg0) {
        if(this.onFocus)this.procces_input(String.valueOf(arg0.getKeyChar()), arg0.getKeyCode(), arg0.isControlDown(), arg0.isShiftDown());
    }

    public void key_released(KeyEvent arg0) {
    }

    public void key_typed(KeyEvent arg0) {
    }

}
