package org.jeecg.modules.ability.vo;

/**
 * Created by fengjiening on 2020/6/11.
 */
public class PicDataInput {


    /**
     * timeStamp : 1955412151
     * picWidth : 1440
     * picHeight : 1080
     * pixelDepth : 3
     * picData : 6Zi/5aSn5aOw6YGT5ZWK5LiJ5omT5LiJ6Ziy5ZWK5rKZ5Y+R5ZWK5rKZ5Y+R6aOe5rSS6JCo6Iqs5ZWK5rKZ5Y+R6Zi/5pa557SiYQ==
     * minHeadSize : 500
     * totalFrame : 3
     * xRange : 0.15
     * yRange : 0.1
     */

    private String timeStamp;
    private int picWidth;
    private int picHeight;
    private int pixelDepth;
    private String picData;
    private int minHeadSize;
    private int totalFrame;
    private float xRange;
    private float yRange;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public int getPixelDepth() {
        return pixelDepth;
    }

    public void setPixelDepth(int pixelDepth) {
        this.pixelDepth = pixelDepth;
    }

    public String getPicData() {
        return picData;
    }

    public void setPicData(String picData) {
        this.picData = picData;
    }

    public int getMinHeadSize() {
        return minHeadSize;
    }

    public void setMinHeadSize(int minHeadSize) {
        this.minHeadSize = minHeadSize;
    }

    public int getTotalFrame() {
        return totalFrame;
    }

    public void setTotalFrame(int totalFrame) {
        this.totalFrame = totalFrame;
    }

    public float getXRange() {
        return xRange;
    }

    public void setXRange(float xRange) {
        this.xRange = xRange;
    }

    public float getYRange() {
        return yRange;
    }

    public void setYRange(float yRange) {
        this.yRange = yRange;
    }
}
