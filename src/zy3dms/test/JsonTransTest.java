package zy3dms.test;

import java.util.List;

import net.sf.json.JSONArray;

import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.usermanager.service.ManagementService;

public class JsonTransTest {

	public JsonTransTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//将字符串转换为json对象，然后根据建得到相应的值  
        ParseJson pj = new ParseJson("{\"name\":\"gu\",\"num\":123456,\"sex\":\"male\",\"age\":24}");  
        pj.parse();  
          
        //将一个json字符串转换为java对象  
        Person p = pj.JSON2Object();  
        System.out.println("Name:" + p.getName());  
        System.out.println("Num:" + p.getNum());  
        System.out.println("Sex:" + p.getSex());  
        System.out.println("age:" + p.getAge());  
          
        //将一个java对象转换为Json字符串  
        Person p1 = new Person("gu1",123,"male",23);  
        ConsJson cj = new ConsJson();
        String str1 = cj.Object2Json(p1);
        System.out.println(str1);
        
        ManagementService manaServ=new ManagementService();
		List<UserinfoDto> userlist=manaServ.getRoleDao().getUserlist();
		
		UserinfoDto userInforDto=userlist.get(1);
		List<Privinfo> pPrivinfos=userInforDto.getPrivlist();
		
		//JSONObject jsonObj=JSONObject.fromObject(userlist);
		JSONArray jsArray=JSONArray.fromObject(userlist);
		String str=jsArray.toString();
		System.out.println(str);
        
        

	}

}
