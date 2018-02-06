package controller;

import model.*;
import view.*;

import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {


	@Override
	public void start(Stage primaryStage) {
		try {
			// Load root scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Index.fxml"));
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());

			// Set icon and title
			primaryStage.getIcons().add(new Image("file:image/chicken.png"));
			primaryStage.setTitle("PKU Raman Spectrum");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}




}
