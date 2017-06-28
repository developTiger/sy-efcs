package com.wxzd.efcs.report.application.dtos;

import com.wxzd.efcs.business.application.dtos.PalletDetailDto;
import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.PalletChannelPolicy;
import com.wxzd.efcs.business.domain.enums.PalletDispatchStatus;
import com.wxzd.efcs.business.domain.enums.PalletStatus;
import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by LYK on 2017/4/19
 */
public class StorageBatteryDetailDto {

    private UUID id;

    @MemberIgnore
    private Integer xPos;

    @MemberIgnore
    private Integer yPos;

    @MemberIgnore
    private Integer zPos;

    @MemberIgnore
    private String pallet_no;

    @MemberIgnore
    private String house_no;

    /**
     * 当前表单
     */
    private String current_form_no;

    /**
     * 是否空托
     */
    private Boolean is_empty;

    /**
     * 通道策略
     */
    private PalletChannelPolicy channel_policy;

    /**
     * 调度状态
     */
    private PalletDispatchStatus dispatch_status;

    /**
     * 当前位置
     */
    private String current_pos;

    /**
     * 组盘时间
     */
    private Date palletize_complete_time;

    /**
     * 入线时间
     */
    private Date enter_time;

    /**
     * 当前工序开始时间
     */
    private Date current_procedure_time;

    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;

    /**
     * 拆盘时间
     */
    private Date pallet_split_time;


    private List<PalletDetailDto> betteryList;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getzPos() {
        return zPos;
    }

    public void setzPos(Integer zPos) {
        this.zPos = zPos;
    }

    public String getPallet_no() {
        return pallet_no;
    }

    public void setPallet_no(String pallet_no) {
        this.pallet_no = pallet_no;
    }

    public List<PalletDetailDto> getBetteryList() {
        return betteryList;
    }

    public void setBetteryList(List<PalletDetailDto> betteryList) {
        this.betteryList = betteryList;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getCurrent_form_no() {
        return current_form_no;
    }

    public void setCurrent_form_no(String current_form_no) {
        this.current_form_no = current_form_no;
    }

    public Boolean getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(Boolean is_empty) {
        this.is_empty = is_empty;
    }

    public PalletChannelPolicy getChannel_policy() {
        return channel_policy;
    }

    public void setChannel_policy(PalletChannelPolicy channel_policy) {
        this.channel_policy = channel_policy;
    }

    public PalletDispatchStatus getDispatch_status() {
        return dispatch_status;
    }

    public void setDispatch_status(PalletDispatchStatus dispatch_status) {
        this.dispatch_status = dispatch_status;
    }

    public String getCurrent_pos() {
        return current_pos;
    }

    public void setCurrent_pos(String current_pos) {
        this.current_pos = current_pos;
    }

    public Date getPalletize_complete_time() {
        return palletize_complete_time;
    }

    public void setPalletize_complete_time(Date palletize_complete_time) {
        this.palletize_complete_time = palletize_complete_time;
    }

    public Date getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(Date enter_time) {
        this.enter_time = enter_time;
    }

    public Date getCurrent_procedure_time() {
        return current_procedure_time;
    }

    public void setCurrent_procedure_time(Date current_procedure_time) {
        this.current_procedure_time = current_procedure_time;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }

    public Date getPallet_split_time() {
        return pallet_split_time;
    }

    public void setPallet_split_time(Date pallet_split_time) {
        this.pallet_split_time = pallet_split_time;
    }
}
