package cn.edu.pku.dao;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Hamilton Chang 2018/03/19
 */

public class tableViewContent {

	private SimpleStringProperty x;
    private SimpleStringProperty y;

    public tableViewContent( String _x, String string ) {

    	this.x = new SimpleStringProperty(_x) ;
    	this.y = new SimpleStringProperty(string) ;

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

    	this.x = new SimpleStringProperty(_x) ;

    } // end of setX()

    public void setY( String _y ) {

    	this.y = new SimpleStringProperty(_y) ;

    } // end of setY()

}
