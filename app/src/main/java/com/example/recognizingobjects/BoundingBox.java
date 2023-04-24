package com.example.recognizingobjects;
public class BoundingBox {
    private String label;
    private float score;
    private float xmin;
    private float ymin;
    private float xmax;
    private float ymax;

    public BoundingBox(String label, float score, float xmin, float ymin, float xmax, float ymax) {
        this.label = label;
        this.score = score;
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    public String getLabel() {
        return label;
    }

    public float getScore() {
        return score;
    }

    public float getXmin() {
        return xmin;
    }

    public float getYmin() {
        return ymin;
    }

    public float getXmax() {
        return xmax;
    }

    public float getYmax() {
        return ymax;
    }
}
