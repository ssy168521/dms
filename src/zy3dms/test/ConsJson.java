package zy3dms.test;

import net.sf.json.JSONObject;

public class ConsJson {

	public ConsJson() {
		// TODO Auto-generated constructor stub
	}
	public String Object2Json(Object obj){  
        JSONObject json = JSONObject.fromObject(obj);//将java对象转换为json对象  
        String str = json.toString();//将json对象转换为字符串  
          
        return str;  
    }

}
