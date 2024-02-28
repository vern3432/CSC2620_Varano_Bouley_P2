package com.photoeditor;

import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import java.awt.Graphics;


/**
 * The line class creates a new line appearing on a JPanel
 */
public class Line {

    Point start;
    Point end;
    java.awt.Color color;

    public Line(Point start, Point end, java.awt.Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    
    /** 
     * @param g
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(0, 0, 0, 0);
    }

    public Point getStartPoint() {
        return start;
    }

    public Point getEndPoint() {
        return end;
    }

}
