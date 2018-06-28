package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.lang.Object;

import cn.edu.pku.entity.tableViewContentEntity;
import cn.edu.pku.service.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TabController extends Tab implements Initializable {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private TabPane tabPane;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private HBox hbox;

	@FXML
	private Button buttonZoom;

	@FXML
	private Button buttonReset;

	@FXML
	private TableView<tableViewContentEntity> tableView = new TableView<>();

	@FXML
	private TableColumn xColumn;

	@FXML
	private TableColumn yColumn;

	private Series<Number, Number> series;
	private Double xAxisLowerBound;
	private Double xAxisUpperBound;
	private Double yAxisLowerBound;
	private Double yAxisUpperBound;

	final Rectangle zoomRect = new Rectangle();

	public ArrayList<BasicFilter> filterList;

	// Constructor
	public TabController(XYChart.Series<Number, Number> series) {

		this.series = series;

		this.filterList = new ArrayList<BasicFilter>();

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

		// final NumberAxis xAxisx = new NumberAxis(0,1000,0.5);
		// final NumberAxis yAxisy = new NumberAxis(0,1000,0.5);
		// lineChart = new LineChart<>(xAxisx, yAxisy);
		lineChart.getData().add(series);
		lineChart.setLegendVisible(false);

		// ChartZoomManager tmp;

		NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
		NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
		// xAxis.setUpperBound(3000);
		// xAxis.setLowerBound(0);
		// yAxis.setUpperBound(2000);
		// yAxis.setLowerBound(-2000);
		xAxisLowerBound = xAxis.getLowerBound();
		xAxisUpperBound = xAxis.getUpperBound();
		yAxisLowerBound = yAxis.getLowerBound();
		yAxisUpperBound = yAxis.getUpperBound();

		System.out.println("xAxisLowerBound:" + xAxisLowerBound + "xAxisUpperBound:" + xAxisUpperBound);
		System.out.println("yAxisLowerBound:" + yAxisLowerBound + "yAxisUpperBound:" + yAxisUpperBound);

		// Complete, 03/26
		// Fill data to tableview
		// Get data from specific linechart
		xColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity, String>("x"));
		yColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity, String>("y"));
		tableView.setItems(getTableContent());

		// Set filter
		// Store original data
		//BasicFilter filter = new BasicFilter(series) ;

		DifferenceFilter filter = new DifferenceFilter(series);
		filter.fillData();
		// filter.PrintInput();
		Series<Number, Number> org = this.series ;

		// smoothing moving average
//		this.series = filter.smoothing_MovingAvg(100);
//		lineChart.getData().clear();
//		lineChart.getData().add(series);
//		filter.printOutput();
//		tableView.setItems(getTableContent());

		// different
		this.series = filter.launch();
		lineChart.getData().clear();
		lineChart.getData().add(series);
		filter.printOutput();
		tableView.setItems(getTableContent());

		// 04/23 zoom test
		zoomRect.setManaged(false);
		zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
		anchorPane.getChildren().add(zoomRect);

		setUpZooming(zoomRect, lineChart);
		// 04/23 zoom test

		buttonZoom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doZoom(zoomRect, lineChart);
				// zoomWindow() ;
			}
		});

		buttonReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// final NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
				// xAxis.setLowerBound(xAxisLowerBound);
				// xAxis.setUpperBound(xAxisUpperBound);
				// final NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
				// yAxis.setLowerBound(yAxisLowerBound);
				// yAxis.setUpperBound(yAxisUpperBound);

				zoomRect.setWidth(0);
				zoomRect.setHeight(0);
			}
		});

		final BooleanBinding disableControls = zoomRect.widthProperty().lessThan(5)
				.or(zoomRect.heightProperty().lessThan(5));
		buttonZoom.disableProperty().bind(disableControls);

	} // end of initialize()

	private LineChart<Number, Number> createChart() {
		final NumberAxis xAxis = createAxis();
		final NumberAxis yAxis = createAxis();
		// final LineChart<Number, Number> chart = new LineChart<>(xAxis,
		// yAxis);
		// chart.setAnimated(false);
		// chart.setCreateSymbols(false);
		LineChart<Number, Number> chart = new LineChart<>(new NumberAxis(), new NumberAxis());
		chart.getData().add(this.series);
		return chart;
	}

	private NumberAxis createAxis() {
		final NumberAxis xAxis = new NumberAxis();
		// final NumberAxis xAxis = new NumberAxis(0,1000,0.5);
		// xAxis.setAutoRanging(false);
		// xAxis.setLowerBound(-1000);
		// xAxis.setUpperBound(2000);
		return xAxis;
	}

	/**
	 * 1. Get data from THE linechart 2. return THE data
	 *
	 * @return ObservableList<tableViewContent>
	 */
	public ObservableList<tableViewContentEntity> getTableContent() {

		ObservableList<tableViewContentEntity> tvdata = FXCollections.observableArrayList();

		// get data from series linechart
		for (int i = 0; i < lineChart.getData().size(); i++) {

			XYChart.Series<Number, Number> series = lineChart.getData().get(i);

			for (int j = 0; j < series.getData().size(); j++) {

				// add data into tableViewContent
				tableViewContentEntity tmp = new tableViewContentEntity(series.getData().get(j).getXValue().toString(),
						series.getData().get(j).getYValue().toString());
				// Add tableViewContent into ObservableList<>
				tvdata.add(tmp);

			}

		}

		return tvdata;
	}

	/**
	 * 04/23 zoom test
	 *
	 * @param rect
	 * @param zoomingNode
	 */
	private void setUpZooming(final Rectangle rect, final Node zoomingNode) {

		final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();

		// Reset when right-button clicked
		zoomingNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
				if (button == MouseButton.SECONDARY) {
					final NumberAxis xAxis = (NumberAxis) getLineChart().getXAxis();
					xAxis.setLowerBound(0);
					xAxis.setUpperBound(1000);
					final NumberAxis yAxis = (NumberAxis) getLineChart().getYAxis();
					yAxis.setLowerBound(0);
					yAxis.setUpperBound(1000);

					zoomRect.setWidth(0);
					zoomRect.setHeight(0);

				}
			}
		});

		zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
				if(button==MouseButton.PRIMARY){
					mouseAnchor.set(new Point2D(event.getX(), event.getY()));
					rect.setWidth(0);
					rect.setHeight(0);
				}
			}
		});

		zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();
				if(button==MouseButton.PRIMARY){
					double x = event.getX();
					double y = event.getY();
					rect.setX(Math.min(x, mouseAnchor.get().getX()));
					rect.setY(Math.min(y, mouseAnchor.get().getY()));
					rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
					rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
				}
			}
		});

		// doZoom when left-button released
		zoomingNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MouseButton button = event.getButton();

				if(button==MouseButton.PRIMARY){
					// doZoom(rect, linechart);
					// zoomWindow();
				}
			}

		});
	}

	private void doZoom(Rectangle zoomRect, LineChart<Number, Number> chart) {
		Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
		Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(),
				zoomRect.getY() + zoomRect.getHeight());
		final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
		Point2D yAxisInScene = yAxis.localToScene(xAxisLowerBound, yAxisLowerBound);
		final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
		Point2D xAxisInScene = xAxis.localToScene(xAxisLowerBound, yAxisLowerBound);
		double xOffset = zoomTopLeft.getX() - yAxisInScene.getX();
		double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
		double xAxisScale = xAxis.getScale();
		double yAxisScale = yAxis.getScale();
		xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
		xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
		yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
		yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
		System.out.println(xAxis.getLowerBound() + " " + xAxis.getUpperBound());
		System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
		zoomRect.setWidth(0);
		zoomRect.setHeight(0);
	}

	private void zoomWindow() {
		Stage zoomStage = new Stage();
		final LineChart<Number, Number> chart = null;
		chart.getData().add(cloneSeries(series));

		final StackPane chartContainer = new StackPane();
		chartContainer.getChildren().add(chart);

		final Rectangle zoomRect = new Rectangle();
		zoomRect.setManaged(false);
		zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
		chartContainer.getChildren().add(zoomRect);

		setUpZooming(zoomRect, chart);

		final HBox controls = new HBox(10);
		controls.setPadding(new Insets(10));
		controls.setAlignment(Pos.CENTER);

		final Button zoomButton = new Button("Zoom");
		final Button resetButton = new Button("Reset");
		zoomButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doZoom(zoomRect, chart);
			}
		});
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
				xAxis.setLowerBound(0);
				xAxis.setUpperBound(1000);
				final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
				yAxis.setLowerBound(0);
				yAxis.setUpperBound(1000);

				zoomRect.setWidth(0);
				zoomRect.setHeight(0);
			}
		});
		final BooleanBinding disableControls = zoomRect.widthProperty().lessThan(5)
				.or(zoomRect.heightProperty().lessThan(5));
		zoomButton.disableProperty().bind(disableControls);
		controls.getChildren().addAll(zoomButton, resetButton);

		final BorderPane root = new BorderPane();
		root.setCenter(chartContainer);
		root.setBottom(controls);

		final Scene scene = new Scene(root, 600, 400);
		zoomStage.setScene(scene);
		zoomStage.show();
	}

	/**
	 * clone series, in order not to change the original series
	 *
	 * @param source
	 * @return XYChart.Series<Number, Number>
	 */
	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {

		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>();

		for (int i = 0; i < source.getData().size(); i++)
			destination.getData()
					.add(new XYChart.Data<>(source.getData().get(i).getXValue(), source.getData().get(i).getYValue()));

		return destination;

	}

	public ArrayList<BasicFilter> getFilterList(){
		return this.filterList;
	}

	public void addFilter(BasicFilter filter){
		this.filterList.add(filter);
	}

	public void removeFilter(int index){
		this.filterList.remove(index);
	}

	public LineChart<Number, Number> getLineChart() {
		return lineChart;
	}

	public void setLineChart(LineChart<Number, Number> lineChart) {
		this.lineChart = lineChart;
	}

	public TableView<tableViewContentEntity> getTableView() {
		return tableView;
	}

	public void setTableView(TableView<tableViewContentEntity> tableView) {
		this.tableView = tableView;
	}

	public Series<Number, Number> getSeries(){
		return series;
	}

}
