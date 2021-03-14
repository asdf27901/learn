package org.example.variable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointClass {

    @SerializedName("placeNo")
    private Integer placeNo;
    @SerializedName("pointValue")
    private String pointValue;
    @SerializedName("message")
    private String message;

    public Integer getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(Integer placeNo) {
        this.placeNo = placeNo;
    }

    public String getPointValue() {
        return pointValue;
    }

    public void setPointValue(String pointValue) {
        this.pointValue = pointValue;
    }

}
