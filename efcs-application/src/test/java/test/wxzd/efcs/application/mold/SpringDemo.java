package test.wxzd.efcs.application.mold;

import org.junit.Test;

import com.wxzd.gaia.common.base.spring.core.utils.SpringUtl;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-01
 */
public class SpringDemo extends SpringDemoBase {

	@Test
	public void name() {
		SpringUtl.showCustomerBeanName(applicationContext);
	}

}
