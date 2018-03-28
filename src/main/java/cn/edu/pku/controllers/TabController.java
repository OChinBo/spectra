package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.pku.dao.Dao;
import cn.edu.pku.entity.tableViewContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private javafx.scene.control.TabPane tabPane ;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private TableView<tableViewContent> tableView = new TableView<>();

	@FXML
	private TableColumn xColumn;

	@FXML
	private TableColumn yColumn;

	private Series<Number, Number> series;

	// Constructor
    public TabController( XYChart.Series<Number, Number> series ) {

    	this.series = series;

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

    } // end of Constructor TabController()

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		lineChart.getData().add(series);

		// Complete, 03/26
		// Fill data to tableview
		// Get data from specific linechart
		xColumn.setCellValueFactory(new PropertyValueFactory<tableViewContent,String>("x"));
		yColumn.setCellValueFactory(new PropertyValueFactory<tableViewContent,String>("y"));
		tableView.setItems(getTableContent());

	} // end of initialize()

	public LineChart<Number, Number> getLineChart() {

		return lineChart ;

	} // end of getLineChart()

	public void setTableView( TableView<tableViewContent> tableview  ) {

		this.tableView = tableview ;

	} // end of setTableView()

	/**
	 *
	 *1. Get data from THE linechart
	 *2. Assign/Set data to THE tableview
	 *@return ObservableList<tableViewContent>
	 *
	 * */
	public ObservableList<tableViewContent> getTableContent() {

		ObservableList<tableViewContent> tvdata = FXCollections.observableArrayList() ;

		// get data from series linechart
		for (int i = 0; i < lineChart.getData().size(); i++) {

			XYChart.Series<Number, Number> series = lineChart.getData().get(i);

			for (int j = 0; j < series.getData().size(); j++) {

				// add data into tableViewContent
				tableViewContent tmp = new tableViewContent(series.getData().get(j).getXValue().toString(),
						                                    series.getData().get(j).getYValue().toString()) ;
				tvdata.add(tmp); // add tableViewContent into ObservableList<>

			} // end of for

		} // end of for

        return tvdata;

    } // end of getTableContent()

}
