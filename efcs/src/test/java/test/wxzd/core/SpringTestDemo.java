package test.wxzd.core;

import com.wxzd.gaia.common.base.spring.core.utils.SpringUtl;
import com.wxzd.wms.core.application.service.WareHouseAppService;
import com.wxzd.wms.core.application.service.ZoneAppService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-03-02
 */
public class SpringTestDemo extends SpringTestBase {

	@Resource
	private WareHouseAppService wareHouseService;

	@Resource
	private ZoneAppService zoneAppService;

	@Test
	public void showInstance() {
		SpringUtl.showCustomerBeanName(applicationContext);
	}
	
}
