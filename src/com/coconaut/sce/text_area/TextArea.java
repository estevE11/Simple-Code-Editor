package com.coconaut.sce.text_area;

import com.coconaut.sce.file_system.FileContent;
import com.coconaut.sce.gfx.MainCanvas;
import com.coconaut.sce.input.KeyboardListener;
import com.coconaut.sce.utils.Utils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class TextArea extends KeyboardListener {
    public Hashtable<String, Color> scheme = new Hashtable<>();
    public Hashtable<String, Color> syntax = new Hashtable<>();

    private boolean onFocus = true;

    private int current_width = 0;
    private int current_height = 0;

    private int FONT_SIZE = 16;
    private int FONT_W = 8;
    private int LINE_H = 18;
    private int OFF_LEFT = 7;

    private boolean saved = false;
    private FileContent current_file = new FileContent();

    private int viewY = 0;
    private int offsetY = 0;

    private int line = 0;
    private int idx = 0;
    private int idx_target = idx;

    private int idx_t = 0;
    private int idx_timing = 400;

    private Point selec_start = new Point(0, 0);
    private Point selec_end = new Point(0, 0);
    private Point selec_src;

    private CommandInput cmd_input;

    public TextArea() {
        init();
    }

    private void init() {
        this.cmd_input = new CommandInput(this);
        this.cmd_input.initCommands();
        this.current_file.setLine(0, "");
        this.loadScheme("default_dark");
        this.loadSyntax("txt");
    }

    public void update(long dt) {
        this.idx_t+=dt/2;
        if(this.idx_t > idx_timing) this.idx_t = 0;
        
        //Offset
        //if(this.viewY*this.LINE_H > this.current_file.length() - this.LINE_H) this.offsetY = this.current_file.length() - this.LINE_H;
        if(this.viewY < 0) this.viewY = 0;

        this.offsetY = this.viewY * -1;
        
        this.cmd_input.update();
    }

    public void render(MainCanvas canvas) {
        this.current_width = canvas.getWidth();
        this.current_height = canvas.getHeight()-this.LINE_H;

        if(this.idx < 0) this.idx = 0;

        //System.out.println(viewY);

        canvas.fill(0, 0, canvas.getWidth(), canvas.getHeight(), this.scheme.get("txt_area_bg"));

        //LINE BAAKGROUND
        canvas.fill(0, this.line*this.LINE_H + this.offsetY, canvas.getWidth(), this.LINE_H, this.scheme.get("txt_area_bg_ln"));

        //SELECTION
        int diff = Math.abs(this.selec_end.y - this.selec_start.y);
        if(!(this.selec_start.y == this.selec_end.y && this.selec_start.x == this.selec_end.x)) {
            for (int i = 0; i < diff + 1; i++) {
                if(this.selec_start.y == this.selec_end.y) {
                    if(this.selec_start.x < this.selec_end.x) {
                        canvas.fill(OFF_LEFT + this.selec_start.x * this.FONT_W, this.selec_start.y * this.LINE_H + this.offsetY, (this.selec_end.x - this.selec_start.x) * this.FONT_W, this.LINE_H, this.scheme.get("txt_area_selection"));
                    }
                    if(this.selec_start.x > this.selec_end.x) {
                        canvas.fill(OFF_LEFT + (this.selec_start.x+this.FONT_W) * this.FONT_W, this.selec_start.y * this.LINE_H + this.offsetY, (this.selec_end.x - this.selec_start.x) * this.FONT_W, this.LINE_H, this.scheme.get("txt_area_selection"));
                    }
                } else if(diff > 0) {
                    canvas.fill(OFF_LEFT + (this.selec_start.x) * this.FONT_W, this.selec_start.y * this.LINE_H + this.offsetY, (this.current_file.getLine(this.selec_start.y).length() - this.selec_start.x) * this.FONT_W, this.LINE_H, this.scheme.get("txt_area_selection"));
                    for(int j = this.selec_start.y+1;  j < this.selec_end.y; j++) {

                        canvas.fill(OFF_LEFT, j * this.LINE_H + this.offsetY, this.current_file.getLine(j).length() * this.FONT_W, this.LINE_H, this.scheme.get("txt_area_selection"));
                    }
                    canvas.fill(OFF_LEFT, this.selec_end.y * this.LINE_H + this.offsetY, this.selec_end.x * this.FONT_W, this.LINE_H, this.scheme.get("txt_area_selection"));
                }
            }
        }

        //Text
        for(int i = 0; i < this.current_file.length(); i++) {
            String[] line = this.current_file.getLine(i).split(" ");
            String[] words = this.current_file.getLine(i).split("[^a-zA-Z0-9]");
            for(int j = 0; j < line.length; j++) {
                System.out.println(line[j]);
                canvas.renderText(line[j] + (j == line.length - 1 ? "" : " "), OFF_LEFT, 14 + i * this.LINE_H + this.offsetY, 14, this.getSyntax(line[j]));
            }
        }

        //POinter
        if(idx_t < idx_timing/2 && canvas.hasFocus() && this.onFocus) {
            canvas.fill((this.idx+1) * this.FONT_W, this.line*this.LINE_H + this.offsetY, 7, this.LINE_H, this.scheme.get("txt_area_idx"));
            if(this.idx < this.current_file.getLine(line).length() && this.current_file.getLine(line) != null && this.current_file.getLine(line).length() > 0){
                char c = this.current_file.getLine(line).charAt(this.idx);
                if(!String.valueOf(c).equals(" "))canvas.renderText(String.valueOf(c), OFF_LEFT+this.idx*this.FONT_W, 14+this.line*this.LINE_H + this.offsetY, 14, this.scheme.get("txt_area_font_on_idx"));
            }
        }

        //INFO BAR
        canvas.fill(0, canvas.getHeight()-this.LINE_H*2, canvas.getWidth(), this.LINE_H, this.scheme.get("info_bar_bg"));
        //Line : Idx text
        String t0 = (this.line+1) + ":" + this.idx;
        canvas.renderText(t0, 7, canvas.getHeight()-this.LINE_H-5, 14, this.scheme.get("info_bar_font"));

        //File name
        String t1 = this.current_file.getName() + (saved ? "" : "*");
        canvas.renderText(t1, 100, canvas.getHeight()-this.LINE_H-5, 14, this.scheme.get("info_bar_font"));

        //Extension
        String t2 = this.current_file.getExtension().toUpperCase();
        canvas.renderText(t2, canvas.getWidth() - (canvas.calcTextWidth(t2)+7), canvas.getHeight()-this.LINE_H-5, 14, this.scheme.get("info_bar_font"));


        this.cmd_input.render(canvas);
    }


    private void procces_input(String input_char, int keycode, boolean ctrl, boolean shift) {
        String to_add = input_char;
        System.out.println(input_char);
        if(ctrl) {
            switch(keycode) {
                case 8:
                    this.remove_chars(2);
                    to_add = "NaN";
                    break;
                case 17:
                case 18:
                    to_add = "NaN";
                    break;
                case 67://COPY
                    this.copy(this.selec_start, this.selec_end);
                    to_add = "NaN";
                    break;
                case 83:
                    this.saveFile();
                    to_add = "NaN";
                    break;
                case 86://PASTE
                    this.paste(Utils.get_clipboard());
                    to_add = "NaN";
                    break;

                    //Arrows
                case 38:
                    this.viewY -= this.LINE_H;
                    to_add = "NaN";
                    break;
                case 40:
                    this.viewY += this.LINE_H;
                    to_add = "NaN";
                    break;
                default:
                    this.cmd_input.openCommand(keycode);
                    break;
            }
        } else {
            if(shift) {
                switch(keycode) {
                    //Arrows
                    case 38:
                        this.move_selection(0, -1);
                        to_add = "NaN";
                        break;
                    case 40:
                        this.move_selection(0, 1);
                        to_add = "NaN";
                        break;
                    case 37:
                        this.move_selection(-1, 0);
                        to_add = "NaN";
                        break;
                    case 39:
                        this.move_selection(1, 0);
                        to_add = "NaN";
                        break;
                }
            }

            switch(keycode) {
                case 10:
                    this.add_line();
                    to_add = "NaN";
                    break;
                case 8:
                    this.remove_chars(1);
                    to_add = "NaN";
                    break;
                case 155://INSERT
                    to_add = "NaN";
                    break;
                case 127://SUPR
                    this.remove_chars(-1);
                    to_add = "NaN";
                    break;
                case 35://FIN
                    to_add = "NaN";
                    break;

                //Arrows
                case 38:
                    move_line(-1);
                    to_add = "NaN";
                    break;
                case 40:
                    move_line(1);
                    to_add = "NaN";
                    break;
                case 37:
                    move_idx(-1);
                    to_add = "NaN";
                    break;
                case 39:
                    move_idx(1);
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
            if(!shift)selec_follow_idx();
        }

        System.out.println(to_add);
        if(!to_add.equals("NaN")){
            add_char(to_add);
            this.saved = false;
        }

        if(this.idx < 0) this.idx = 0;

        System.out.println(keycode);
        System.out.println("----");
    }

    private void selec_follow_idx() {
        this.selec_start = new Point(this.idx, this.line);
        this.selec_end = new Point(this.idx, this.line);
    }

    private void move_selection(int x, int y) {
        if(x != 0 && this.selec_start.x == this.selec_end.x) {
            if(x > -1) {
                this.selec_src = this.selec_start;
                this.selec_end.x+=x;
            } else {
                this.selec_src = this.selec_end;
                this.selec_start.x+=x;
            }
        } else if(this.selec_src == this.selec_start) {
            if(this.selec_end.x+x > -1 && this.selec_end.x+x < this.current_file.getLine(this.line).length()+1)
                this.selec_end.x+=x;
        } else if(this.selec_src == this.selec_end) {
            if(this.selec_start.x+x > -1 && this.selec_start.x+x < this.current_file.getLine(this.line).length()+1)
                this.selec_start.x+=x;
        }

        if(y != 0 && this.selec_start.y == this.selec_end.y) {
            if(y > -1) {
                this.selec_src = this.selec_start;
                this.selec_end.y+=y;
            } else {
                this.selec_src = this.selec_end;
                this.selec_start.y+=y;
            }
        } else if(this.selec_src == this.selec_start) {
            if(this.selec_end.y+y > -1 && this.selec_end.y+y < this.current_file.length()+1)
                this.selec_end.y+=y;
        } else if(this.selec_src == this.selec_end) {
            if(this.selec_start.y+y > -1 && this.selec_start.y+y < this.current_file.length()+1)
                this.selec_start.y+=y;
        }
    }

    private void add_line() {
        this.current_file.setText(Utils.push(this.current_file.getText(), this.current_file.getLine(this.line).substring(this.idx, this.current_file.getLine(this.line).length()), this.line));
        this.current_file.setLine(this.line, this.current_file.getLine(this.line).substring(0, this.idx));
        this.move_line(1);
        this.idx = 0;
    }

    private void add_line(int line) {
        this.current_file.setText(Utils.push(this.current_file.getText(), this.current_file.getLine(this.line).substring(this.idx, this.current_file.getLine(this.line).length()), line));
        this.current_file.setLine(this.line, this.current_file.getLine(this.line).substring(0, this.idx));
        this.move_line(1);
        this.idx = 0;
    }

    private void add_empty_line(int line) {
        this.current_file.setText(Utils.push(this.current_file.getText(), this.current_file.getLine(this.line).substring(this.idx, this.current_file.getLine(this.line).length()), line));
        this.line++;
        this.current_file.setLine(this.line, "");
        this.idx = 0;
    }

    private void remove_chars(int chars) {
        if(chars > 0) { // BACKSPACE
            if(idx == 0) {
                if(this.line == 0) return;
                String lineCont = this.current_file.getLine(this.line);
                this.current_file.setText(Utils.pop(this.current_file.getText(), this.line));
                this.move_line(-1);
                this.current_file.setLine(this.line,  this.current_file.getLine(this.line).concat(lineCont));
                this.idx = this.current_file.getLine(this.line).length() - lineCont.length();
                return;
            }
            if(this.current_file.getLine(line).length() < chars) chars = this.current_file.getLine(line).length();
            if (this.current_file.getLine(line) != null && this.current_file.getLine(line).length() > 0 && this.idx > chars-1) {
                String fstpart = this.current_file.getLine(line).substring(0, idx-chars);
                String scpart = this.current_file.getLine(line).substring(idx, this.current_file.getLine(line).length());
                this.current_file.setLine(line, fstpart + scpart);
                this.move_idx(-chars);
            }
        } else if(chars < 0) { // SUPR
            chars = Math.abs(chars);
            if(this.current_file.getLine(line).length() < chars) chars = this.current_file.getLine(line).length();
            if (this.current_file.getLine(line) != null && this.current_file.getLine(line).length() > 0) {
                String fstpart = this.current_file.getLine(line).substring(0, idx);
                String scpart = this.current_file.getLine(line).substring(idx+chars, this.current_file.getLine(line).length());
                this.current_file.setLine(line, fstpart + scpart);
            }
        }
    }

    private void copy(Point idx_start, Point idx_end) {
        String s = "";

        if(idx_start.y > idx_end.y) throw new NullPointerException("idx_end must be higher or equal than idx_start.");

        if(idx_start.y == idx_end.y) s = this.current_file.getLine(idx_start.y).substring(idx_start.x, idx_end.x);
        else {
            s += (this.current_file.getLine(idx_start.y).substring(idx_start.x, this.current_file.getLine(idx_start.y).length()) + "\n");
            for(int i = idx_start.y+1;  i < idx_end.y; i++) {
                s += (this.current_file.getLine(i) + "\n");
            }
            s += (this.current_file.getLine(idx_end.y).substring(0, idx_end.x));
        }

        Utils.copy_to_clipboard(s);
    }

    private void paste(String data) {
        String[] lines = Utils.get_lines_from_raw_text(data);
        if(lines.length == 1) {
            String fstpart = (this.current_file.getLine(line).length() == 0 || idx == 0 ? "" : this.current_file.getLine(line).substring(0, idx));
            String scpart = (this.current_file.getLine(line).length() == 0 || idx == this.current_file.getLine(line).length() ? "" : this.current_file.getLine(line).substring(idx, this.current_file.getLine(line).length()));
            this.current_file.setLine(line, fstpart + data + scpart);
            this.move_idx(data.length());
        } else if(lines.length > 1) {
            String fstpart = (this.current_file.getLine(line).length() == 0 || idx == 0 ? "" : this.current_file.getLine(line).substring(0, idx));
            String scpart = (this.current_file.getLine(line).length() == 0 || idx == this.current_file.getLine(line).length() ? "" : this.current_file.getLine(line).substring(idx, this.current_file.getLine(line).length()));
            this.current_file.setLine(this.line, fstpart + lines[0]);
            this.add_empty_line(this.line);
            for(int i = 1; i < lines.length-1; i++) {
                this.current_file.setLine(this.line, lines[i]);
                this.add_empty_line(this.line);
            }
            this.current_file.setLine(this.line, lines[lines.length-1] + scpart);
        }
    }

    private void move_idx(int steps) {
        if(this.idx+steps > -1 && this.idx+steps < this.current_file.getLine(this.line).length()+1) this.idx+=steps;
        this.idx_target = this.idx;
        this.idx_t = 0;
    }

    private void remove_selected() {

    }

    private void move_line(int steps) {
        if(this.line+steps > -1 && this.line+steps < this.current_file.length())this.line += steps;
        this.idx_t = 0;

        if(this.idx_target > this.current_file.getLine(this.line).length()) this.idx = this.current_file.getLine(this.line).length();
        else this.idx = this.idx_target;

        if(this.line < this.current_height) this.offsetY--;
        if(this.line > -1) this.offsetY++;
    }

    private void add_char(String c) {
        String fstpart = (this.current_file.getLine(line).length() == 0 || idx == 0 ? "" : this.current_file.getLine(line).substring(0, idx));
        String scpart = (this.current_file.getLine(line).length() == 0 || idx == this.current_file.getLine(line).length() ? "" : this.current_file.getLine(line).substring(idx, this.current_file.getLine(line).length()));
        this.current_file.setLine(line, fstpart + c + scpart);
        this.idx++;
    }

    public void openFile(String path) {
        FileContent file = new FileContent();
        file.setPath(path);
        this.current_file = file;
        this.loadSyntax(this.current_file.getExtension());
        this.saved = true;
    }

    public void saveFile() {
        this.current_file.save();
        this.saved = true;
    }

    private void loadScheme(String name) {
        String[] file = Utils.readFileByLine("config/schemes/scheme_" + name + ".txt");
        for(String line : file) {
            String[] prop = line.split(" ");
            this.scheme.put(prop[0], new Color(Integer.valueOf(prop[1]), Integer.valueOf(prop[2]), Integer.valueOf(prop[3])));
        }
        System.out.println("Scheme loaded: " + name);
    }

    private void loadSyntax(String ext) {
        if(ext.equals("txt")) {
            this.syntax = new Hashtable<>();
            return;
        }

        String[] file = Utils.readFileByLine("config/syntax/syntax_" + ext + ".txt");
        for(String line : file) {
            this.syntax = new Hashtable<>();
            String[] prop = line.split(" ");
            this.syntax.put(prop[0], new Color(Integer.valueOf(prop[1]), Integer.valueOf(prop[2]), Integer.valueOf(prop[3])));
        }
    }

    private Color getSyntax(String word) {
        Color res = this.syntax.get(word);
        if(res != null) return res;
        else return this.scheme.get("txt_area_font");
    }

    public void setFocus(boolean b) {
        this.onFocus = b;
    }

    public void key_pressed(KeyEvent arg0) {
        if(this.onFocus)this.procces_input(String.valueOf(arg0.getKeyChar()), arg0.getKeyCode(), arg0.isControlDown(), arg0.isShiftDown());
    }

    public void key_released(KeyEvent arg0) {
    }

    public void key_typed(KeyEvent arg0) {
    }

    public CommandInput getCmdInpt() {
        return this.cmd_input;
    }
}
