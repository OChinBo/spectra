package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;

public class IndexController {


	@FXML
	private javafx.scene.control.MenuItem closeButton;


	@FXML
	private void closeButtonAction(){

	    Platform.exit();
	}




}
