package com.sasmac.jni;

public class ImageProduce {
	static{
		System.loadLibrary("ImageProduce");
	}
	//传入含jpw文件的jpg，经过重采样，
	public native boolean ImageRectify(String strInFileName,String strOutFilePath,int outSizeX,int outSizeY);
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImageProduce imgprodu=new ImageProduce();
		boolean resu= imgprodu.ImageRectify("E:\\database\\data\\J46D001001.tif","E:\\database\\test\\J46D001001.png",256,256);
		System.out.println(resu);

	}

}
