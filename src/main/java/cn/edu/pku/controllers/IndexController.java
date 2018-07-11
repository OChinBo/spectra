package cn.edu.pku.controllers;

import cn.edu.pku.dao.FileDao;
import cn.edu.pku.service.BasicFilter;
import cn.edu.pku.ui.AboutDialog;
import cn.edu.pku.ui.FilterSelector;
import cn.edu.pku.controllers.TabController;
import cn.edu.pku.util.OpenFileUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class IndexController implements Initializable {

	@FXML
	private javafx.scene.layout.VBox root;

	@FXML
	private javafx.scene.control.TabPane tabPane;

	@FXML
	private javafx.scene.layout.VBox filterBox;

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
			TabController tab = new TabController(series,tabPane);
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

		// Get selected tab
		TabController tab = getCurrentTab();
		LineChart<Number, Number> linechart = tab.getLineChart();

		SaveController oSaveController = new SaveController(linechart.getData().get(0)) ;
		oSaveController.primaryProcess();

	}

    @FXML
    void about(ActionEvent event) {
        AboutDialog aboutDialog = new AboutDialog(stage);
        aboutDialog.showAbout();
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {






		//refreshFilterBox();

		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			refreshFilterBox();
		});
	}

	void refreshFilterBox(){
		System.out.println("RefreshFilterPane");
		getCurrentTab().refresh();
    }

    TabController getCurrentTab(){
    	System.out.println("Tab:"+ (TabController) tabPane.getSelectionModel().getSelectedItem());
    	return (TabController) tabPane.getSelectionModel().getSelectedItem();
    }



    /*
     * @FXML
     * filter parameters listener : set parameters' values
     * 1. Set parameters
     * 2. re-calculate output, which is restart the filters of the linechart by order
     *
     * DETAILS
     * 1. Choose the right filter in the filter list to set its parameters' value
     * 2. Re-calculate and order by filter list
     * */

    /*
    void print( LineChart<Number, Number> linechart ) {
    	for (int i = 0; i < linechart.getData().size(); i++) {
			XYChart.Series<Number, Number> series = linechart.getData().get(i);
			for (int j = 0; j < series.getData().size(); j++) {
				 System.out.println( j+1 + ". " + series.getData().get(j).getXValue() +"," + series.getData().get(j).getYValue());
			}
		}
    }
     */
}
