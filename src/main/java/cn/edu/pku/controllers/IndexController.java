package cn.edu.pku.controllers;

import cn.edu.pku.dao.FileDao;
import cn.edu.pku.ui.AboutDialog;
import cn.edu.pku.controllers.TabController;
import cn.edu.pku.util.OpenFileUtils;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class IndexController {

	@FXML
	private javafx.scene.layout.VBox root;

	@FXML
	private javafx.scene.control.TabPane tabPane;

	private FileDao fileDao;
	private File sourceFile;
	private String defaultDirectory = ".";
	private Stage stage;

	@FXML
	private void closeWindowsAction() {
		Platform.exit();
	}

	@FXML
	private void importAction(ActionEvent ae) {

		Window window = (Stage)root.getScene().getWindow();

		sourceFile = OpenFileUtils.open(window, defaultDirectory);

		if(sourceFile != null){

			// Update the latest directory as defaultDirectory
			defaultDirectory = sourceFile.toString() + "/..";

			// Create TabController and then add Tab
			fileDao = new FileDao(sourceFile);
			XYChart.Series<Number, Number> series = fileDao.read();
			TabController tab = new TabController(series);
			tab.setText(sourceFile.getName());
			tabPane.getTabs().add(tab);
			fileDao.setTabPane(tabPane);  // set THE tabpane to dao package

		} // end of if

	}

	@FXML
	private void saveAction(ActionEvent ae) {
		fileDao.write(sourceFile);
	}

	@FXML
	private void saveAsAction(ActionEvent ae) {

		TabController tab = (TabController) tabPane.getSelectionModel().getSelectedItem();
		LineChart<Number, Number> linechart = tab.getLineChart();

		SaveController oSaveController = new SaveController(linechart.getData().get(0)) ;
		oSaveController.primaryProcess();

	}

    @FXML
    void about(ActionEvent event) {
        AboutDialog aboutDialog = new AboutDialog(stage);
        aboutDialog.showAbout();
    }

//    void print( LineChart<Number, Number> linechart ) {
//
//    	for (int i = 0; i < linechart.getData().size(); i++) {
//			XYChart.Series<Number, Number> series = linechart.getData().get(i);
//			for (int j = 0; j < series.getData().size(); j++) {
//				 System.out.println( j+1 + ". " + series.getData().get(j).getXValue() +"," + series.getData().get(j).getYValue());
//			}
//		}
//
//    }

}
