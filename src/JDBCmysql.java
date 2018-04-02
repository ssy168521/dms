import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JDBCmysql {
	public static void main(String[] args) {
		
		//异常捕获
		try {
			//1、注册MySQL驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			
			//2、连接数据库，通过driver manager连接
//			String  url="jdbc:mysql://192.168.1.177:3306/test";
//			String user="root";
//			String password ="123456";			
			String  url="jdbc:mysql://192.168.1.177:8066/test-mycat";
			String user="mycat";
			String password ="mycat";			
			Connection conn=DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功！ "+conn);
			
			//3、创建statement工具   Statement工具：发送SQL到数据库，并执行
			Statement state=conn.createStatement();
			
			//4、写一个SQL语句
			String strsql="insert into ";
			
			//5、Statement执行SQL语句     
			state.executeUpdate(strsql);  //executeUpdate执行增、删、改操作
			
			//6、关闭相关工具
			state.close();
			conn.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("加载驱动失败！");
			e.printStackTrace();//打印错误信息
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
	}
	
	public static Connection GetDBcon() {		
		Connection conn=null;
		//异常捕获
		try {
			//1、注册MySQL驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			
			//2、连接数据库，通过driver manager连接
//			String  url="jdbc:mysql://192.168.1.177:3306/test";
//			String user="root";
//			String password ="123456";			
			String  url="jdbc:mysql://192.168.1.177:8066/test-mycat";
			String user="mycat";
			String password ="mycat";			
			conn=DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功！ "+conn);			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("加载驱动失败！");
			e.printStackTrace();//打印错误信息
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
		return conn;
	}

	public static ResultSet ExecuteSQL(String strsql){		
		Connection conn=GetDBcon();
		ResultSet result=null;
		//3、创建statement工具   Statement工具：发送SQL到数据库，并执行		
		Statement state=null;
		try {
			state = conn.createStatement();
			//5、Statement执行SQL语句     
			state.executeUpdate(strsql);  //executeUpdate执行增、删、改操作			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL执行出错！");
			e.printStackTrace();
		}
				
		//6、关闭相关工具
		try {
			state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("State关闭出错！");
			e.printStackTrace();
		}
		
		CloseDBcon(conn);
		return result;
		
	}
	
	public static void CloseDBcon(Connection conn){
		try {
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Connection关闭出错！");
			e.printStackTrace();
		}	
	
	}

}
