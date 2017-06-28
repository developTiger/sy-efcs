/**
 * Created by jade on 2017/4/19.
 */
//枚举
define(function (require, exports, module) {
    var win = {};

    //托盘状态
    win.PalletStatus = {
        In_Waiting: " 待入",
        In_Executing: "入中",
        In_Finished: "已入",
        Out_Waiting: "待出",
        Out_Executing: "出中",
        Out_Finished: "已出"
    };

    //工序类型枚举
    win.WorkProcedure = {
        Formation_In: "化成段电池入库",
        Formation_Palletize: "化成组盘",
        High_Temperature: "高温静置",
        Formation: "化成",
        Formation_Split: "化成拆盘",
        Formation_Rework_Palletize: "化成REWORK组盘",
        Formation_Rework: "化成REWORK",
        Formation_Out: "化成电池出库",
        Test_In: "测试段电池入库",
        Test_Palletize: "测试组盘",
        Normal_Temperature_1: "常温静置1",
        Test_OCV_1: "测试OCV1",
        Normal_Temperature_2: "常温静置2",
        Test_OCV_2: "测试OCV2",
        Test_Out: "测试段电池出库",
        Test_Pallet_Split: "测试拆盘",
        Palletize_Cache: "移动到组盘缓存",
        FORMATION_ERROR_EXPORT: "化成段异常排除",
        Formation_PalletMove: "化成空拖流转",
        Test_PalletMove: "测试空托流转",
        TEST_ERROR_EXPORT: "测试段异常排除",
        Manual: "人工操作托盘"
    };

    //位置枚举
    win.PositionType = {
        Storage_Location: "库位中的位置",
        Transport_Location: "输送线上的位置",
        Pallet: "托盘中的位置",
        Line: "拉带线中的位置"
    };

    //电池状态
    win.Battery_status = {
        OK: "优品",
        NG: "NG",
        NC: "NC",
        fake: "fake",
        rework: "rework"
    };


    win.Battery_status_colors = {
        OK: "green",
        NG: "red",
        NC: "NC"
    };


    //表单创建方式
    win.FmCreateMode = {
        System: "系统",
        Manual: "人工"
    };

    //表单状态
    win.FmStatus = {
        Created: "待执行",
        Executing: "执行中",
        Finished: "执行完成",
        ErrorFinished: "异常结束"
    };

    //表单状态颜色
    win.FmStatus_colors = {
        Created: "#333",
        Executing: "#333",
        Finished: "green",
        ErrorFinished: "red"
    };

    //异常代码
    win.EfcsErrorCode = {
        None: "无异常",
        instruction_exception: "指令异常",
        instruction_scrapped: "指令作废",
        Unknown: "未知",
        ErrorFinish: "工序异常结束"
    };

    //组拆盘状态
    win.PalletizeStatus = {
        Waiting: "等待",
        Executing: "执行中",
        Finished: "完成"
    };

    //组拆盘颜色
    win.PalletizeStatus_colors = {
        Waiting: "等待",
        Executing: "执行中",
        Finished: "green"
    };


    //报警类型
    win.AlarmType = {
        FireAlarm: "火灾报警",
        ErrorAlarm: "异常报警"
    };
    //位置类型
    win.LocationType = {
        Device: "设备",
        Storage: "库位"
    };

    //报警级别
    win.AlarmLevel = {
        High: "高",
        Normal: "中",
        Low: "低"
    };

    //指令类型
    win.InstructionType = {
        Stock_In: "上架",
        Stock_Out: "下架",
        Stock_Move: "摆渡",
        Transport: "移动"
    };

    //移动策略
    win.InstructionMovePolicy = {
        Static: "必须按照下发的指定移动，目标位置不能发生变化",
        Dynamic: "可以根据实际情况微调，目标位置可能发生变化，位置的选择由电气决定"
    }

    //执行状态
    win.InstructionStatus = {
        Created: "等待,指令没有下发时间，待更新时间后，变为",
        Waiting: "待执行",
        Send: " 已下发指令",
        Executing: "执行中",
        Finished: "执行完成"
    };

    //执行状态颜色
    win.InstructionStatus_colors = {
        Created: "#333",
        Waiting: "#333",
        Send: " #333",
        Executing: "#333",
        Finished: "green"
    };


    //容器类型
    win.StorageType = {
        sku: "sku",
        container: "容器"
    };

    //设备类型
    win.EquipmentType = {
        "Stocker": "堆垛机",
        "Conveyor": "输送机",
        "Line": "拉带线",
        "Robot_XYZ": "三坐标机器人",
        "OCV_System": "OCV系统",
        "Formation_System": "化成系统"
    };

    //调度状态
    win.PalletDispatchStatus = {
        "Dispatching": "调度中",
        "Finished": "调度完成"
    };

    //计划类型
    win.SchedulerType = {
        ExecutionOnce: "单次执行",
        ExecutionLoop: "循环执行"
    };

    //任务类型
    win.TaskType = {
        AllotLocation: '分配库位',
        MESChargeNumber: ' MES 收数',
        EmptyPalletOut: '空托出库',
        EmptyPalletIn: "空托上架",
        highTemperatureStay: '高温静置',
        normalTemperatureStay: '常温静置'
    };

    // 计划状态
    win.SchedulerStatus = {
        Running: "执行中",
        Finish: "执行完成",
        Stop: '中止',
        OnScheduler: '计划中',
        ErrorFinished: '异常结束'
    };

    // 间隔时间单位
    win.TimeIntervalUnit = {
        DAY: '日',
        HOUR: '时',
        MINUTE: '分',
        SECOND: '秒'
    };

    // 工装计划类型
    win.SchedulerType = {
        ExecutionOnce: '单次执行',
        ExecutionLoop: '循环执行'
    }

    // 工装时间间隔单位
    win.TimeIntervalUnit = {
        DAY: '日',
        HOUR: '时',
        MINUTE: '分',
        SECOND: '秒'
    }

    // 工装执行状态
    win.AppointmentStatus = {
        Waiting: '等待',
        Appointmented: '已预约',
        Finished: '执行完成',
        ErrorFinished: '异常结束'

    }

    //获取枚举label
    function getEnumsLabel(enumType, value) {
        return (win[enumType] && win[enumType][value]) || ""
    }

    //获取带颜色枚举label
    function getEnumsLabelTpl(enumType, value) {
        var colorKey = enumType + "_colors";
        return "<span style='color:" + ((win[colorKey] && win[colorKey][value]) || "#333") + "'>" +
            getEnumsLabel(enumType, value) + "</span>";
    }

    exports.getEnumsLabel = getEnumsLabel;

    exports.getEnumsLabelTpl = getEnumsLabelTpl;
});

