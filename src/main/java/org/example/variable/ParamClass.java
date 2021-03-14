package org.example.variable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParamClass {

    @SerializedName("ex")
    private List<PointClass> ex;

    public List<PointClass> getEx() {
        return ex;
    }

    public void setEx(List<PointClass> ex) {
        this.ex = ex;
    }

}
