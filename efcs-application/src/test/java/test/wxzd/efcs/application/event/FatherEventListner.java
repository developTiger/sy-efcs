package test.wxzd.efcs.application.event;

import org.springframework.stereotype.Component;

import com.wxzd.gaia.event.listener.AbstractEventListener;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-25
 */
@Component
public class FatherEventListner extends AbstractEventListener<FatherEvent> {

	@Override
	public void onEvent(FatherEvent eventObj) throws Exception {
		System.out.println("father event happened");
	}

}
