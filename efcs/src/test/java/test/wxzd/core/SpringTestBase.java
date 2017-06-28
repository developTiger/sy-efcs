package test.wxzd.core;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-02-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ //
		"classpath*:config-frame-*.xml", //
		"classpath*:config-app-*.xml", //
		"classpath*:config-test-*.xml" })
public class SpringTestBase implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) //
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Resource
	DefaultWebSecurityManager securityManager;

	@Before
	public void before() {
		SecurityUtils.setSecurityManager(securityManager);
	}
}