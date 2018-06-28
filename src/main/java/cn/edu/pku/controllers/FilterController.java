package cn.edu.pku.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.pku.ui.FilterSelector;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FilterController {

	private static Stage stage;

	FilterController(){
		refresh();
	}

	public void addFilter(){

	}

	public void refresh(){

		TabController tab = IndexController.getCurrentTab();

		System.out.println("filterpane:"+IndexController.filterPane.getChildren());

		// ¨«³Xfilter°}¦C
		//System.out.print(filterPane.toString());

		/*
		if(!tab.filterList.isEmpty()){
			tab.filterList.forEach(item->{
				System.out.println(item.getClass());
			});
		}*/

		Button addButton = new Button();
		addButton.setOnAction((e) -> {
			FilterSelector fs = new FilterSelector(stage);
			fs.show();
		});
		// ¥[GUI
		AnchorPane.setTopAnchor(addButton, 10.0);
		IndexController.filterPane.getChildren().add(addButton);

	}



}
