package cn.edu.pku.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaveController extends SplitPane implements Initializable {

	@FXML
	private LineChart<Number, Number> lineChartRange ;
	@FXML
	private LineChart<Number, Number> lineChartPreview ;
	@FXML
	private Slider SliderBegin ;
	@FXML
	private Slider SliderEnd ;
	@FXML
	private Spinner<Integer> SpinnerBegin ;
	@FXML
	private Spinner<Integer> SpinnerEnd ;

	private Series<Number, Number> series;
	private int size ;
	final Stage stage = new Stage() ;

	// Constructor
	public SaveController( XYChart.Series<Number, Number> series ) {

		this.series = series ;
		this.size = this.series.getData().size();
		// Double max = Double.parseDouble(this.series.getData().get(this.series.getData().size()-1).getXValue().toString());
		// Double min = Double.parseDouble(this.series.getData().get(0).getXValue().toString());
		// xAxisSize = max.intValue() - min.intValue() ;
		// print(this.series) ;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpectrumRangeSelector.fxml"));
		loader.setController(this);
		loader.setRoot(this);

		try {

			Parent root = loader.load();
			stage.setTitle("Save");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void print( XYChart.Series<Number, Number> series ) {

		for (int j = 0; j < series.getData().size(); j++) {
			System.out.println( j+1 + ". " + series.getData().get(j).getXValue() +"," + series.getData().get(j).getYValue());
		}

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		lineChartRange.getData().add(cloneSeries(this.series)) ;
		lineChartPreview.getData().add(cloneSeries(this.series)) ;

		// SpinnerBegin.setEditable(true);
		// SpinnerEnd.setEditable(true);

		// SpinnerValueFactory<Integer> factoryBegin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, this.size-1, 0);
		SpinnerValueFactory<Integer> factoryBegin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
		SpinnerBegin.setValueFactory(factoryBegin);

		// SpinnerValueFactory<Integer> factoryEnd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, this.size-1, this.size-1);
		SpinnerValueFactory<Integer> factoryEnd = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 100);
		SpinnerEnd.setValueFactory(factoryEnd);

	}

	public void primaryProcess() {

		SpinnerBegin.getEditor().setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case UP:
                	SpinnerBegin.increment(1);
                    break;
                case DOWN:
                	SpinnerBegin.decrement(1);
                    break;
            }

        });

		SpinnerBegin.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if( !"".equals(newValue) ) {
				SliderBegin.setValue(Integer.parseInt(newValue));
			}

	    });

		SpinnerEnd.getEditor().setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case UP:
                	SpinnerEnd.increment(1);
                    break;
                case DOWN:
                	SpinnerEnd.decrement(1);
                    break;
            }

        });

		SpinnerEnd.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

			if ( !"".equals(newValue) ) {
				SliderEnd.setValue(Integer.parseInt(newValue));
			}

	    });

		// Listen for Slider Begin value changes
		SliderBegin.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {

		    	Double begin = SliderBegin.getValue() ;
		    	SpinnerBegin.getValueFactory().setValue(begin.intValue());

		    	begin = SliderBegin.getValue()/100 * size ;
				Double end   = SliderEnd.getValue()/100 * size -1 ;
				setPreview(begin, end) ;
		    }
		});

		// Listen for Slider End value changes
		SliderEnd.valueProperty().addListener(new ChangeListener<Number>() {
		    @Override
		    public void changed(ObservableValue<? extends Number> observable,
		            Number oldValue, Number newValue) {

		    	Double end = SliderEnd.getValue() ;
		    	SpinnerEnd.getValueFactory().setValue(end.intValue());

		    	Double begin = SliderBegin.getValue()/100 * size ;
				end   = SliderEnd.getValue()/100 * size -1 ;
				setPreview(begin, end) ;
		    }
		});

	}

	private void setPreview( Double dbegin, Double dend ) {

		lineChartPreview.getData().clear();

		int begin = dbegin.intValue() ;
		int end   = dend.intValue() ;

		if ( begin <= end ) {

			XYChart.Series<Number, Number> tmpseries = new XYChart.Series<Number, Number>() ;

			for ( int i = begin ; i < end ; i++ ) {

				tmpseries.getData().add( new XYChart.Data<>(
											this.series.getData().get(i).getXValue(),
											this.series.getData().get(i).getYValue()) );

			}

			lineChartPreview.getData().add(tmpseries) ;

		}

		else {
			; // DO NOTHING
		}

	}

	private XYChart.Series<Number, Number> cloneSeries(XYChart.Series<Number, Number> source) {

		XYChart.Series<Number, Number> destination = new XYChart.Series<Number, Number>() ;
		destination.setName("Spetra");

		for( int i = 0 ; i < source.getData().size() ; i++ )
			destination.getData().add(new XYChart.Data<>(
											source.getData().get(i).getXValue(),
											source.getData().get(i).getYValue())) ;

		return destination ;

	}

}
