package com.web.action;

import java.sql.Connection;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.DataGrid;
import com.web.form.Staticstis;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;

@Controller
@Scope("prototype") 
@RequestMapping("/log")
public class LogAction {

	private WebService service = new WebServiceImpl();
	
	@ResponseBody
    @RequestMapping("/dataGridInfo")
	public DataGrid dataGridInfo() {
		Connection conn = null;
		
		DataGrid dg = null;
		
		try {
			conn = DbUtils.getConnection(true);
			
			dg = new DataGrid();
			
			List<Staticstis> list = service.getStaticstisInfo(conn, null);
			
			if(list != null) {
				dg.setTotal(Long.parseLong(String.valueOf(list.size())));
				dg.setRows(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}
		
		return dg;
	}
}
