package test.wxzd.efcs.application.dababase.bean;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wxzd.gaia.common.base.core.string.UuidUtl;
import com.wxzd.gaia.jdbc.core.connection.DatabaseExecuter;

import test.wxzd.efcs.application.mold.SpringDemoBase;

/**
 * 研究了下关于时间的处理
 * 
 * @version 1
 * @author y
 * @.create 2017-04-01
 */
public class SpringDemo extends SpringDemoBase {

	@Autowired
	TempRepository tempRepository;

	@Test
	public void check() {
		System.out.println(tempRepository);
	}

	@Test
	public void insert() {
		TempDomain temp = new TempDomain();
		temp.setCreate_date(new Date());
		tempRepository.saveById(temp);
	}

	String id = "11baec16-8bf5-4d59-9d00-93932f4f8c54";

	@Test
	public void query() {
		String uuid = id;
		TempDomain temp = tempRepository.getById(UUID.fromString(uuid));
		System.out.println(temp);
		System.out.println(temp.getCreate_date());
	}

	@Test
	public void sql() {
		String sql = "select create_date from temp where id = '" + id + "'";
		Map<String, Object> queryEntity = DatabaseExecuter.queryEntity(sql);
		System.out.println(queryEntity.get("create_date"));
	}

}
