package test.wxzd.efcs.application.instruction;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wxzd.efcs.business.application.service.MemoryInstructionAppService;
import com.wxzd.efcs.business.domain.entities.Instruction;
import com.wxzd.efcs.business.domain.enums.InstructionStatus;
import com.wxzd.efcs.business.domain.enums.InstructionType;
import com.wxzd.efcs.business.domain.enums.SendType;
import com.wxzd.gaia.jdbc.common.type.DatabaseType;
import com.wxzd.gaia.jdbc.core.annotation.Transaction;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.jdbc.core.connection.SqlExecuter;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-28
 */
@Service
public class DemoService {
	
	@Autowired
	private MemoryInstructionAppService memoryInstructionAppService;
	
	@Transaction
	public void demo() {
		DatabaseExecuter.execute(new SqlExecuter(){
			@Override
			public Object deal(Connection connection, DatabaseType databaseType) {
				System.out.println(connection);
				try {
					System.out.println(connection.getAutoCommit());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}});
		Instruction inst = new Instruction("29");
		inst.setInstr_no("1");
		inst.setHouse_no("29");
		inst.setQueue_no("29110");
		inst.setPallet_no("托盘编号1");
		inst.setInstr_type(InstructionType.Stock_Out);
		inst.setEquip_no("110");
		inst.setFrom_pos("LA020202");
		inst.setTo_pos("1320");
		inst.setSend_type(SendType.QueuesDirect);
		inst.setInstr_status(InstructionStatus.Created);
		inst.setInstr_level(1);
		memoryInstructionAppService.createInstr(inst);
	}
}
