package test.wxzd.efcs.application.business;

import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerDto;
import com.wxzd.efcs.business.application.dtos.FcsToolSchedulerLocationDto;
import com.wxzd.efcs.business.application.querys.FcsToolSchedulerExQuery;
import com.wxzd.efcs.business.application.service.FcsToolSchedulerAppService;
import com.wxzd.efcs.business.domain.enums.AppointmentStatus;
import com.wxzd.efcs.business.domain.enums.SchedulerType;
import com.wxzd.gaia.common.base.core.date.DateUtl;
import com.wxzd.gaia.common.base.core.result.GaiaResult;
import com.wxzd.gaia.common.base.core.result.ListResult;
import com.wxzd.gaia.common.base.core.result.ObjectResult;
import com.wxzd.gaia.common.base.core.result.PageResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.wxzd.efcs.application.mold.SpringDemoBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhouzh on 2017/4/19.
 */
public class FCSToolScheduleServiceTest extends SpringDemoBase {


    @Autowired
    FcsToolSchedulerAppService schedulerService;

    // region 工装调度接口

    /**
     * 获取目前所有在执行的计划
     * @return
     */

    @Test
    public void  getLiveToolSchedulesTest(){

       ListResult<FcsToolSchedulerDto> result= schedulerService.getLiveToolSchedules();
       Assert.assertTrue(result.isSuccess());

    }

    /**
     * 获取目前所有需要执行的工装单
     * @return
     */

    @Test
    public void getLiveToolSchedulerLocationsTest(){
        String scheduleNo="TRS170622000009";
        ListResult<FcsToolSchedulerLocationDto> result= schedulerService.getLiveToolSchedulerLocations(scheduleNo);
        Assert.assertTrue(result.isSuccess());
    }

    //endregion

    // region   start fetch
    /**
     * 按标识获取工装计划
     * @return
     */

    @Test
    public void getByIdTest(){
        UUID id =UUID.fromString("222829a1-6dfc-499e-8067-740a7cd40b7f");
        ObjectResult result =schedulerService.getById(id);
        Assert.assertTrue(result.isSuccess());

    }

    /**
     按调度计划编号获取调度计划
     * @return
     */

    @Test
    public void getByNoTest(){
        String scheduleNo ="TRS170622000009";
        ObjectResult result =schedulerService.getByNo(scheduleNo);
        Assert.assertTrue(result.isSuccess());

    }


    /**
     按照指定条件组合查询ToolSchedule
     * @return
     */

    @Test
    public void findToolSchedulesTest(){
        FcsToolSchedulerExQuery query = new FcsToolSchedulerExQuery();
        ListResult<FcsToolSchedulerDto> result= schedulerService.findToolSchedules(query);
        Assert.assertTrue(result.isSuccess());

    }

    /**
     * 按照条件分页获取ToolSchedule
     * @return
     */

    @Test
    public void findToolSchedulesPagedTest(){

        FcsToolSchedulerExQuery query = new FcsToolSchedulerExQuery();
        PageResult<FcsToolSchedulerDto> result= schedulerService.findToolSchedulesPaged(query);
    }


    /**
     * 按工单标识获取绑定的工装单
     * @return
     */

    @Test
    public void getScheduleLocationTest(){
        UUID locationId =UUID.fromString("70a811e5-c19e-490d-baa3-7b6cb1c4c973");
        ObjectResult<FcsToolSchedulerLocationDto> result =schedulerService.getScheduleLocation(locationId);
        Assert.assertTrue(result.isSuccess());

    }

    /**
     * 按计划标识获取工装单
     * @return
     */

    @Test
    public void getScheduleLocationsByScIdTest(){
        UUID scId =UUID.fromString("59f8f24c-0e1e-46d8-b59f-4d1cfd4942ad");
        ListResult<FcsToolSchedulerLocationDto> result =schedulerService.getScheduleLocations(scId);
        Assert.assertTrue(result.isSuccess());

    }


    /**
     * 按计划编号获取工装单
     * @return
     */

    @Test
    public void getScheduleLocationsByNoTest(){
        String sc_no="TRS170622000009";
        ListResult<FcsToolSchedulerLocationDto> result =schedulerService.getScheduleLocations(sc_no);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 按工装单状态获取工装单
     * @return
     */

    @Test
    public void getScheduleLocationsByStatusTest(){
        UUID scId = UUID.fromString("59f8f24c-0e1e-46d8-b59f-4d1cfd4942ad");
        AppointmentStatus status = AppointmentStatus.Appointmented;
        ListResult<FcsToolSchedulerLocationDto> result =schedulerService.getScheduleLocationsByStatus(scId,status);
        Assert.assertTrue(result.isSuccess());

    }


    //endregion end fetch

    //region start action

    /**
     * 新增计划
     * @return
     */

    @Test
    public void addToolSchedulerTest(){
        FcsToolSchedulerDto scheduler = new FcsToolSchedulerDto();
        scheduler.setSc_name("新增工装计划2");
        scheduler.setCurrent_location_no("当前位置2");
        scheduler.setOriginal_location_no("原始位置2");
        scheduler.setHouse_id("houseId2");
        scheduler.setHouse_no("29");
        scheduler.setScheduler_start_time(new Date());
        scheduler.setScheduler_end_time(DateUtl.parseDate("2017-07-03 12:00"));
        scheduler.setScheduler_type(SchedulerType.ExecutionOnce);
        scheduler.setTool_no("工装编码2");
        scheduler.setTool_type("拔吸嘴工装2");
        scheduler.setSc_luncher("fcs");
        scheduler.setDescription("工作描述");

        List<UUID> locationIds = new ArrayList<>();
        locationIds.add(UUID.fromString("acc8af26-bbb0-443b-97a2-68b804772ec7"));
        Assert.assertTrue(schedulerService.addToolScheduler(scheduler,locationIds).isSuccess());
    }

    /**
     重置已存在计划工作单
     * @return
     */
    @Test
    public void  resetScheduleLocationTest(){
        FcsToolSchedulerDto scheduler = new FcsToolSchedulerDto();
        scheduler.setId(UUID.fromString("abc0585d-73e7-4e8a-a975-1ecabfc84692"));
        scheduler.setSc_name("重置测试计划");
        scheduler.setCurrent_location_no("重置当前位置");
        scheduler.setHouse_id("重置houseId");
        scheduler.setHouse_no("重置29");
        scheduler.setScheduler_start_time(new Date());
        scheduler.setScheduler_end_time(DateUtl.parseDate("2017-07-01 12:00"));
        scheduler.setScheduler_type(SchedulerType.ExecutionOnce);
        scheduler.setTool_no("重置工装编码1231");
        scheduler.setTool_type("重置插吸嘴工装");
        scheduler.setOriginal_location_no("重置原始工装位");
        scheduler.setSc_luncher("重置后的发起者");
        scheduler.setDescription("重置后的工资2描述");

        List<UUID> locationIds = new ArrayList<>();
        locationIds.add(UUID.fromString("acc8af26-bbb0-443b-97a2-68b804772ec7"));
        Assert.assertTrue(schedulerService.resetScheduleLocation(scheduler,locationIds).isSuccess());
    }

    /**
     * 停止任务
     *
     * @return
     */
    @Test
    public void  stopSchedulerTest(){
        String schedulerNo ="TRS170622000009";
        GaiaResult result = schedulerService.stopScheduler(schedulerNo);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 启动任务
     *
     * @return
     */
    @Test
    public void  startSchedulerTest(){
        String schedulerNo ="TRS170622000009";
        GaiaResult result = schedulerService.startScheduler(schedulerNo);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 删除计划
     * @return
     */

    @Test
    public void  deleteSchedulerTest(){
        UUID schedulerId =UUID.fromString("222829a1-6dfc-499e-8067-740a7cd40b7f");
        GaiaResult result = schedulerService.deleteScheduler(schedulerId);
        Assert.assertTrue(result.isSuccess());

    }


    //endregion end action

    //region  start check
    /**
     * 计划是否已结束
     * @return
     */

    @Test
    public void  isSchedulerCompleteTest(){
        String schedulerNo ="TRS170622000009";
        GaiaResult result = schedulerService.isSchedulerComplete(schedulerNo);
        Assert.assertTrue(result.isSuccess());

    }


}
