package test;

import com.wxzd.configration.catlConfig.DispatcherConfig;
import com.wxzd.efcs.business.domain.enums.WorkProcedure;
import org.junit.Test;

/**
 * @author Leon Regulus on 2017/5/4.
 * @version 1.0
 * @since 1.0
 */
public class SimpleTest {

    @Test
    public void test() {
        WorkProcedure result = DispatcherConfig.getPalletErrorProcedureByLocation("2020");
        System.out.println(result.toString());

        result = DispatcherConfig.getPalletErrorProcedureByLocation("3200");
        System.out.println(result.toString());

        result = DispatcherConfig.getPalletErrorProcedureByLocation("LA010410");
        System.out.println(result.toString());

        result = DispatcherConfig.getPalletErrorProcedureByLocation("LB060501");
        System.out.println(result.toString());
    }

    @Test
    public void test2() {
        System.out.println(DispatcherConfig.battery_status_mapping.get(DispatcherConfig.battery_default_fake));
        System.out.println(DispatcherConfig.battery_status_mapping.get(DispatcherConfig.battery_default_nc));
        System.out.println(DispatcherConfig.battery_status_mapping.get(DispatcherConfig.battery_default_ok));
        System.out.println(DispatcherConfig.battery_status_mapping.get(DispatcherConfig.battery_default_rework));
        System.out.println(DispatcherConfig.battery_status_mapping.get(DispatcherConfig.battery_default_ng));
    }

    @Test
    public void testConvert() {
        int no1 = DispatcherConfig.convertPalletNoToInt("L29B0011");
        System.out.println(no1);
        int no2 = DispatcherConfig.convertPalletNoToInt("L29B1011");
        System.out.println(no2);
        int no3 = DispatcherConfig.convertPalletNoToInt("L29B0211");
        System.out.println(no3);

        int no4 = DispatcherConfig.convertPalletNoToInt("L29BA211");
        System.out.println(no4);
        int no5 = DispatcherConfig.convertPalletNoToInt("L29BFFF21");
        System.out.println(no5);
    }
}
