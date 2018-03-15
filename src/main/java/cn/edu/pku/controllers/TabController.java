package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabController extends Tab implements Initializable {

	
	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private TableView<Series> tableView;

	@FXML
	private TableColumn xColumn;

	@FXML
	private TableColumn yColumn;


	private Series<Number, Number> series;


	// Constructor
    public TabController( XYChart.Series<Number, Number> series ) {
    	System.out.println("incon:");
    	this.series = series;
    	System.out.println("this series:" + this.series.getData());

    	// Set root, Tab.fxml is fx:root
		FXMLLoader tabLoader = new FXMLLoader(getClass().getResource("/view/Tab.fxml"));
        tabLoader.setRoot(this);
        tabLoader.setController(this);


		try {
			tabLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		//CSS
        //getStyleClass().add(getClass().getResource("/css/tab.css").toExternalForm());




    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println("Arg0:" + arg0);
		System.out.println("Arg1:" + arg1);

		lineChart.getData().add(series);
		//tableView;

		xColumn.setCellValueFactory(new PropertyValueFactory<>("xAxis"));
		//tableView.setItems(series);
	}



}
