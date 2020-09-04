package basic.control;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;

public class ChartController implements Initializable{
	@FXML PieChart pieChart;
	@FXML BarChart barChart;
	@FXML AreaChart areaChart;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ObservableList<Data> list = FXCollections.observableArrayList(
				new PieChart.Data("AWT", 10),new PieChart.Data("SWING", 20),
				new PieChart.Data("SWT", 30),new PieChart.Data("JAVAFX", 35)
				);
//		list.add(new PieChart.Data("AWT", 10));		
//		list.add(new PieChart.Data("AWT", 10));		
//		list.add(new PieChart.Data("AWT", 10));		
//		list.add(new PieChart.Data("AWT", 10));		
		pieChart.setData(list);
		
		XYChart.Series<String, Integer> s1 = new XYChart.Series<>();
		s1.setData(getSeries1());
		s1.setName("남자");
		barChart.getData().add(s1);
		
		XYChart.Series<String, Integer> s2 = new XYChart.Series<>();
		s1.setData(getSeries2());
		s1.setName("여자");
		barChart.getData().add(s2);
		

		XYChart.Series<String, Integer> s3 = new XYChart.Series<>();
		s3.setData(getSeries3());
		s3.setName("온도");
		areaChart.getData().add(s3);
		
		
//		barChart.setData();
		
	
	}
	public ObservableList<XYChart.Data<String, Integer>> getSeries1() {
		ObservableList<XYChart.Data<String, Integer>>  list=FXCollections.observableArrayList();
		list.add(new XYChart.Data<String, Integer>("2015",70));
		list.add(new XYChart.Data<String, Integer>("2019",50));
		list.add(new XYChart.Data<String, Integer>("2020",20));
		list.add(new XYChart.Data<String, Integer>("2011",30));
		return list;
	}
	
	public ObservableList<XYChart.Data<String, Integer>> getSeries2() {
		ObservableList<XYChart.Data<String, Integer>>  list=FXCollections.observableArrayList();
		list.add(new XYChart.Data<String, Integer>("2015",40));
		list.add(new XYChart.Data<String, Integer>("2019",10));
		list.add(new XYChart.Data<String, Integer>("2020",90));
		list.add(new XYChart.Data<String, Integer>("2011",120));
		return list;
	}
	
	public ObservableList<XYChart.Data<String, Integer>> getSeries3() {
		Connection conn = ConnectionDB.getDB();
		ObservableList<XYChart.Data<String, Integer>>  list=FXCollections.observableArrayList();
		String sql = "Select * from receipt";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new XYChart.Data<>(rs.getString("receipt_month"),rs.getInt("receipt_qty")));
			}
			int r = pstmt.executeUpdate();
			System.out.println(r +"건 입력됨.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

}
