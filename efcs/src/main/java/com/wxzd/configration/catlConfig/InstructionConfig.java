package com.wxzd.configration.catlConfig;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public class InstructionConfig {

	/** 调度执行间隔 */
	private static int executer_interval = 1 * 1000;

	/** 指令超时时间，那么队列任务的其他类别将会发送 */
	public static int queue_time_out = 60 * 1000;

	//重发指令默认优先级
	public static int resend_command_level = DispatcherConfig.formation_instruction_level + 1;

	//    public static String Formation_Stocker_Device_No = "ddj2";
	public static String Formation_Stocker_Device_No = DispatcherConfig.formation_stocker_2;

	public static int getExecuter_interval() {
		return executer_interval;
	}

	public static void setExecuter_interval(int executer_interval) {
		InstructionConfig.executer_interval = executer_interval;
	}

}
