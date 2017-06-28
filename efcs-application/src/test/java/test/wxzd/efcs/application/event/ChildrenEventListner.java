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
public class ChildrenEventListner extends AbstractEventListener<ChildrenEvent> {

	@Override
	public void onEvent(ChildrenEvent eventObj) throws Exception {
		System.out.println("children event happened");
		throw new RuntimeException("....");
	}

}
