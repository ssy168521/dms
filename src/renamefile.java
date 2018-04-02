
import java.io.File;


public class renamefile {

	public static void main(String[] args) throws Exception {
	 
		String filepath ="D:\\tiles_map\\tile" ;
		java.io.File myPath = new java.io.File(filepath);
		Handlefilename(myPath,true);
		Handlefilename(myPath,false); 
		
	}
	public static  void Handlefilename(File filepath,boolean fileflag) {
		//java.io.File myPath = new java.io.File(filepath);
		java.io.File[] aF = filepath.listFiles();
		String filename;
		int rowcol=0;
		String strrowcolhex="";
		String tmpstr="00000000";
		
		for(java.io.File fF : aF)
		{
			String strfilepath=fF.getParent();
			filename=fF.getName();
			if(fF.isDirectory()) {
				Handlefilename( fF,fileflag);
				if(!fileflag)
				{
					rowcol=Integer.parseInt(filename.substring(1));
					strrowcolhex=Integer.toHexString(rowcol);
					strrowcolhex=tmpstr.concat(strrowcolhex);
					strrowcolhex=strrowcolhex.toUpperCase();				
					File newfile=null;
					if(filename.startsWith("L"))
					{
						strrowcolhex=strrowcolhex.substring(strrowcolhex.length()-2);
						newfile=new File(strfilepath+"\\L"+strrowcolhex); 
					}
					else if(filename.startsWith("R"))
					{
						strrowcolhex=strrowcolhex.substring(strrowcolhex.length()-8);
						newfile=new File(strfilepath+"\\R"+strrowcolhex); 
					}
					
					fF.renameTo(newfile);
				}
			} 
			else 
			{
				if(fileflag)
				{
					rowcol=Integer.parseInt(filename.substring(1,filename.indexOf(".")));
					strrowcolhex=Integer.toHexString(rowcol);
					strrowcolhex=tmpstr.concat(strrowcolhex);
					strrowcolhex=strrowcolhex.substring(strrowcolhex.length()-8);
					strrowcolhex=strrowcolhex.toUpperCase();
					File newfile=new File(strfilepath+"\\C"+strrowcolhex+".png"); 
					fF.renameTo(newfile);
				}
			}
			
		}
	
	}
	/*public static String decode(String bytes) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
	    // 灏嗘瘡2浣�6杩涘埗鏁存暟缁勮鎴愪竴涓瓧鑺�
	    for (int i = 0; i < bytes.length(); i += 2)
	    baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
	                    .indexOf(bytes.charAt(i + 1))));
	    return new String(baos.toByteArray());
	}*/
}
