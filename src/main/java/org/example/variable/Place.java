package org.example.variable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Place {

    @SerializedName("places")
    private List<PlaceInfo> placeInfoList;

    public List<PlaceInfo> getPlaceInfoList() {
        return placeInfoList;
    }

    public void setPlaceInfoList(List<PlaceInfo> placeInfoList) {
        this.placeInfoList = placeInfoList;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeInfoList=" + placeInfoList +
                '}';
    }
}
