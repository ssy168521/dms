package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.web.dao.XmlTable;

public class UploadXML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 保存文件的目录
	private static String PATH_FOLDER = "/";
	// 存放临时文件的目录
	private static String TEMP_FOLDER = "/";

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext servletCtx = config.getServletContext();
		// 初始化路径
		// 保存文件的目录
		PATH_FOLDER = servletCtx.getRealPath("/upload");
		// 存放临时文件的目录,存放xxx.tmp文件的目录
		TEMP_FOLDER = servletCtx.getRealPath("/upload");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); // 设置编码
		response.setCharacterEncoding("utf-8");
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); // 设置编码
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		// 获得磁盘文件条目工厂
				DiskFileItemFactory factory = new DiskFileItemFactory();
				
				// 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
				// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
				/**
				 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
				 * 格式的 然后再将其真正写到 对应目录的硬盘上
				 */
				factory.setRepository(new File(TEMP_FOLDER));
				// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
				factory.setSizeThreshold(1024 * 1024);

				// 高水平的API文件上传处理
				ServletFileUpload upload = new ServletFileUpload(factory);
		
				try {
					// 提交上来的信息都在这个list里面
					// 这意味着可以上传多个文件
					// 请自行组织代码
					List<FileItem> list = upload.parseRequest(request);
					// 获取上传的文件
					FileItem item = getUploadFileItem(list);
					// 获取文件名
					String filename = getUploadFileName(item);
					// 保存后的文件名
					String saveName = new Date().getTime() + filename.substring(filename.lastIndexOf("."));
					// 保存后文件的浏览器访问路径
					String docUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/upload/"+saveName;

					System.out.println("存放目录:" + PATH_FOLDER);
					System.out.println("文件名:" + filename);
					System.out.println("浏览器访问路径:" + docUrl);

					// 真正写到磁盘上
					item.write(new File(PATH_FOLDER, saveName)); // 第三方提供的
					
					File uploadFile= new File(PATH_FOLDER+"//"+saveName);
				    String jsonTableList=null;
					
					if(uploadFile.exists()){
						List<XmlTable> tables = new ArrayList<XmlTable>();
						SAXReader sax=new SAXReader();//创建一个SAXReader对象
						File xmlFile=uploadFile;
						Document document=sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
						Element root=document.getRootElement();//获取根节点
						JSONArray jsonArray=null;
						jsonArray = JSONArray.fromObject(this.getNodes(root,tables));
						jsonTableList = jsonArray.toString();
						//jsonTableList="[{\"fieldName\":\"CreaDate\",\"nodecontent\":\"20170821\",\"nodepath\":\"/metadata/Esri/CreaDate\"},{\"fieldName\":\"Process\",\"nodecontent\":\"BuildPyramids H:\\水利部第五六批成果\\第五六批成果\\J46D001001.TIF -1 NONE NEAREST DEFAULT 75 OVERWRITE\",\"nodepath\":\"/metadata/Esri/DataProperties/lineage/Process\"}]";
					}
					PrintWriter out = response.getWriter();
					out.print(jsonTableList);
					out.flush();
					out.close();
					
				} catch (FileUploadException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	/**
	* 从指定节点开始,递归遍历所有子节点
	*/
	//List<XmlTable> tables = new ArrayList<XmlTable>();
	private List<XmlTable> getNodes(Element node,List<XmlTable> tables){		 
		String a=node.getTextTrim();
			if(a.length()!=0){
				//当前节点的名称、文本内容和属性
				XmlTable content = new XmlTable();
				String nodeName=node.getName();
				String nodecontent=node.getTextTrim();
				String fieldName=null;
				String nodepath=node.getPath();
				
				content.setNodeName(nodeName);
				content.setNodecontent(nodecontent);
				content.setNodepath(nodepath);
				content.setFieldName(fieldName);
				tables.add(content);
			}
		
//			List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
//			for(Attribute attr:listAttr){//遍历当前节点的所有属性
//				String name=attr.getName();//属性名称
//				String value=attr.getValue();//属性的值
//				System.out.println("属性名称："+name+"属性值："+value);	
//			}		
			//递归遍历当前节点所有的子节点
			List<Element> listElement=node.elements();//所有一级子节点的list
				for(Element e:listElement){//遍历所有一级子节点
					this.getNodes(e,tables);//递归
					
			}
//				JSONArray jsonArray = JSONArray.fromObject(tables);
//				jsonTableList = jsonArray.toString();
				return tables;
	
	}
	private FileItem getUploadFileItem(List<FileItem> list) {
		for (FileItem fileItem : list) {
			if(!fileItem.isFormField()) {
				return fileItem;
			}
		}
		return null;
	}
	/**
	 * 判断数据类型：字符串型，浮点型，整型
	 * @param item
	 * @return
	 */
//	public static String convert(String item) {
//	    item = item.trim();
//	    if ("true".equalsIgnoreCase(item) || "false".equalsIgnoreCase(item)) {
//	       // return Boolean.valueOf(item);
//	    	return "布尔型";
//	    }
//	    if (item.matches(INT_PATTERN)) {
//	       // return Integer.valueOf(item);
//	    	return "整型";
//	    }
//	    if (item.matches(DOUBLE_PATTERN)) {
//	       // return Double.valueOf(item);
//	        return "浮点型";
//	    }
//	    return "字符型";
//	}
	
	private String getUploadFileName(FileItem item) {
		// 获取路径名
		String value = item.getName();
		// 索引到最后一个反斜杠
		int start = value.lastIndexOf("/");
		// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
		String filename = value.substring(start + 1);
		
		return filename;
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}


}
