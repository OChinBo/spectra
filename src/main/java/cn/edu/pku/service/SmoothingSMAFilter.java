package cn.edu.pku.service;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class SmoothingSMAFilter extends BasicFilter {

	private int points = 5;

	public SmoothingSMAFilter() {
		super();
	}


	public void setPoints(int p){
		System.out.println("set p:" + p);
		this.points = p;
	}

	@Override
	public Series<Number, Number> launch(Series<Number, Number> series){

		Series<Number, Number> outputseries = new XYChart.Series<Number, Number>();
		outputseries.setName("SmoothingSMA series");
		Double x = 0.0 ;
		Double y = 0.0 ;

		this.series = series;
		fillData();

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



}
