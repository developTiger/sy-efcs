package com.wxzd.configration.catlConfig;

import com.wxzd.wms.core.SerialNoGenerator;

import java.text.DecimalFormat;

/**
 * @author Leon Regulus on 2017/4/23.
 * @version 1.0
 * @since 1.0
 */
public class ApplicationConfig {

    private static DecimalFormat locationNoFormatter = new DecimalFormat(SerialNoGenerator.LocationNoType);

    public static String getLocationNo(int x, int y, int z) {
        String profix = "";
        if (x <= 4) {
            profix = "LA";
        } else {
            profix = "LB";
        }

        String localtionNo = profix
                + locationNoFormatter.format(x <= 4 ? x : (x - 4))
                + locationNoFormatter.format(y)
                + locationNoFormatter.format(z);

        return localtionNo;
    }

    public static int getLocationX(String location) {
        String rowStr = location.substring(2, 4);
        int result = 0;
        try {
            result = Integer.parseInt(rowStr);
        } catch (Exception ex) {
        }

        return result;
    }

    public static String getAlarmCommandNo() {


        return Long.toString(System.currentTimeMillis());
    }
}
