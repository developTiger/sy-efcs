package test.wxzd.efcs.application.mes;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-02-16
 */
public class DemoData {
	/** 芯片信息 */
	static List<String> cellList = new LinkedList<>();
	/** 托盘信息 */
	static List<String> trayList = new LinkedList<>();
	/** 化成资源 */
	static List<String> resourceList = new LinkedList<>();

	static {
		addTrayList();
		addCellList();
		addResourceList();
	}

	public static List<String> getResourceList() {
		return resourceList;
	}

	public static List<String> getCellList() {
		return cellList;
	}

	public static List<String> getTrayList() {
		return trayList;
	}

	private static void addResourceList() {
		resourceList.add("FXXX1177");
		resourceList.add("FXXX1178");
		resourceList.add("FXXX1179");
		resourceList.add("FXXX1180");
		resourceList.add("FXXX1181");
		resourceList.add("FXXX1182");
		resourceList.add("FXXX1183");
	}

	private static void addTrayList() {
		trayList.add("LFP00001");
		trayList.add("LFP00002");
		trayList.add("LFP00003");
		trayList.add("LFP00004");
		trayList.add("LFP00005");
		trayList.add("LFP00006");
		trayList.add("LFP00007");
		trayList.add("LFP00008");
		trayList.add("LFP00009");
		trayList.add("LFP00010");

	}

	private static void addCellList() {
		cellList.add("14Y7061M0416");
		cellList.add("14Y7061M0436");
		cellList.add("14Y7061M0437");
		cellList.add("14Y7061M0438");
		cellList.add("14Y7061M0441");
		cellList.add("14Y7061M0443");
		cellList.add("14Y7061M0448");
		cellList.add("14Y7061M0453");
		cellList.add("14Y7061M0459");
		cellList.add("14Y7061M0465");
		cellList.add("14Y7061M0466");
		cellList.add("14Y7061M0469");
		cellList.add("14Y7061M0470");
		cellList.add("14Y7061M0472");
		cellList.add("14Y7061M0474");
		cellList.add("14Y7062G0001");
		cellList.add("14Y7062G0002");
		cellList.add("14Y7062G0004");
		cellList.add("14Y7062G0006");
		cellList.add("14Y7062G0008");
		cellList.add("14Y7062G0009");
		cellList.add("14Y7062G0010");
		cellList.add("14Y7062G0011");
		cellList.add("14Y7062G0012");
		cellList.add("14Y7062G0013");
		cellList.add("14Y7062G0016");
		cellList.add("14Y7062G0017");
		cellList.add("14Y7062G0018");
		cellList.add("14Y7062G0019");
		cellList.add("14Y7062G0021");
		cellList.add("14Y7062G0022");
		cellList.add("14Y7062G0024");
		cellList.add("14Y7062G0025");
		cellList.add("14Y7062G0026");
		cellList.add("14Y7062G0027");
		cellList.add("14Y7062G0029");
		cellList.add("14Y7062G0032");
		cellList.add("14Y7062G0033");
		cellList.add("14Y7062G0037");
		cellList.add("14Y7062G0039");
		cellList.add("14Y7062G0041");
		cellList.add("14Y7062G0046");
		cellList.add("14Y7062G0050");
		cellList.add("14Y7062G0052");
		cellList.add("14Y7062G0054");
		cellList.add("14Y7062G0056");
		cellList.add("14Y7062G0057");
		cellList.add("14Y7062G0058");
		cellList.add("14Y7062G0059");
		cellList.add("14Y7062G0061");
		cellList.add("14Y7062G0063");
		cellList.add("14Y7062G0064");
		cellList.add("14Y7062G0065");
		cellList.add("14Y7062G0067");
		cellList.add("14Y7062G0068");
		cellList.add("14Y7062G0069");
		cellList.add("14Y7062G0070");
		cellList.add("14Y7062G0071");
		cellList.add("14Y7062G0074");
		cellList.add("14Y7062G0075");
		cellList.add("14Y7062G0076");
		cellList.add("14Y7062G0078");
		cellList.add("14Y7062G0079");
		cellList.add("14Y7062G0080");
		cellList.add("14Y7062G0083");
		cellList.add("14Y7062G0084");
		cellList.add("14Y7062G0092");
		cellList.add("14Y7062G0094");
		cellList.add("14Y7062G0095");
		cellList.add("14Y7062G0100");
		cellList.add("14Y7062G0101");
		cellList.add("14Y7062G0107");
		cellList.add("14Y7062G0110");
		cellList.add("14Y7062G0114");
		cellList.add("14Y7062G0115");
		cellList.add("14Y7062G0118");
		cellList.add("14Y7062G0119");
		cellList.add("14Y7062G0124");
		cellList.add("14Y7062G0127");
		cellList.add("14Y7062G0129");
		cellList.add("14Y7062G0130");
		cellList.add("14Y7062G0131");
		cellList.add("14Y7062G0135");
		cellList.add("14Y7062G0136");
		cellList.add("14Y7062G0139");
		cellList.add("14Y7062G0141");
		cellList.add("14Y7062G0142");
		cellList.add("14Y7062G0143");
		cellList.add("14Y7062G0144");
		cellList.add("14Y7062G0145");
		cellList.add("14Y7062G0146");
		cellList.add("14Y7062G0147");
		cellList.add("14Y7062G0148");
		cellList.add("14Y7062G0149");
		cellList.add("14Y7062G0151");
		cellList.add("14Y7062G0152");
		cellList.add("14Y7062G0155");
		cellList.add("14Y7062G0156");
		cellList.add("14Y7062G0158");
		cellList.add("14Y7062G0159");
		cellList.add("14Y7062G0160");
		cellList.add("14Y7062G0161");
		cellList.add("14Y7062G0162");
		cellList.add("14Y7062G0165");
		cellList.add("14Y7062G0166");
		cellList.add("14Y7062G0168");
		cellList.add("14Y7062G0169");
		cellList.add("14Y7062G0170");
		cellList.add("14Y7062G0171");
		cellList.add("14Y7062G0172");
		cellList.add("14Y7062G0174");
		cellList.add("14Y7062G0175");
		cellList.add("14Y7062G0176");
		cellList.add("14Y7062G0179");
		cellList.add("14Y7062G0180");
		cellList.add("14Y7062G0182");
		cellList.add("14Y7062G0184");
		cellList.add("14Y7062G0187");
		cellList.add("14Y7062G0188");
		cellList.add("14Y7062G0189");
		cellList.add("14Y7062G0195");
		cellList.add("14Y7062G0196");
		cellList.add("14Y7062G0197");
		cellList.add("14Y7062G0198");
		cellList.add("14Y7062G0199");
		cellList.add("14Y7062G0200");
		cellList.add("14Y7062G0203");
		cellList.add("14Y7062G0204");
		cellList.add("14Y7062G0206");
		cellList.add("14Y7062G0207");
		cellList.add("14Y7062G0210");
		cellList.add("14Y7062G0211");
		cellList.add("14Y7062G0212");
		cellList.add("14Y7062G0213");
		cellList.add("14Y7062G0215");
		cellList.add("14Y7062G0216");
		cellList.add("14Y7062G0217");
		cellList.add("14Y7062G0220");
		cellList.add("14Y7062G0222");
		cellList.add("14Y7062G0225");
	}
}
