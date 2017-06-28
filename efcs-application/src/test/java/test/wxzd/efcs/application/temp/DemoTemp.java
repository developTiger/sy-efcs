package test.wxzd.efcs.application.temp;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.wxzd.gaia.common.base.core.string.StringUtl;
import com.wxzd.gaia.common.base.core.string.UuidUtl;
import com.wxzd.gaia.common.base.core.thread.ThreadUtl;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-22
 */
public class DemoTemp {
	
	@Test
	public void namexx() throws Exception {
		Date d1 = new Date();
		ThreadUtl.sleep(10*1000);
		Date d2 = new Date();
		System.out.println(d2.getTime()-d1.getTime());
	}
	
	@Test
	public void charNum() throws Exception {
		char c = 42;
		System.out.println(String.valueOf(c));
	}
	
	@Test
	public void stringIsWhat() throws Exception {
		String str = "  ";
		System.out.println(StringUtl.getHexString(str.getBytes()));
	}
	
	@Test
	public void dealTrim() throws Exception {
		String str = " x\tv  ";
		System.out.println(str.replaceAll("\\s", ""));
	}
	
	@Test
	public void uuid() throws Exception {
		System.out.println(UuidUtl.getUuid());
	}
	
	@Test
	public void name() {
		List<String> list = new LinkedList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		//		System.out.println(list);
		//		list.remove(0);
		//		System.out.println(list);
		//		System.out.println(list.indexOf("c"));
		for (int i = 0; i < list.size(); i++) {
			System.out.println("--------------");
			System.out.println(list.get(i));
			if (i == 1) {
				list.remove(i);
			}
			System.out.println(list.get(i));
		}
		System.out.println(list);
	}

	@Test
	public void name2() throws Exception {
		int type = 5;
		int content = 4;

		System.out.println(content & type);
	}

	public <T extends Father> void demo(Class<T> clazz) {
		return;
	}

	@Test
	public void demo2() {
		demo(Children.class);
	}

	@Test
	public void xxx() {
		System.out.println(String.valueOf(Integer.MAX_VALUE));
		System.out.println(String.valueOf(Integer.MAX_VALUE).length());
		System.out.println(String.valueOf(Long.MAX_VALUE));
		System.out.println(String.valueOf(Long.MAX_VALUE).length());
	}

	@Test
	public void namex() throws Exception {
		int i = 0;
		System.out.println(StringUtils.leftPad(String.valueOf(i), 2, "0"));
	}
}
