package com.wxzd.efcs.business.application.dtos;

/**
 * Created by zhouzh on 2017/4/22.
 */
public class PalletLocationDto {


    private String container_no;

    private Integer x_pos;

    private Integer y_pos;


    private Integer z_pos;

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public Integer getX_pos() {
        return x_pos;
    }

    public void setX_pos(Integer x_pos) {
        this.x_pos = x_pos;
    }

    public Integer getY_pos() {
        return y_pos;
    }

    public void setY_pos(Integer y_pos) {
        this.y_pos = y_pos;
    }

    public Integer getZ_pos() {
        return z_pos;
    }

    public void setZ_pos(Integer z_pos) {
        this.z_pos = z_pos;
    }
}
