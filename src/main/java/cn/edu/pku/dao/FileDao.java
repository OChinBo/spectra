package cn.edu.pku.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.edu.pku.controllers.TabController;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class FileDao extends Dao<XYChart.Series<Number, Number>, File, LineChart<Number, Number>> {

	@FXML
	private javafx.scene.control.TabPane tabPane;
	private LineChart<Number, Number> linechart ;

	File data = null;

	// Constructor
	public FileDao(File data) {
		this.data = data;
	}

	@Override
	public XYChart.Series<Number, Number> read() {

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("spectra Data");

		try {
			BufferedReader in = new BufferedReader(new FileReader(data));
			String tempString = null;

			// Read a line once a time, until the end of the file which is null
			while ((tempString = in.readLine()) != null) {
				if (!tempString.isEmpty() && Character.isDigit(tempString.charAt(0))) {
					// separate X-Axis and Y-Axis with ','
					String[] tmpString = tempString.split(",");
					// Add data into series
					series.getData().add(
							new XYChart.Data<>(Double.parseDouble(tmpString[0]), Double.parseDouble(tmpString[1])));
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return series;
	}

	@Override
	public void write(File data) {
		// TODO Auto-generated method stub
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(data));
			// 03/16, get the specific linechart from specific tab
			TabController tab = (TabController) tabPane.getSelectionModel().getSelectedItem();
			LineChart<Number, Number> linechart = tab.getLineChart();

			for (int i = 0; i < linechart.getData().size(); i++) {
				XYChart.Series<Number, Number> series = linechart.getData().get(i);
				for (int j = 0; j < series.getData().size(); j++) {
					// System.out.println(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.write(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.newLine();
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(File data, LineChart<Number, Number> lc) {
		// TODO Auto-generated method stub

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(data));

			for (int i = 0; i < lc.getData().size(); i++) {

				XYChart.Series<Number, Number> series = lc.getData().get(i);
				for (int j = 0; j < series.getData().size(); j++) {
					out.write(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.newLine();
				}

			}

			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setTabPane(javafx.scene.control.TabPane tabPane) {
		this.tabPane = tabPane;
	}

	public javafx.scene.control.TabPane getTabPane() {
		return this.tabPane;
	}

}
