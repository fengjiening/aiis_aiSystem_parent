package org.jeecg.modules.ability.vo;

import java.io.Serializable;

/**
 * Created by fengjiening on 2020/6/11.
 */
public class FaceBox implements Serializable {
    /**
     * x1 : 100
     * y1 : 100
     * x2 : 100
     * y2 : 100
     */

    private String x1;
    private String y1;
    private String x2;
    private String y2;

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getY1() {
        return y1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getY2() {
        return y2;
    }

    public void setY2(String y2) {
        this.y2 = y2;
    }

    @Override
    public String toString() {
        return "FaceBox{" +
                "x1='" + x1 + '\'' +
                ", y1='" + y1 + '\'' +
                ", x2='" + x2 + '\'' +
                ", y2='" + y2 + '\'' +
                '}';
    }
}
