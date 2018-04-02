/**  
 * @Title: ConfigureAction.java
 * @Package com.web.action
 * @Description: TODO
 * @author zwt
 * @date 2016-12-30
 */
package com.web.action;

import java.sql.Connection;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.DataGrid;
import com.web.common.Json;
import com.web.form.ConfigureForm;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
import com.web.util.Log;

/**
 * ClassName: ConfigureAction
 * 
 * @Description: 閰嶇疆鐣岄潰action灞�
 * @date 2016-12-30
 */

@Controller
@Scope("prototype")
@RequestMapping("/configure")
public class ConfigureAction {
	private WebService service = new WebServiceImpl();

	/**
	 * @Description: 璇诲彇鍒楄〃
	 * @param @return
	 * @return DataGrid
	 * @throws
	 * @date 2016-12-30
	 */
	@ResponseBody
	@RequestMapping("/dataGridInfo")
	public DataGrid dataGridInfo() {
		Connection conn = null;

		DataGrid dg = null;

		try {
			conn = DbUtils.getConnection(true);

			dg = new DataGrid();

			List<ConfigureForm> list = service.getAllSysConfig(conn);

			if (list != null) {
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

	/**
	 * @Description: 娣诲姞淇℃伅
	 * @param @return
	 * @return DataGrid
	 * @throws
	 * @date 2016-12-30
	 */
	@ResponseBody
	@RequestMapping("/addInfo")
	public Json addInfo(ConfigureForm configureForm) {
		Log.println("");
		Log.println("Add system configuration.");
		Connection conn = null;
		Json j = new Json();
		try {
			conn = DbUtils.getConnection(true);

			System.out.println(configureForm.getId());
			if (service.addSysConfig(conn, configureForm)) {
				j.setSuccess(true);
				j.setMsg("配置功能 !");
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("配置发生错误!");
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}
		return j;
	}

	/**
	 * @Description: 缂栬緫淇℃伅
	 * @param @return
	 * @return DataGrid
	 * @throws
	 * @date 2016-12-30
	 */
	@ResponseBody
	@RequestMapping("/editInfo")
	public Json editInfo(ConfigureForm configureForm) {
		Log.println("");
		Log.println("Edit system configuration.");
		Connection conn = null;
		Json j = new Json();
		try {
			conn = DbUtils.getConnection(true);

			if (service.updateSysConfig(conn, configureForm)) {
				j.setSuccess(true);
				j.setMsg("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("淇敼閰嶇疆淇℃伅鍑洪敊!");
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}
		return j;
	}

	/**
	 * @Description: 鍒犻櫎淇℃伅
	 * @param @return
	 * @return DataGrid
	 * @throws
	 * @date 2016-12-30
	 */
	@ResponseBody
	@RequestMapping("/deleteInfo")
	public Json deleteInfo(ConfigureForm configureForm) {
		Log.println("");
		Log.println("Delet system configuration.");
		Log.println("Config id is: " + configureForm.getId());
		Connection conn = null;
		Json j = new Json();
		try {
			conn = DbUtils.getConnection(true);

			String satellite = service.getConfigById(conn,
					configureForm.getId()).getSatellite();
			if (service.deleteSysConfig(conn, configureForm)) {

				// Constants.removeThread(satellite);
				Log.println("Delete system config: " + satellite
						+ " and the archive thread stoped.");

				j.setSuccess(true);
				j.setMsg("鍒犻櫎鎴愬姛!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("鍒犻櫎閰嶇疆淇℃伅鍑洪敊!");
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}
		return j;
	}

	@ResponseBody
	@RequestMapping("/stopThread")
	public Json stopThread(ConfigureForm configureForm) {
		Json j = new Json();
		// Constants.setThreadStatus(configureForm.getSatellite(),
		// Constants.THREAD_STATUS_STOPED);
		// System.out.println(configureForm.getId());
		return j;
	}
}
