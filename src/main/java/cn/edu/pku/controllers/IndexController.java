package cn.edu.pku.controllers;

import cn.edu.pku.dao.Dao;
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
	private Dao dao = new Dao();
	private File sourceFile;
	private File outputFile;
	private String defaultDirectory = ".";

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
			XYChart.Series<Number, Number> series = dao.readFileIntoSeries(sourceFile);
			TabController tab = new TabController(series);
			tab.setText(sourceFile.getName());
			tabPane.getTabs().add(tab);
			dao.setTabPane(tabPane);  // set THE tabpane to dao package

		} // end of if

	} // end of openFileAction()

	// 存檔
	@FXML
	private void saveAction(ActionEvent ae) {

		dao.writeFile(sourceFile);

	} // end of saveAction()

	// 另存新檔
	@FXML
	private void saveAsAction(ActionEvent ae) {

		// 得到當前視窗
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
		// FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().add(allFilter);
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);
		// fileChooser.getExtensionFilters().add(jpgFilter);

		outputFile = fileChooser.showSaveDialog(window);

		if(outputFile != null){

			// Update the latest directory as defaultDirectory
			defaultDirectory = outputFile.toString() + "/..";
			dao.writeFile(outputFile);

		} // end of if

	} // end of saveAsAction()

}
