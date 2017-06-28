package com.wxzd.efcs.business.domain.enums;

/**
 * 工序类型枚举
 *
 * <p>工艺顺序如下</p>
 * <ol>
 * <li>化成段组盘<li>
 * <li>高温<li>
 * <li>化成<li>
 * <li>Rework组盘<li>
 * <li>化成Rework<li>
 * <li>化成段拆盘<li>
 * <li>测试段组盘<li>
 * <li>常温1<li>
 * <li>OCV1<li>
 * <li>常温2<li>
 * <li>OCV2<li>
 * <li>测试段拆盘<li>
 * </ol>
 * TODO 根据需要细化工序要求
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum WorkProcedure {

    /**
     * 化成段电池入库
     */
    Formation_In,
    /**
     * 化成组盘
     */
    Formation_Palletize,
    /**
     * 高温静置
     */
    High_Temperature,
    /**
     * 化成
     */
    Formation,
    /**
     * 化成拆盘
     */
    Formation_Split,
    /**
     * 化成REWORK组盘
     */
    Formation_Rework_Palletize,
    /**
     * 化成REWORK
     */
    Formation_Rework,
    /**
     * 化成电池出库
     */
    Formation_Out,


    /**
     * 测试段电池入库
     */
    Test_In,
    /**
     * 测试组盘
     */
    Test_Palletize,
    /**
     * 常温静置1
     */
    Normal_Temperature_1,
    /**
     * 测试OCV1
     */
    Test_OCV_1,
    /**
     * 常温静置2
     */
    Normal_Temperature_2,
    /**
     * 测试OCV2
     */
    Test_OCV_2,
    /**
     * 测试段电池出库
     */
    Test_Out,
    /**
     * 测试拆盘
     */
    Test_Pallet_Split,

    /**
     * 移动到组盘缓存
     */
    Palletize_Cache,

    /**
     * 化成段异常排除
     */
    FORMATION_ERROR_EXPORT,
    /**
     * 测试段异常排除
     */
    TEST_ERROR_EXPORT,

    Test_PalletMove,
    /**
     * 无业务工序托盘流转移动
     */
    Formation_PalletMove,
    /**
     * 人工操作托盘
     */
    Manual;

}
