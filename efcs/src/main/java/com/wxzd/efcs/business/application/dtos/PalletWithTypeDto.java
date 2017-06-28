package com.wxzd.efcs.business.application.dtos;

/**
 * Created by zhouzh on 2017/4/22.
 */
public class PalletWithTypeDto extends PalletDispatchDto {

	private String aliasTypeName;

	/**
	 * 容器类别别名
	 * 是为了和化成通信的时候给他用的
	 * t.container_alias_name typeName
	 * 使用为业务托盘类型还是工装类型
	 */
	private String typeName;

	public String getAliasTypeName() {
		return aliasTypeName;
	}

	public void setAliasTypeName(String aliasTypeName) {
		this.aliasTypeName = aliasTypeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
