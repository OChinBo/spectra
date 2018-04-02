package cn.edu.pku.controllers;

import cn.edu.pku.dao.SpectrumDao;
import cn.edu.pku.ui.AboutDialog;
import cn.edu.pku.controllers.TabController;
import cn.edu.pku.util.PropertiesUtils;

import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;

public class IndexController {

	@FXML
	private javafx.scene.control.TabPane tabPane;
	private SpectrumDao spectrumDao = new SpectrumDao();
	private File sourceFile;
	private File outputFile;
	private String defaultDirectory = ".";
	private Stage stage;

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
			XYChart.Series<Number, Number> series = spectrumDao.readFileIntoSeries(sourceFile);
			TabController tab = new TabController(series);
			tab.setText(sourceFile.getName());
			tabPane.getTabs().add(tab);
			spectrumDao.setTabPane(tabPane);  // set THE tabpane to dao package

		} // end of if

	} // end of openFileAction()

	@FXML
	private void saveAction(ActionEvent ae) {
		spectrumDao.writeFile(sourceFile);
	}

	@FXML
	private void saveAsAction(ActionEvent ae) {

		// 得到當前視窗
		Window window = ((Node) ae.getTarget()).getScene().getWindow();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(defaultDirectory));

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
			spectrumDao.writeFile(outputFile);
		}

	}

    @FXML
    void about(ActionEvent event) {
        AboutDialog aboutDialog = new AboutDialog(stage);
        aboutDialog.showAbout();
    }

}
