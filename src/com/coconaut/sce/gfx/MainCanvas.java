package com.coconaut.sce.gfx;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;

public class MainCanvas extends Canvas {
    public static int FLIP_H = 1;
    public static int FLIP_v = 0;

    private static final String FONT_EDITOR = "Monospaced";
    public static final Font FONT = new Font(FONT_EDITOR, Font.PLAIN, 14);

    private Graphics g;
    private BufferStrategy bs;

    public MainCanvas() {
        this.requestFocus();
        this.setFocusTraversalKeysEnabled(false);
    }

    public boolean start() {
        this.bs = this.getBufferStrategy();
        if(this.bs == null) {
            this.createBufferStrategy(3);
            return false;
        }
        this.g = bs.getDrawGraphics();
        return true;
    }

    public void end() {
        g.dispose();
        bs.show();
    }

    public void clear() {
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /*    public void render(Sprite spr, int x, int y, int w, int h, float opacity) {
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g.drawImage(spr.getOriginalImage(), x, y, w, h, null);
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }

        public void render(SpriteSheet sheet, double x, double y, int xx, int yy, int ww, int hh) {
            sheet.draw(x + camera.offsetX, y + camera.offsetY, xx, yy, ww, hh, g);
        }

        public void render(SpriteSheet sheet, double x, double y, int w, int h, int xx, int yy, int ww, int hh) {
            sheet.draw(x + camera.offsetX, y + camera.offsetY, w, h, xx, yy, ww, hh, g);
        }

        public void render(SpriteSheet sheet, double x, double y, int xx, int yy, int ww, int hh, int flip) {
            BufferedImage image = sheet.getImage(xx, yy, ww, hh);

            AffineTransform tx = AffineTransform.getScaleInstance(flip == 0 ? 1 : -1, flip == 0 ? -1 : 1);
            if(flip == 0) tx.translate(0, -image.getHeight(null));
            else if(flip == 1) tx.translate(-image.getWidth(null), 0);
            else {
                Log.error("int flip must be 0 or 1");
                return;
            }
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);


            g.drawImage(image, (int)(x + camera.offsetX), (int)(y + camera.offsetY), null);
        }

        public void render(Sprite spr, double x, double y) {
            spr.draw(x + camera.offsetX, y + camera.offsetY, g);
        }

        public void renderOnHUD(Sprite spr, double x, double y) {
            spr.draw(x, y, g);
        }

        public void renderOnHUD(SpriteSheet spr, double x, double y, int xx, int yy, int ww, int hh) {
            spr.draw(x, y, xx, yy, ww, hh, g);
        }

        public void render(Sprite spr, double x, double y, float opacity) {
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            spr.draw(x + camera.offsetX, y + camera.offsetY, g);
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }

        public void render(Sprite spr, double x, double y, int flip) {
            BufferedImage image = spr.getOriginalImage();

            AffineTransform tx = AffineTransform.getScaleInstance(flip == 0 ? 1 : -1, flip == 0 ? -1 : 1);
            if(flip == 0) tx.translate(0, -image.getHeight(null));
            else if(flip == 1) tx.translate(-image.getWidth(null), 0);
            else {
                Log.error("int flip must be 0 or 1");
                return;
            }
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);


            g.drawImage(image, (int)(x + camera.offsetX), (int)(y + camera.offsetY), null);
        }
    */
    public void drawRect(Rectangle r, Color c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(c);
        g2d.draw(r);
    }

    public void drawRect(int x, int y, int w, int h, Color c) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(c);
        Rectangle rr = new Rectangle(x, y, w, h);
        g2d.draw(rr);
    }

    public void drawRect(Rectangle r, float opacity) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle rr = new Rectangle(r.x, r.y, r.width, r.height);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g2d.setColor(new Color(0, 0, 0));
        g2d.draw(rr);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    public void drawRect(Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        Rectangle rr = new Rectangle(r.x, r.y, r.width, r.height);
        g2d.draw(rr);
    }


    public void renderText(String s, int x, int y, int size, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font(FONT_EDITOR, Font.PLAIN, size));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g.setColor(color);
        g.drawString(s, x, y);
    }

    public void setColor(Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
    }

    public void fill(int x, int y, int w, int h, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setColor(color);
        g2d.fillRect(x, y, w, h);
    }

    public void fill(int x, int y, int w, int h, Color color, float a) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2d.setColor(color);
        g2d.fillRect(x, y, w, h);
    }

    public int calcTextWidth(String msg) {
        return g.getFontMetrics().stringWidth(msg);
    }

    public int calcTextHeight(String msg) {
        return (int)g.getFontMetrics().getLineMetrics(msg, g).getHeight();
    }

    public Graphics getG() {
        return g;
    }
}
