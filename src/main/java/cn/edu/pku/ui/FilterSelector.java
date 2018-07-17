package cn.edu.pku.ui;

import cn.edu.pku.controllers.TabController;
import cn.edu.pku.service.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FilterSelector {

	@FXML
	private javafx.scene.control.TabPane tabPane;
	private TabController tab;

	final Stage stage = new Stage();
	// final Button closeButton = new Button();

	public FilterSelector(Stage primaryStage) {
		prepareStage(primaryStage);
		stage.setScene(prepareScene());
	}

	public void show() {
		stage.show();
	}

	private void prepareStage(Stage primaryStage) {
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
		stage.setTitle("新增濾鏡");
	}

	private Scene prepareScene() {
		GridPane stageBox = new GridPane();
		//stageBox.setId("about");
		stageBox.setId("filterSelector");
		stageBox.setHgap(10);
		stageBox.setVgap(10);
		stageBox.setPadding(new Insets(10, 10, 10, 10));


		// Define button action
		Button diff = new Button("差分");
		diff.setOnAction((e) -> {
			tab.addFilter(new DifferenceFilter());
			stage.close();
		});

		Button smoothSMA = new Button("Smoothing_SMA");
		smoothSMA.setOnAction((e) -> {
			tab.addFilter(new SmoothingSMAFilter());
			stage.close();
		});

		// Define button position
		stageBox.add(diff, 0,0);
		stageBox.add(smoothSMA, 0,1);

		Scene scene = new Scene(stageBox, 400, 150);

		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

		return scene;
	}



	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	public void setTabController(TabController tab){
		this.tab = tab;
	}



}
