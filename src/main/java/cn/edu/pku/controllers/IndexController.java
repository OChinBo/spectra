package cn.edu.pku.controllers;

import cn.edu.pku.controllers.TabController;
import cn.edu.pku.util.PropertiesUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class IndexController {

	@FXML
	private javafx.scene.control.TabPane tabPane;

	private File sourceFile;
	private File outputFile;
	private LineChart<Number, Number> linechart;


	@FXML
	private void closeWindowsAction() {
		// System.exit(0);
		Platform.exit();
	}

	@FXML
	private void openFileAction(ActionEvent ae) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Assign selected file to local parameter
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Files", PropertiesUtils.readFormats()));
		sourceFile = fileChooser.showOpenDialog(null);


		XYChart.Series<Number, Number> series = readFileIntoSeries(sourceFile);
		TabController tab = new TabController(series);

		tab.setText(sourceFile.getName());
		tabPane.getTabs().add(tab);
	}

	@FXML
	private void saveAction(ActionEvent ae) {
		writeFile(sourceFile);
	}

	@FXML
	private void saveAsAction(ActionEvent ae) {
		getSelectedTab();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		// Set format of filter
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		outputFile = fileChooser.showSaveDialog(null);
		writeFile(outputFile);
	}



	/**
	 * 讀檔後回傳Series
	 * @param file
	 * @return XYChart.Series<Number, Number>
	 *
	 */
	private XYChart.Series<Number, Number> readFileIntoSeries(File file) {


		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("spectra Data");

		try {

			BufferedReader in = new BufferedReader(new FileReader(file));

			// Read a line once a time, until the end of the file which is null
			String tempString = null;

			while ((tempString = in.readLine()) != null) {

				linechartdata tmpdata = new linechartdata();

				if (!tempString.isEmpty() && Character.isDigit(tempString.charAt(0))) {
					// Show line number
					String[] tmpString = tempString.split(",");
					tmpdata.setXY(Double.parseDouble(tmpString[0]), Double.parseDouble(tmpString[1]));
					series.getData().add(new XYChart.Data<>(Double.parseDouble(tmpString[0]),
							Double.parseDouble(tmpString[1])));
				}
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return series;
	}

	private void writeFile(File file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < linechart.getData().size(); i++) {
				XYChart.Series<Number, Number> series = linechart.getData().get(i);
				for (int j = 0; j < series.getData().size(); j++) {
					System.out.println(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.write(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.newLine();
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getSelectedTab(){
		System.out.print("Current tab index:" + tabPane.getSelectionModel().getSelectedIndex());
		return tabPane.getSelectionModel().getSelectedIndex();
	}

}
