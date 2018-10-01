package com.coconaut.sce.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyboardInput implements KeyListener {

    private LinkedList<KeyboardListener> listeners = new LinkedList<KeyboardListener>();

    public KeyboardInput() {
    }

    public void keyPressed(KeyEvent arg0) {
        for(KeyboardListener l : listeners) {
            l.key_pressed(arg0);
        }
    }

    public void keyReleased(KeyEvent arg0) {
        for(KeyboardListener l : listeners) {
            l.key_released(arg0);
        }
    }

    public void keyTyped(KeyEvent arg0) {
        for(KeyboardListener l : listeners) {
            l.key_typed(arg0);
        }
    }

    public void add(KeyboardListener kbl) {
        this.listeners.add(kbl);
    }
}
