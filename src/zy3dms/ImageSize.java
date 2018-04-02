package zy3dms;

public class ImageSize {
	private String Name="";
	private double y=0.0;
	public String getName() {
		return this.Name;		
	}
	
	public double gety() {
		return this.y;
	}
	
	public void setName(String strsatename) {
		this.Name=strsatename;		
	}
	
	public void sety(double dbimgsize) {
		this.y=dbimgsize;
	}
}
