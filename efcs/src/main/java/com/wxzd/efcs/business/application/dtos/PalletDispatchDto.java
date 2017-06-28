package com.wxzd.efcs.business.application.dtos;

import com.wxzd.efcs.business.domain.entities.PalletDetail;
import com.wxzd.efcs.business.domain.enums.*;
import com.wxzd.efcs.ddd.domain.enums.EfcsErrorCode;
import com.wxzd.gaia.common.base.bean.MemberIgnore;

import java.util.*;

/**
 * 托盘调度记录
 */
public class PalletDispatchDto {


    private UUID id;

    /**
     * 容器Id
     */
    private String container_id;
    /**
     * 容器条码
     */
    private String container_no;

    /**
     * 当前表单
     */
    private String current_form_no;
    /**
     * 工序
     */
    private WorkProcedure work_procedure;
    /**
     * 托盘状态
     */
    private PalletStatus pallet_status;
    /**
     * 通道策略
     */
    private PalletChannelPolicy channel_policy;
    /**
     * 是否空托
     */
    private Boolean is_empty;
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

    /**
     * 异常代码
     */
    private EfcsErrorCode error_code;
    /**
     * 异常信息
     */
    private String error_desc;

    /**
     * 化成库进入次数
     */
    private Integer loc_change_times;


    /**
     * 托盘容量
     */
    @MemberIgnore
    private Integer sku_max_count;


    /**
     * 拉线
     */
    @MemberIgnore
    private String house_no;
    /**
     * 库位号
     */
    @MemberIgnore
    private String loc_no;

    /**
     * 设备号
     */
    @MemberIgnore
    private String equip_no;

    /**
     * 货物列表
     */
    @MemberIgnore
    private List<PalletDetailDto> palletDetails = new ArrayList<>();

    @MemberIgnore
    private List<PalletMoveDetailDto> palletMoveDetailDtos = new ArrayList<>();

    /**
     * 条码-货物明细
     */
    @MemberIgnore
    private Map<String, PalletDetailDto> palletDetailMap = new HashMap<>();


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public EfcsErrorCode getError_code() {
        return error_code;
    }

    public void setError_code(EfcsErrorCode error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getLoc_no() {
        return loc_no;
    }

    public void setLoc_no(String loc_no) {
        this.loc_no = loc_no;
    }

    public String getEquip_no() {
        return equip_no;
    }

    public void setEquip_no(String equip_no) {
        this.equip_no = equip_no;
    }

    public Integer getLoc_change_times() {
        return loc_change_times;
    }

    public void setLoc_change_times(Integer loc_change_times) {
        this.loc_change_times = loc_change_times;
    }

    public List<PalletDetailDto> getPalletDetails() {
        return palletDetails;
    }

    public void setPalletDetails(List<PalletDetailDto> palletDetails) {
        this.palletDetails = palletDetails;
    }

    public Integer getSku_max_count() {
        return sku_max_count;
    }

    public void setSku_max_count(Integer sku_max_count) {
        this.sku_max_count = sku_max_count;
    }

    public Map<String, PalletDetailDto> getPalletDetailMap() {
        return palletDetailMap;
    }

    public void setPalletDetailMap(Map<String, PalletDetailDto> palletDetailMap) {
        this.palletDetailMap = palletDetailMap;
    }

    public String getCurrent_form_no() {
        return current_form_no;
    }

    public void setCurrent_form_no(String current_form_no) {
        this.current_form_no = current_form_no;
    }

    public List<PalletMoveDetailDto> getPalletMoveDetailDtos() {
        return palletMoveDetailDtos;
    }

    public void setPalletMoveDetailDtos(List<PalletMoveDetailDto> palletMoveDetailDtos) {
        this.palletMoveDetailDtos = palletMoveDetailDtos;
    }
}