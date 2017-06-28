package test.wxzd.efcs.application.dababase.procedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.wxzd.gaia.jdbc.common.type.DatabaseType;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;
import com.wxzd.gaia.jdbc.core.connection.SqlExecuter;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 
 * 
 * @version 1
 * @author y
 * @.create 2017-04-21
 */
public class SpringDemo extends SpringDemoBase {

	@Test
	public void name() throws Exception {
		String result = (String) DatabaseExecuter.execute(new SqlExecuter() {
			@Override
			public Object deal(Connection connection, DatabaseType databaseType) {
				try {
					String sql = "{call GET_BILL_NO(?,?)}";
					CallableStatement call = connection.prepareCall(sql);
					call.setString(1, "FM");
					call.registerOutParameter(2, java.sql.Types.VARCHAR);
					call.execute();
					String result = call.getString(2);
					return result;
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

			}
		});
		System.out.println(result);
	}

//	@Test
//	public void demoMethod() {
//		String no = Bill_No_Utl.GET_BILL_NO("FM");
//		System.out.println(no);
//	}
}
