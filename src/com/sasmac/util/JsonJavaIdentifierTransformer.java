package com.sasmac.util;

import net.sf.json.util.JavaIdentifierTransformer;

public class JsonJavaIdentifierTransformer extends JavaIdentifierTransformer
{
	@Override  
	public String transformToJavaIdentifier(String str)
	{ 
	  return str.toLowerCase(); 
	} 
}
