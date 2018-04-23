package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cn.edu.pku.entity.tableViewContentEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TabController extends Tab implements Initializable {

	@FXML
	private AnchorPane anchorPane ;

	@FXML
	private TabPane tabPane ;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private TableView<tableViewContentEntity> tableView = new TableView<>();

	@FXML
	private TableColumn xColumn;

	@FXML
	private TableColumn yColumn;

	private Series<Number, Number> series;

	// Constructor
    public TabController( XYChart.Series<Number, Number> series ) {

    	this.series = series;

    	// Set root, Tab.fxml is fx:root
		FXMLLoader tabLoader = new FXMLLoader(getClass().getResource("/view/Tab.fxml"));
		this.getStyleClass().add(getClass().getResource("/css/tab.css").toExternalForm());
        tabLoader.setRoot(this);
        tabLoader.setController(this);

		try {
			tabLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		lineChart.getData().add(series);
		lineChart.setLegendVisible(false);

		// Complete, 03/26
		// Fill data to tableview
		// Get data from specific linechart
		xColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity,String>("x"));
		yColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity,String>("y"));
		tableView.setItems(getTableContent());

		// 04/23 zoom test
		final Rectangle zoomRect = new Rectangle();
		zoomRect.setManaged(false);
		zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
		anchorPane.getChildren().add(zoomRect) ;

		setUpZooming(zoomRect, lineChart);
		// 04/23 zoom test

	} // end of initialize()

	/**
	 *1. Get data from THE linechart
	 *2. return THE data
	 *@return ObservableList<tableViewContent>
	 * */
	public ObservableList<tableViewContentEntity> getTableContent() {

		ObservableList<tableViewContentEntity> tvdata = FXCollections.observableArrayList() ;

		// get data from series linechart
		for (int i = 0; i < lineChart.getData().size(); i++) {

			XYChart.Series<Number, Number> series = lineChart.getData().get(i);

			for (int j = 0; j < series.getData().size(); j++) {

				// add data into tableViewContent
				tableViewContentEntity tmp = new tableViewContentEntity(series.getData().get(j).getXValue().toString(),
						                                    series.getData().get(j).getYValue().toString()) ;
				// Add tableViewContent into ObservableList<>
				tvdata.add(tmp);

			}

		}

        return tvdata;
    }

	/**
	 * 04/23 zoom test
	 * @param rect
	 * @param zoomingNode
	 */
	private void setUpZooming(final Rectangle rect, final Node zoomingNode) {

		final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();

		zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mouseAnchor.set(new Point2D(event.getX(), event.getY()));
				rect.setWidth(0);
				rect.setHeight(0);
			}

		});

		zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				double x = event.getX();
				double y = event.getY();

				rect.setX(Math.min(x, mouseAnchor.get().getX()));
				rect.setY(Math.min(y, mouseAnchor.get().getY()));
				rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
				rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));

			}

		});
	}

	public LineChart<Number, Number> getLineChart() {
		return lineChart ;
	}

	public void setLineChart(LineChart<Number, Number> lineChart) {
		this.lineChart = lineChart ;
	}

	public TableView<tableViewContentEntity> getTableView() {
		return tableView ;
	}

	public void setTableView(TableView<tableViewContentEntity> tableView) {
		this.tableView = tableView ;
	}

}
