package test.wxzd.efcs.application.event;

import org.junit.Test;

import com.wxzd.gaia.event.exception.EventException;
import com.wxzd.gaia.event.publisher.ApplicationEventPublisher;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-01
 */
public class SpringDemo extends SpringDemoBase {

	/**
	 * 测试是否会子类触发父类
	 */
	@Test
	public void triger() {
		try {
			ApplicationEventPublisher.trigger(new ChildrenEvent());
			System.out.println("华丽的分隔线");
			ApplicationEventPublisher.trigger(new FatherEvent());
		} catch (EventException e) {
			e.printStackTrace();
		}
	}

}
