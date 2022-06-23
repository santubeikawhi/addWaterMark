package com.converter.jwatermark.demo.pdf.pdfbox;

public class Point {
    
    private float x;
    
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void applyRotation(double theta) {
        Double xPrime = x * Math.cos(theta) - y * Math.sin(theta);
        Double yPrime = y * Math.cos(theta) + x * Math.sin(theta);

        x = xPrime.floatValue();
        y = yPrime.floatValue();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
