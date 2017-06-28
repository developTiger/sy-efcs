package test.wxzd.efcs.application.log;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Test;

import com.wxzd.catl.log.CatlWebserviceLogFactory;
import com.wxzd.dispatcher.dts.log.DTSFileLogFactory;
import com.wxzd.gaia.common.base.core.date.DateUtl;
import com.wxzd.gaia.common.base.core.log.FileLogConfig;
import com.wxzd.gaia.common.base.core.log.FileLogFactory;
import com.wxzd.gaia.common.base.core.log.FileLogUnity;
import com.wxzd.gaia.common.base.core.thread.ThreadUtl;
import com.wxzd.gaia.common.core.io.FileUtl;
import com.wxzd.wms.catl.fcs.webservice.log.ICWebServiceLogFactory;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-21
 */
public class DemoLog {

	@Test
	public void fileLogUnity() {
		FileLogUnity log = FileLogFactory.getFileLogUnity();
		log.begin("fileLogUnity", "message");

		log.info("fileLogUnity", "message");

		log.end("fileLogUnity", "message");
	}

	@Test
	public void fileLogUnityCustomer() {
		FileLogUnity log = FileLogFactory.getCustomerFileLogUnity("tag", "/path", "devideId");
		log.begin("fileLogUnity", "message");

		ThreadUtl.sleep(1000);
		log.info("fileLogUnity", "message");

		log.end("fileLogUnity", "message");
	}

	@Test
	public void fileLogFactory() {
		//		FileLogFactory.log(MessageType.info, "message", "fileLogFactory", "message");
		FileLogFactory.info("message", "fileLogFactory", "message");
		FileLogFactory.warn("message", "fileLogFactory", "message");
	}

	@Test
	public void fileLogFactoryCustomer() {
		//		FileLogFactory.logCustomer("/path", "devideId", MessageType.info, "message", "fileLogFactory", "message");
		FileLogFactory.infoCustomer("/path", "devideId", "message", "fileLogFactory", "message");
		FileLogFactory.warnCustomer("/path", "devideId", "message", "fileLogFactory", "message");
	}

	@Test
	public void webServiceFileLogUnity() {
		FileLogUnity log = ICWebServiceLogFactory.getWebServiceFileLogUnity();

		log.begin("webServiceFileLogUnity", "message");

		log.info("webServiceFileLogUnity", "message");

		log.end("webServiceFileLogUnity", "message");

	}

	@Test
	public void mesFileLogUnity() {
		FileLogUnity log = CatlWebserviceLogFactory.getWebserviceFileLogUnity("/method", "device");

		log.begin("webServiceFileLogUnity", "message");

		log.info("webServiceFileLogUnity", "message");

		log.end("webServiceFileLogUnity", "message");

	}

	/**
	 * 各种奇葩字符信息
	 */
	@Test
	public void fileLogFactoryEx() {
		FileLogFactory.warn("message", "fileLogFactory", "aa\r\n\t  ,\nxxx\r\n ll\ncc   \"bb");
	}

	@Test
	public void demo() throws Exception {
		File file = new File("local/demo.txt");
		String content = FileUtl.read(file);
		System.out.println(content.length() / 2);
	}

	@Test
	public void name() {
		DTSFileLogFactory.templatureInfo("demo", "msg");
	}

	@Test
	public void demo2() throws Exception {
		String file = FileLogConfig.getDefault_path() + "/templature";
		File f = new File(file);
		if (f.isDirectory()) {
			Iterator<File> it = FileUtils.iterateFiles(f, new AllIoFileFilter(), new AllIoFileFilter());
			while (it.hasNext()) {
				File o = it.next();
				if (isDelFile(o)) {
					o.delete();
				}
			}
		}
	}

	private boolean isDelFile(File file) {
		Calendar date = Calendar.getInstance();
		int i = date.get(Calendar.HOUR_OF_DAY);
		String hour = String.valueOf(i);
		String the = hour + "_" + DateUtl.formatDate(date.getTime());
		String name = file.getName();
		if (name.startsWith(the)) {
			return false;
		}
		if (name.startsWith(hour)) {
			return true;
		}
		return false;
	}

}

class AllIoFileFilter implements IOFileFilter {

	@Override
	public boolean accept(File file) {
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
