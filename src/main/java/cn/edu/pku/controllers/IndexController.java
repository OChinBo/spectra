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
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;

public class IndexController {

	@FXML
	private javafx.scene.control.TabPane tabPane;

	private File sourceFile;

	private File outputFile;

	private String defaultDirectory = ".";

	private LineChart<Number, Number> linechart;



	@FXML
	private void closeWindowsAction() {
		Platform.exit();
	}

	@FXML
	private void openFileAction(ActionEvent ae) {

		Window window = ((Node) ae.getTarget()).getScene().getWindow();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(defaultDirectory));

		// Assign selected file to local parameter
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("files", PropertiesUtils.readFormats()));
		sourceFile = fileChooser.showOpenDialog(window);

		if(sourceFile != null){
			// Update the latest directory as defaultDirectory
			defaultDirectory = sourceFile.toString() + "/..";

			// Create TabController and then add Tab
			XYChart.Series<Number, Number> series = readFileIntoSeries(sourceFile);
			TabController tab = new TabController(series);
			tab.setText(sourceFile.getName());
			tabPane.getTabs().add(tab);
		}

	}

	@FXML
	private void saveAction(ActionEvent ae) {
		writeFile(sourceFile);

		/*
		Window window = ((Node) ae.getTarget()).getScene().getWindow();

		File file = new File(filename);
		FileChooser fileChooser = new FileChooser();
		File dest = fileChooser.showSaveDialog(window);
		if (dest != null) {
		    try {
		        Files.copy(file.toPath(), dest.toPath());
		    } catch (IOException ex) {
		        // handle exception...
		    }
		}
*/
	}

	@FXML
	private void saveAsAction(ActionEvent ae) {

		Window window = ((Node) ae.getTarget()).getScene().getWindow();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(defaultDirectory));

		// 暫時封印自動填入檔名,考慮到開啟多個檔時或許會衝突
		/*
		if(sourceFile != null){
			fileChooser.setInitialFileName(sourceFile.getName());
		}*/


		// Set format of filter
		FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All", "*");
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(allFilter);
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		outputFile = fileChooser.showSaveDialog(window);

		if(outputFile != null){

			// Update the latest directory as defaultDirectory
			defaultDirectory = outputFile.toString() + "/..";

			writeFile(outputFile);
		}

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
			TabController tab = (TabController) tabPane.getSelectionModel().getSelectedItem() ;
			linechart = tab.getLineChart() ;
			
			for (int i = 0; i < linechart.getData().size(); i++) {
				
				XYChart.Series<Number, Number> series = linechart.getData().get(i);
				
				for (int j = 0; j < series.getData().size(); j++) {
					
					System.out.println(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.write(series.getData().get(j).getXValue() + "," + series.getData().get(j).getYValue());
					out.newLine();
					
				} // end of for
				
			} // end of try
			
			out.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} // end of catch
		
	} // end of writeFile()

	// 03/16 Hamilton, did use the function to implement the writeFile function
	private int getSelectedTabIndex(){
		
		System.out.println("Current tab index:" + tabPane.getSelectionModel().getSelectedIndex());
		TabController tab = (TabController) tabPane.getSelectionModel().getSelectedItem() ;
		return tabPane.getSelectionModel().getSelectedIndex();
		
	} // end of gerSelectedTabIndex()

}
