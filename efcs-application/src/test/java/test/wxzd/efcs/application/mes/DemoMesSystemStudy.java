package test.wxzd.efcs.application.mes;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlmes.ws.celltestintegration.CheckProcessLotRequest;
import com.atlmes.ws.celltestintegration.CheckProcessLotResponse;
import com.wxzd.catl.base.MesSystemServiceBase;
import com.wxzd.gaia.common.base.core.exception.ExceptionUtl;
import com.wxzd.gaia.common.base.core.thread.ThreadUtl;

/**
 * 临时测试用的一些方法
 * 
 * 处理相关技术的地方
 * 
 * @version 1
 * @author y
 * @.create 2017-04-10
 */
public class DemoMesSystemStudy extends MesSystemServiceBase {

	private static final Logger log = LoggerFactory.getLogger(DemoMesSystem.class);

	List<String> cellList = DemoData.getCellList();
	List<String> trayList = DemoData.getTrayList();
	List<String> resourceList = DemoData.getResourceList();

	@Test
	public void name() throws Exception {
		System.out.println("...");
	}

	/**
	 * 托盘校验
	 * 测试掉线后是否会自动重连，是否需要重新启动
	 */
	@SuppressWarnings("unused")
	@Test
	public void miCheckProcessLot() throws Exception {
		for (int i = 0; ; i++) {

			try {
				CheckProcessLotResponse response = miCheckProcessLot(trayList.get(0));
				log.trace("{}", response.getCode());
				log.trace("{}", response.getMessage());
			} catch (Exception e) {
				Throwable rootCause = ExceptionUtl.getRootCause(e);
				if (rootCause instanceof java.net.SocketTimeoutException) {
					System.out.println("超时");
					System.out.println(e.getMessage());
				} else {
					throw e;
				}
			}
			ThreadUtl.sleep(10000);
		}
	}

	@Test
	public void demoException() {
		try {
			throw new RuntimeException();
		} catch (Exception e) {
			System.out.println(e.getClass());
			System.out.println(e.getCause());
		}

	}

	/**
	 * 托盘校验
	 */
	public CheckProcessLotResponse miCheckProcessLot(String processLot) throws Exception {
		CheckProcessLotRequest request = new CheckProcessLotRequest();
		request.setProcessLot(processLot);
		request.setSite("2001");
		CheckProcessLotResponse response = miCheckProcessLotService.miCheckProcessLot(request);
		return response;
	}
	
	
}
