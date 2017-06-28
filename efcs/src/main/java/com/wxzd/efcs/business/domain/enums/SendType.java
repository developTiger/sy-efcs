package com.wxzd.efcs.business.domain.enums;

import com.wxzd.gaia.common.base.core.type.ValueType;

/**
 * Created by zhouzh on 2017/4/21.
 * 指令下发类型
 */
public enum SendType implements ValueType {

	/**
	 * 1
	 * 立刻下发
	 */
	Direct(1),

	/**
	 * 2
	 * 计划任务，根据sendtime 下发
	 */
	Scheduler(2),

	/**
	 * 队列下发 4
	 * 1+4
	 * 2+4
	 * 队列这个概念就看有没有队列号
	 * 计划和队列是冲突的
	 */
	QueuesDirect(1 + 4),

	/**
	 * 队列延迟下发
	 */
	QueuesScheduler(2 + 4);

	Integer value = null;

	private SendType(int value) {
		this.value = value;
	}

	@Override
	public String getDescription() {
		return name();
	}

	@Override
	public Integer getValue() {
		return value;
	}
}
