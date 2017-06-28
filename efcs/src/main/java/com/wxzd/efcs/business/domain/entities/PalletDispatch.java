package com.wxzd.efcs.business.domain.entities;

/***********************************************************************
 * Module:  fcs_palletized_cargo.java
 * Author:  zhouzh
 * Purpose: Defines the Class fcs_palletized_cargo
 ***********************************************************************/

import java.util.*;

import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.ddd.domain.entities.EfcsErrorEntity;
import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.core.domain.entities.SkuCatgRequireField;
import com.wxzd.wms.ddd.relation.OneToMany;

/**
 * 托盘调度记录
 */
@AliasName("fcs_pallet_dispatch")
public class PalletDispatch extends EfcsErrorEntity {

    /**
     * 拉线
     */
    private UUID house_id;
    /**
     * 容器Id
     */
    private String container_id;
    /**
     * 容器条码
     */
    private String container_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;


    private PalletSplitPolicy split_policy;
    /**
     * 通道策略
     */
    private PalletChannelPolicy channel_policy;

    private String  storage_location;
    /**
     * 是否空托
     */
    private Boolean is_empty = true;
    /**
     * 入线时间
     */
    private Date enter_time;
    /**
     * 组盘时间
     */
    private Date palletize_complete_time;
    /**
     * 当前工序开始时间
     */
    private Date current_procedure_time;
    /**
     * 拆盘时间
     */
    private Date pallet_split_time;
    /**
     * 调度状态
     */
    private PalletDispatchStatus dispatch_status;
    /**
     * 位置类型
     */
    private PositionType pos_type;
    /**
     * 当前位置
     */
    private String current_pos;

    private String current_form_no;

    private Integer loc_change_times;


    private String procedure_route = "";


//    @OneToMany(domain = PalletDetail.class,foreignKey = "pallet_dispatch_id")
//    // TODO 添加映射关系
//    private Collection<PalletDetail> PalletDetail;
//    // TODO 添加映射关系
//    private Collection<PalletMoveDetail> PalletMoveDetail;


    public UUID getHouse_id() {
        return house_id;
    }

    public void setHouse_id(UUID house_id) {
        this.house_id = house_id;
    }

    public String getContainer_id() {
        return container_id;
    }

    public void setContainer_id(String container_id) {
        this.container_id = container_id;
    }

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public WorkProcedure getWork_procedure() {
        return work_procedure;
    }

    public void setWork_procedure(WorkProcedure work_procedure) {
        this.work_procedure = work_procedure;
    }

    public PalletStatus getPallet_status() {
        return pallet_status;
    }

    public void setPallet_status(PalletStatus pallet_status) {
        this.pallet_status = pallet_status;
    }

    public PalletChannelPolicy getChannel_policy() {
        return channel_policy;
    }

    public void setChannel_policy(PalletChannelPolicy channel_policy) {
        this.channel_policy = channel_policy;
    }

    public Boolean getIs_empty() {
        return is_empty;
    }

    public void setIs_empty(Boolean is_empty) {
        this.is_empty = is_empty;
    }

    public Date getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(Date enter_time) {
        this.enter_time = enter_time;
    }

    public Date getPalletize_complete_time() {
        return palletize_complete_time;
    }

    public void setPalletize_complete_time(Date palletize_complete_time) {
        this.palletize_complete_time = palletize_complete_time;
    }

    public Date getCurrent_procedure_time() {
        return current_procedure_time;
    }

    public void setCurrent_procedure_time(Date current_procedure_time) {
        this.current_procedure_time = current_procedure_time;
    }

    public Date getPallet_split_time() {
        return pallet_split_time;
    }

    public void setPallet_split_time(Date pallet_split_time) {
        this.pallet_split_time = pallet_split_time;
    }

    public PalletDispatchStatus getDispatch_status() {
        return dispatch_status;
    }

    public void setDispatch_status(PalletDispatchStatus dispatch_status) {
        this.dispatch_status = dispatch_status;
    }

    public PositionType getPos_type() {
        return pos_type;
    }

    public void setPos_type(PositionType pos_type) {
        this.pos_type = pos_type;
    }

    public String getCurrent_pos() {
        return current_pos;
    }

    public void setCurrent_pos(String current_pos) {
        this.current_pos = current_pos;
    }


    public PalletSplitPolicy getSplit_policy() {
        return split_policy;
    }

    public void setSplit_policy(PalletSplitPolicy split_policy) {
        this.split_policy = split_policy;
    }

    public String getCurrent_form_no() {
        return current_form_no;
    }

    public void setCurrent_form_no(String current_form_no) {
        this.current_form_no = current_form_no;
    }

    public Integer getLoc_change_times() {
        return loc_change_times;
    }

    public void setLoc_change_times(Integer loc_change_times) {
        this.loc_change_times = loc_change_times;
    }

    public String getProcedure_route() {
        return procedure_route;
    }

    public void setProcedure_route(String procedure_route) {
        this.procedure_route = procedure_route;
    }

    public String getStorage_location() {
        return storage_location;
    }

    public void setStorage_location(String storage_location) {
        this.storage_location = storage_location;
    }

    public void Reset(WorkProcedure workProcedure) {
    	this.setId(null);
        this.setIs_empty(true);
        this.dispatch_status = PalletDispatchStatus.Dispatching;
        this.setPallet_status(PalletStatus.In_Waiting);
        this.setPallet_split_time(null);
        this.setStorage_location("");

       
        this.setPalletize_complete_time(null);
        this.setCurrent_procedure_time(new Date());
        this.setWork_procedure(workProcedure);
    }



}