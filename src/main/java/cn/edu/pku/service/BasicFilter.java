package cn.edu.pku.service;

import java.util.ArrayList;

import cn.edu.pku.entity.tableViewContentEntity;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class BasicFilter {

	public Type type ;
	private Series<Number, Number> series;
	private ArrayList<tableViewContentEntity> input = null ;
	private ArrayList<tableViewContentEntity> output = null ;

	public BasicFilter(XYChart.Series<Number, Number> series){
		this.series = cloneSeries(series) ;
		input  = new ArrayList<tableViewContentEntity>() ;
		output = new ArrayList<tableViewContentEntity>() ;
	}

	public void FillData() {

		for (int j = 0; j < series.getData().size(); j++) {

			// add data into tableViewContent
			tableViewContentEntity tmp = new tableViewContentEntity(series.getData().get(j).getXValue().toString(),
					series.getData().get(j).getYValue().toString());
			// Add tableViewContent into ObservableList<>
			input.add(tmp);

		}

	}

	public Series<Number, Number> Smoothing_SMA(int points){

		Series<Number, Number> outputseries = new XYChart.Series<Number, Number>();
		Double x = 0.0 ;
		Double y = 0.0 ;

		for ( int i = 0 ; i < input.size()-points ; i++ ) {

			for ( int j = 0 ; j < points ; j++ ) {
				x = x + Double.parseDouble(input.get(i+j).getX());
				y = y + Double.parseDouble(input.get(i+j).getY());
			}

			x = x/points ;
			y = y/points ;

//			tableViewContentEntity tmp = new tableViewContentEntity(Double.toString(x), Double.toString(y)) ;

			outputseries.getData().add(new XYChart.Data<>(x,y));

//			output.add(tmp) ;

		}

		return outputseries ;

	}

	public Series<Number, Number> Difference() {

		Series<Number, Number> outputseries = new XYChart.Series<Number, Number>();
		Double x = 0.0 ;
		Double y = 0.0 ;

		for ( int i = 0 ; i < input.size()-1 ; i++ ) {

			x = x + (Double.parseDouble(input.get(i+1).getX()) - Double.parseDouble(input.get(i).getX()));
			y = y + (Double.parseDouble(input.get(i+1).getY()) - Double.parseDouble(input.get(i).getX()));

			outputseries.getData().add(new XYChart.Data<>(x,y));

		}

		return outputseries ;

	}

	public void PrintInput() {

		for ( int i = 0 ; i < input.size() ; i++ ) {

			System.out.println( i + 1 + ". X: " + input.get(i).getX() + ", Y: " + input.get(i).getY());

		}

	}

	public void PrintOutput() {

		for ( int i = 0 ; i < output.size() ; i++ ) {

			System.out.println( i + 1 + ". X: " + output.get(i).getX() + ", Y: " + output.get(i).getY());

		}

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

}
