package test.wxzd.efcs.application.dababase.bean;

import java.util.Date;

import com.wxzd.gaia.common.base.bean.AliasName;
import com.wxzd.wms.ddd.IEntity;

/**
 * 
 * 
 * @version 1
 * @author y
 * @create 2016-06-12
 */
@AliasName("temp")
public class TempDomain extends IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private int num;
	private String twoLetter;
	private Date create_date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getTwoLetter() {
		return twoLetter;
	}

	public void setTwoLetter(String twoLetter) {
		this.twoLetter = twoLetter;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	@Override
	public String toString() {
		return "TempDomain [name=" + name + ", description=" + description + ", num=" + num + ", twoLetter=" + twoLetter + ", create_date=" + create_date + "]";
	}

}