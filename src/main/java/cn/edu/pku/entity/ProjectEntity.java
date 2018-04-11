package cn.edu.pku.entity;

import javafx.scene.chart.LineChart;
import javafx.scene.control.TableView;
import org.json.*;

public class ProjectEntity {

	// Parameters for project setup
	private LineChart<Number, Number> lineChart;

	private TableView<tableViewContentEntity> tableView;


	// Maybe we can only return parameters, data could be ignore?
	public String toString(){
		return "LineChart: " + this.lineChart + ", TableView: " + this.tableView;
	}

	public JSONObject toJson(){
		return null;
	}
}
