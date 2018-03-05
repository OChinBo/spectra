package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class IndexController {
	
	private File sourceFile;
	private File outputFile ;
	private LineChart<Number, Number> linechart ;

	@FXML
	// private javafx.scene.control.MenuItem closeButton;
	private javafx.scene.control.TabPane tabp ;

	@FXML
	private void closeWindowsAction() {
		
		// System.exit(0);
		Platform.exit();
		
	} // end of closeWindowsAction()
	
	/***** MenuBar Buttons *****/
	@FXML
	private void openFileAction(ActionEvent ae) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		// Assign selected file to local parameter
		sourceFile = fileChooser.showOpenDialog(null);

		readFileByLines(sourceFile);
		
		Tab tab = new Tab() ;
		tab.setText(sourceFile.getName());
		tab.setContent(linechart);
		tabp.getTabs().add(tab);

	} // end of openFileAction()

	@FXML
	private void saveAsAction(ActionEvent ae) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		// Set format of filter
		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		outputFile = fileChooser.showSaveDialog(null);
		writeFile(outputFile) ;

	} // end of saveAsAction()

	private void readFileByLines(File file) {	
		
		final NumberAxis xAxis = new NumberAxis();
	    final NumberAxis yAxis = new NumberAxis();
		
	    final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
	    linechart = new LineChart<Number, Number>(xAxis, yAxis);
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
				
				linechartdata tmpdata = new linechartdata() ;
				
				if ( !tempString.isEmpty() && Character.isDigit(tempString.charAt(0)) ) {
					
					// show line number
					String[] tmpString = tempString.split(",") ;
//					System.out.println("line " + line + ": " + tempString);
//					System.out.println(tmpString[0] + ", " + tmpString[1]);
					tmpdata.setXY(Double.parseDouble(tmpString[0]), Double.parseDouble(tmpString[1]));
					series.getData().add(new XYChart.Data<Number, Number>(Double.parseDouble(tmpString[0]), Double.parseDouble(tmpString[1])));
					line++;
					
				} // end of if
				
				else {
					; // DO NOTHING
				} // end of else
				
			} // end of while

			in.close();

		}catch(IOException e){
			e.printStackTrace();
		} // end of try catch
		
		linechart.getData().add(series);

	} // end of readFileByLines()
	
	private void writeFile( File file ) {
		
		try {
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			for( int i = 0 ; i < linechart.getData().size() ; i++ ) {

				XYChart.Series<Number, Number> series = linechart.getData().get(i) ;
				
				for( int j = 0 ; j < series.getData().size() ; j++ ) {
					
					System.out.println( series.getData().get(j).getXValue() + ", " + series.getData().get(j).getYValue());
					out.write(series.getData().get(j).getXValue() + ", " + series.getData().get(j).getYValue() + "\n");
					
				} // end of for

			} // end of for
						
			out.close();
			
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		} // end of try catch
		
	} // end of writeFile()
	
}
