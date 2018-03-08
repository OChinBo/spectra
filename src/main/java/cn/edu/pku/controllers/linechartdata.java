package main.java.cn.edu.pku.controllers;

public class linechartdata {

	private Double x ;
	private Double y ;

	linechartdata() {
		this.x = 0.0 ;
		this.y = 0.0 ;
	} // end of CONSTRUCTOR

	public void setX( Double x ) {
		this.x = x ;
	} // end of setX

	public void setY( Double y ) {
		this.y = y ;
	} // end of setY

	public void setXY( Double x, Double y) {
		this.x = x ;
		this.y = y ;
	} // end of setXY

	public Double getX() {
		return this.x ;
	} // end of getXY

	public Double getY() {
		return this.y ;
	} // end of getY

}
