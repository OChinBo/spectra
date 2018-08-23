package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import cn.edu.pku.entity.tableViewContentEntity;
import cn.edu.pku.service.*;
import cn.edu.pku.ui.FilterSelector;
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
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
	private VBox filterBox;

	@FXML
	private Button buttonZoom;

	@FXML
	private Button buttonReset;

	@FXML
	private TableView<tableViewContentEntity> tableView = new TableView<>();

	@FXML
	private TableColumn<tableViewContentEntity, String> xColumn;

	@FXML
	private TableColumn<tableViewContentEntity, String> yColumn;

	private Series<Number, Number> series;
	private Double xAxisLowerBound;
//	private Double xAxisUpperBound;
	private Double yAxisLowerBound;
//	private Double yAxisUpperBound;

	private Stage stage;

	final Rectangle zoomRect = new Rectangle();

	private ArrayList<BasicFilter> filterList;

	private int filterID = 0;

	// Constructor
	public TabController(XYChart.Series<Number, Number> series, TabPane tabPane) {

		this.series = series;
		this.filterList = new ArrayList<BasicFilter>();
		this.tabPane = tabPane;

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

	/**
	 * Save File's data is from LineChart
	 * TableView's data is from LineChart
	 * */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Set Add Filter button
		Button addButton = new Button("Add Filter");

		addButton.setMaxWidth(Double.MAX_VALUE);

		addButton.setOnAction((e) -> {
			FilterSelector fs = new FilterSelector(stage);
			fs.setTabController(this);
			fs.show();
		});


		filterBox.getChildren().add(addButton);
		//filterBox.getChildren().add(new Separator());


		// final NumberAxis xAxisx = new NumberAxis(0,1000,0.5);
		// final NumberAxis yAxisy = new NumberAxis(0,1000,0.5);
		// lineChart = new LineChart<>(xAxisx, yAxisy);

		// Setup lineChart
		//lineChart.getData().add(series);
		lineChart.setLegendVisible(false);
		lineChart.autosize();

		// xAxis.setUpperBound(3000);
		// xAxis.setLowerBound(0);
		// yAxis.setUpperBound(2000);
		// yAxis.setLowerBound(-2000);



		// Complete, 03/26
		// Fill data to tableview
		// Get data from specific linechart
		xColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity, String>("x"));
		yColumn.setCellValueFactory(new PropertyValueFactory<tableViewContentEntity, String>("y"));
		tableView.setItems(generateTableContent(lineChart));


		handleFilters();



		// 04/23 zoom test
		zoomRect.setManaged(false);
		zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
		anchorPane.getChildren().add(zoomRect);

		setUpZooming(zoomRect, lineChart);

		buttonZoom.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doZoom(zoomRect, lineChart);
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
				handleFilters();
			}
		});

		final BooleanBinding disableControls = zoomRect.widthProperty().lessThan(5)
				.or(zoomRect.heightProperty().lessThan(5));
		buttonZoom.disableProperty().bind(disableControls);


	} // end of initialize()

	@SuppressWarnings("unused")
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
	 * 1. Get data from THE linechart
	 * 2. return THE data
	 * @return ObservableList<tableViewContent>
	 */
	public ObservableList<tableViewContentEntity> generateTableContent(LineChart<Number, Number> sourceLineChart) {
		ObservableList<tableViewContentEntity> tvdata = FXCollections.observableArrayList();
		for (int i = 0; i < sourceLineChart.getData().size(); i++) {
			XYChart.Series<Number, Number> series = sourceLineChart.getData().get(i);
			for (int j = 0; j < series.getData().size(); j++) {
				tableViewContentEntity tmp = new tableViewContentEntity(
						series.getData().get(j).getXValue().toString(),
						series.getData().get(j).getYValue().toString()
				);
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
					 //doZoom(rect, lineChart);
					 //zoomWindow();
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


	/**
	 * clone series, in order not to change the original series
	 * @param source
	 * @return XYChart.Series<Number, Number>
	 */
	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {
		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>();
		destination.setName("clone spectra Data");
		for (int i = 0; i < source.getData().size(); i++){
			destination.getData().add(new XYChart.Data<>(source.getData().get(i).getXValue(), source.getData().get(i).getYValue()));
		}
		return destination;
	}


	/**
	 * @FXML Add button listener : add filter *** Make sure connect to the correct tab(linechart)
	 * 1. Show all filters we have already implemented
	 * 2. Choose what filter that user wants to add
	 * 3. Add THE CHOOSEN FILTER into filter list, its type and its default parameters
	 *
	 * DETAILS 1. 2. 3. Get filter list from THE tabcontroller Check the list is
	 * empty or not if the list contains items, then list them and show the
	 * status(type of filters and parameters and values of filters) if not do
	 * nothing then add the filter type and the parameters into the filter list
	 * check the filter list to call each filter to calculate
	 */
	public void addFilter(BasicFilter filter) {

		String strID = String.valueOf(filterID);
		String filterClassName = filter.getClass().getSimpleName();

		filter.setId(strID);
		filterList.add(filter);

		// Set filter UI
		VBox vb = new VBox();
		Separator sep = new Separator();
		Label title = new Label(filterClassName);
		Button close = new Button();
		Pane spacer = new Pane();
		HBox titleBar = new HBox();

		close.setOnAction((e) -> {
			removeFilter(Integer.parseInt(vb.getId()));
		});

		HBox.setHgrow(spacer, Priority.ALWAYS);

		close.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);

		close.getStyleClass().add("close");

		vb.setId(strID);

		vb.setSpacing(5);

		titleBar.getChildren().addAll(title, spacer, close);

		titleBar.setPadding(new Insets(0,5,0,5));

		// Set parameter controls
		switch(filterClassName) {
			case "DifferenceFilter":
				vb.getChildren().addAll(titleBar, sep);
				break;

			// SmoothingSMAFilter only has one spinner to control the parameter
			case "SmoothingSMAFilter":
				Spinner<Integer> spinner = new Spinner<Integer>();
				Integer max = 100;
				Integer min = 1;
				Integer init = 5;

				spinner.getEditor().setOnKeyPressed(event -> {
			        switch (event.getCode()) {
			            case UP:
			                spinner.increment(1);
			                break;
			            case DOWN:
			                spinner.decrement(1);
			                break;
						default:
							break;
			        }
			    });
			    spinner.setOnScroll(e -> {
			    	// Prevent overflow
			    	if(e.getDeltaY()>0 && spinner.getValue()<max){
			    		spinner.increment(1);
			    	} else if(e.getDeltaY()<0 && spinner.getValue()>min){
			    		spinner.decrement(1);
			    	}
//		    		spinner.increment((int) (e.getDeltaY() / e.getMultiplierY()));
			    });

			    // Setup Factory
			    // Min:1, Max:100, Init:5
			    SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, init);
			    factory.valueProperty().addListener((observable, oldValue, newValue) -> {
			    	int index = findFilterIndexById(vb.getId());
					((SmoothingSMAFilter)filterList.get(index)).setPoints(newValue);
					handleFilters();
				});
			    spinner.setValueFactory(factory);
			    spinner.setEditable(true);

			    // This part maybe is useless
			    TextFormatter<Integer> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
			    spinner.getEditor().setTextFormatter(formatter);
			    factory.valueProperty().bindBidirectional(formatter.valueProperty());

				vb.getChildren().addAll(titleBar, spinner, sep);
				break;
			default:
				// do nothing
		}

		filterBox.getChildren().add(vb);
		filterID++;
		handleFilters();
	}


	/**
	 * @FXML Delete button listener : delete filter
	 * 1. Choose the filter which the user what to delete
	 * 2. re-calculate output, which is restart the filters of the linechart by order
	 *
	 * DETAILS
	 * 1. Choose the right filter to deleter from the filter list
	 * 2. Re-calculate and order by filter list
	 */
	public void removeFilter(int id) {

		int index = findFilterIndexById(String.valueOf(id));

		filterList.remove(index);
		filterBox.getChildren().remove(index+1); // index+1 skip add button
//		filterBox.getChildren().remove(index+2); // index+2 skip add button and Separator
		handleFilters();
	}

	private int findFilterIndexById(String strID){
		for(int i=0; i < filterList.size(); i++){
			if(filterList.get(i).getId().equals(strID)){
				return i;
			}
		}
		// If Id not found, it would cause IndexOutOfBoundException
		return -1;
	}

	private void refreshChartTable(Series<Number, Number> s){
		lineChart.getData().clear();
		lineChart.getData().add(s);

		tableView.setItems(generateTableContent(lineChart));
	}

	private void handleFilters(){
		Series<Number, Number> tmp = cloneSeries(series);
		if(filterList.size() != 0){
			for(BasicFilter bf : filterList) {
				tmp = bf.launch(tmp);
			}
		}
		refreshChartTable(tmp);
	}

	public ArrayList<BasicFilter> getFilterList(){
		return this.filterList;
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
