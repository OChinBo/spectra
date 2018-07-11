package cn.edu.pku.ui;

import java.util.ArrayList;

import cn.edu.pku.controllers.TabController;
import cn.edu.pku.service.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FilterSelector {

	@FXML
	private javafx.scene.control.TabPane tabPane;

	final Stage stage = new Stage();
	// final Button closeButton = new Button();

	public FilterSelector(Stage primaryStage) {
		prepareStage(primaryStage);
		stage.setScene(prepareScene());
	}

	public void show() {
		stage.show();
	}

	// Not need for now
	private void prepareStage(Stage primaryStage) {
		// stage.initStyle(StageStyle.TRANSPARENT);
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
			addFilter(new DifferenceFilter(getCurrentTab().getSeries()));
			stage.close();
		});


		Button smoothSMA = new Button("Smoothing_SMA");
		smoothSMA.setOnAction((e) -> {
			addFilter(new SmoothingSMAFilter(getCurrentTab().getSeries()));
			stage.close();
		});


		// Define button position
		stageBox.add(diff, 0,0);
		stageBox.add(smoothSMA, 0,1);

		Scene scene = new Scene(stageBox, 400, 150);

		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

		return scene;
	}


	/*
	 * @FXML Add button listener : add filter *** Make sure connect to the
	 * correct tab(linechart) 1. Show all filters we have already implemented 2.
	 * Choose what filter that user wants to add 3. Add THE CHOOSEN FILTER into
	 * filter list, its type and its default parameters
	 *
	 * DETAILS 1. 2. 3. Get filter list from THE tabcontroller Check the list is
	 * empty or not if the list contains items, then list them and show the
	 * status(type of filters and parameters and values of filters) if not do
	 * nothing then add the filter type and the parameters into the filter list
	 * check the filter list to call each filter to calculate
	 */
	public void addFilter(BasicFilter filter) {
		getCurrentTab().getFilterList().add(filter);
		// test
		ArrayList<BasicFilter> tp = getCurrentTab().getFilterList();
		for (BasicFilter bf : tp) {
			System.out.println(bf.getClass());
		}
	}

	/*
	 * @FXML Delete button listener : delete filter 1. Choose the filter which
	 * the user what to delete 2. re-calculate output, which is restart the
	 * filters of the linechart by order
	 *
	 * DETAILS 1. Choose the right filter to deleter from the filter list 2.
	 * Re-calculate and order by filter list
	 */
	public void removeFilter(int index) {
		getCurrentTab().getFilterList().remove(index);
	}

	TabController getCurrentTab() {
		//System.out.println("Tab in FilterSelector:" + (TabController) this.tabPane.getSelectionModel().getSelectedItem());
		return (TabController) tabPane.getSelectionModel().getSelectedItem();
	}

	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

}
