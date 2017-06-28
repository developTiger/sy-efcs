package com.wxzd.efcs.ddd.domain.enums;

/**
 * EFCS 产品模块中使用的异常代码枚举
 * TODO 需要细化异常代码
 *
 * @author Leon Regulus on 2017/4/16.
 * @version 1.0
 * @since 1.0
 */
public enum EfcsErrorCode {

	/**
	 * 无异常
	 */
	None,
	/**
	 * 指令异常
	 */
	instruction_exception,
	/**
	 * 指令作废
	 */
	instruction_scrapped,
	/**
	 * 未知
	 */
	Unknown,


    /**
     * 工序异常结束
     */
    ErrorFinish
}
