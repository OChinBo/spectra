package cn.edu.pku.dao;

import javafx.beans.property.SimpleStringProperty;

/**
 * tableview : Columns of X-Axis and Y-Axis
 */

public class tableViewContent {

	private SimpleStringProperty x;
    private SimpleStringProperty y;

    public tableViewContent( String _x, String _y ) {

    	this.x = new SimpleStringProperty(_x) ;
    	this.y = new SimpleStringProperty(_y) ;

    } // end of Constructor

    // Getter
    public String getX() {

    	return x.get() ;

    } // end of getX()

    public String getY() {

    	return y.get() ;

    } // end of getY()

    // Setter
    public void setX( String _x ) {

    	this.x.set(_x) ;

    } // end of setX()

    public void setY( String _y ) {

    	this.y.set(_y) ;

    } // end of setY()

}
