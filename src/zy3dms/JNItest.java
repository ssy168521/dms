package zy3dms;

public class JNItest {
	
	static{
		System.loadLibrary("test");//系统环境变量中或者jre/bin中dll文件
	}
	
	public native int calc(int a);

	public static void main(String[] args) {
		JNItest jnItest=new JNItest();
		int result=jnItest.calc(10);
		System.out.println(result);
	}

}
