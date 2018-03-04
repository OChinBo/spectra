package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.glass.ui.Window;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class IndexController {

	private File sourceFile;
	private Scene scene ;
	private ArrayList<LineChart> linechartlist = new ArrayList<>() ;
	
	// @FXML
	// private javafx.scene.control.MenuItem closeButton;
	private javafx.scene.control.TabPane tabpane ;

	@FXML
	private void closeWindowsAction() {
		// System.exit(0);
		Platform.exit();
	}

	/***** MenuBar Buttons *****/
	@FXML
	private void openFileAction(ActionEvent ae) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Assign selected file to local parameter
		sourceFile = fileChooser.showOpenDialog(null);

		LineChart<Number, Number> linechart = readFileByLines(sourceFile);
		
		Tab tab = new Tab() ;
		tab.setId("tab");
		tab.setContent(linechart);
		tabpane.getTabs().add(tab);

	}

	@FXML
	private void saveAsAction(ActionEvent ae) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		// Set format of filter
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		sourceFile = fileChooser.showSaveDialog(null);

		// System.out.print(sourceFile);
	}

	private LineChart<Number, Number> readFileByLines(File file) {
		
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
		
	    final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
	    XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
	    series.setName("spectra Data");
		
		try{
		    
			BufferedReader  in = new BufferedReader(new FileReader(file));

			// read a line once a time, until the end of the file which is null
			int line = 1;
			String tempString = null;

			//跳過第一行文檔標頭
			//System.out.println(in.readLine());
			
			while ( (tempString = in.readLine()) != null ) {
				
				if ( !tempString.isEmpty() && Character.isDigit(tempString.charAt(0)) ) {
					// show line number
					String[] tmpString = tempString.split(",") ;
					System.out.println("line " + line + ": " + tempString);
					System.out.println(tmpString[0] + ", " + tmpString[1]);
					series.getData().add(new XYChart.Data<Number, Number>(Double.parseDouble(tmpString[0]), Double.parseDouble(tmpString[1])));
					line++;
				} // end of if
				
				else {
					; // DO NOTHING
				} // end of else
				
			} // end of while

			in.close();
			
			//setLineChart() ;

		}catch(IOException e){
			e.printStackTrace();
		} // end of try catch
		return lineChart ;

	} // end of readFileByLines()

	private void setLineChart(){
		
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
	    xAxis.setLabel("Number of Month");
	    final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(
	        xAxis, yAxis);

	    lineChart.setTitle("Line Chart");
	    XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
	    series.setName("My Data");
	    // populating the series with data
	    series.getData().add(new XYChart.Data<Number, Number>(1.111, 23));
	    series.getData().add(new XYChart.Data<Number, Number>(2, 114));
	    series.getData().add(new XYChart.Data<Number, Number>(3, 15));
	    series.getData().add(new XYChart.Data<Number, Number>(4, 124));

	    scene = new Scene(lineChart, 800, 600);
	    lineChart.getData().add(series);

//	    stage.setScene(scene);
//	    stage.show();    
	    
	} // end of setLineChart()
	
	public Scene showLineChart(){
		return scene ;
	} // end of showLineChart()
	
}
