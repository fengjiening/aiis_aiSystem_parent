package org.jeecg.modules.ability.vo;

import java.io.Serializable;

/**
 * Created by fengjiening on 2020/6/11.
 */
public class FaceResult implements Serializable {

    /**
     * faceBox : {"x1":100,"y1":100,"x2":100,"y2":100}
     * match : true
     * stable : true
     * result :
     * timestamp : 1955412151
     */


    private FaceBox faceBox;
    private Integer match;
    private boolean stable;
    private boolean center;
    private UserFaceInfo result;
    private String timestamp;


    public FaceResult(FaceBox faceBox, UserFaceInfo result) {
        this.faceBox = faceBox;
        this.result = result;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public FaceBox getFaceBox() {
        return faceBox;
    }

    public void setFaceBox(FaceBox faceBox) {
        this.faceBox = faceBox;
    }

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public UserFaceInfo getResult() {
        return result;
    }

    public void setResult(UserFaceInfo result) {
        this.result = result;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FaceResult{" +
                ", faceBox=" + faceBox==null?null:faceBox.toString()  +
                ", match=" + match +
                ", stable=" + stable +
                ", result=" + result==null?null:result.toString()  +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

