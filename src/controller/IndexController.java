package controller;

import java.io.File;

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
import javafx.stage.FileChooser;

public class IndexController {

	private File sourceFile;

	//@FXML
	//private javafx.scene.control.MenuItem closeButton;


	@FXML
	private void closeWindowsAction(){
		//System.exit(0);
	    Platform.exit();
	}


	/***** MenuBar Buttons *****/
	@FXML
	private void openFileAction(ActionEvent ae){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");

		sourceFile = fileChooser.showOpenDialog(null); // Assign selected file to local parameter.
		//System.out.print(sourceFile);
	}

	@FXML
	private void saveAsAction(ActionEvent ae){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As");

		FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"); // Set format of filter.
		FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(csvFilter);
		fileChooser.getExtensionFilters().add(txtFilter);

		sourceFile = fileChooser.showSaveDialog(null);
		//System.out.print(sourceFile);
	}
}
