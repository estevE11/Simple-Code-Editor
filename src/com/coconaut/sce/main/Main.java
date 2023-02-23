package com.coconaut.sce.main;

import com.coconaut.sce.gfx.MainCanvas;
import com.coconaut.sce.input.KeyboardInput;
import com.coconaut.sce.text_area.TextArea;

import javax.swing.*;
import java.awt.*;

public class Main implements Runnable {

    private JFrame frame;
    private final String frame_name = "SCE - Untitled";
    private int WIDTH = 400, HEIGHT = 300;
    private boolean frame_resizeable = true;

    private MainCanvas canvas;
    private TextArea text_area;
    private KeyboardInput kb;

    private void start_thread(String thread_name) {
        Thread thread = new Thread(this, thread_name);
        thread.start();
    }

    private void init() {
        this.frame.setSize(1080, 720);
        this.text_area = new TextArea();
        this.kb = new KeyboardInput();
        this.kb.add(this.text_area);
        this.kb.add(this.text_area.getCmdInpt());
        canvas.addKeyListener(this.kb);

        this.text_area.open_file("/home/esteve/Escritorio/b.txt");
    }

    @Override
    public void run() {
        init();
        int fps = 0;
        long old_time = System.currentTimeMillis();
        long prev_time = old_time;
        while(true) {
            long current_time = System.currentTimeMillis();
            long dt = current_time - prev_time;
            prev_time = current_time;
            update(dt);
            render();
            fps++;
            long _dt = current_time - old_time;
            if(_dt >= 1000) {
                this.frame.setTitle("SCE - ( FPS: " + fps + " )");
                fps = 0;
                old_time = current_time;
            }
        }
    }

    private void update(long dt) {
        this.text_area.update(dt);
    }

    private void render() {
        if(!canvas.start()) return;
        this.text_area.render(canvas);
        canvas.end();
    }

    public Point getFrameCurrentSize() {
        return new Point(this.frame.getSize().width, this.frame.getSize().height);
    }

    public void start() {
        this.frame = new JFrame(this.frame_name);
        this.canvas = new MainCanvas();

        this.frame.setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
        this.frame.setMinimumSize(new Dimension(this.WIDTH, this.HEIGHT));

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(this.frame_resizeable);
        this.frame.setVisible(true);

        this.frame.add(canvas);

        this.frame.requestFocus();

        this.frame.requestFocus();

        this.start_thread("Main Thread");
    }


}
