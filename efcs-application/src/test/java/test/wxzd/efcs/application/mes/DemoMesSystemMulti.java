package test.wxzd.efcs.application.mes;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlmes.ws.celltestintegration.CheckProcessLotRequest;
import com.atlmes.ws.celltestintegration.CheckProcessLotResponse;
import com.wxzd.catl.base.MesSystemServiceBase;
import com.wxzd.gaia.common.base.core.thread.PoolUtl;
import com.wxzd.gaia.common.base.core.thread.ThreadUtl;


/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-02-15
 */
public class DemoMesSystemMulti extends MesSystemServiceBase {

	private static final Logger log = LoggerFactory.getLogger(DemoMesSystemMulti.class);

	ExecutorService executorService = null;

	@Before
	public void begin() {
		/*
		 * 100个线程，超过3倍增长线程，最多开启200个线程
		 * 而队列内线程最多有100*3+200=500个线程
		 * 超过500个线程阻塞
		 */
		executorService = PoolUtl.newIncreasedBlockedThreadPool(100, 200, 3);
		//		executorService = PoolUtl.newCachedThreadPool();//应该用即时的否则计算时间会不对
		log.trace("--------开始完毕--------");
	}

	@After
	public void end() {
		waitEnd();
	}

	private void waitEnd() {
		executorService.shutdown();
		for (;;) {
			ThreadUtl.sleep(1000);
			if (executorService.isTerminated()) {
				log.trace("--------收尾完毕--------");
				break;
			}
		}
	}
	
	@Test
	public void checkTray() throws Exception {
		CheckProcessLotRequest request = new CheckProcessLotRequest();
		request.setSite("2001");
		request.setProcessLot("U1F0048");
		CheckProcessLotResponse response = miCheckProcessLotService.miCheckProcessLot(request);
		log.trace("{}", response.getCode());
		log.trace("{}", response.getMessage());
	}

	@Test
	public void multi() {
		for (int i = 0; i < 100; i++) {
			executorService.execute(new R1("2001", "U1F0048", i));
		}
	}

	class R1 implements Runnable {

		int num = 0;
		String site;
		String processLot;

		public R1(String site, String processLot, int num) {
			this.site = site;
			this.processLot = processLot;
			this.num = num;
		}

		public void run() {
			try {
				Date dateBegin = new Date();
				CheckProcessLotRequest request = new CheckProcessLotRequest();
				request.setSite("2001");
				request.setProcessLot("U1F0048");
				CheckProcessLotResponse response = miCheckProcessLotService.miCheckProcessLot(request);
				Date dateEnd = new Date();
				long time = dateEnd.getTime() - dateBegin.getTime();
				log.trace("{}", response.getCode());
				log.trace("{}", response.getMessage());
				log.trace("num:{};use time:{}", num, time);
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
