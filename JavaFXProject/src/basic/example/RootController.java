package basic.example;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import basic.control.Board;
import basic.control.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable {

	ObservableList<Student> list;
	@FXML
	TableView<Student> tableView;
	@FXML
	Button btnAdd, btnBarChart, selectbutton;

	Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		TableColumn<Student, ?> tc = tableView.getColumns().get(0);// 첫번쨰 칼럼 가져 오는거
		tc.setCellValueFactory(new PropertyValueFactory<>("id"));
		tc = tableView.getColumns().get(1);
		tc.setCellValueFactory(new PropertyValueFactory<>("name"));

		tc = tableView.getColumns().get(2);// 두번쨰 칼럼 가져 오는거
		tc.setCellValueFactory(new PropertyValueFactory<>("korean"));

		tc = tableView.getColumns().get(3);// 두번쨰 칼럼 가져 오는거
		tc.setCellValueFactory(new PropertyValueFactory<>("math"));

		tc = tableView.getColumns().get(4);// 두번쨰 칼럼 가져 오는거
		tc.setCellValueFactory(new PropertyValueFactory<>("english"));

		selectbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				list = getStulist();
				tableView.setItems(list);
			}
		});
		tableView.setItems(list);

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleBtnAddAction();
			}
		});
		btnBarChart.setOnAction(e -> handleBtnChartAction());
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (event.getClickCount() == 2) {
					handleDoubleClickAction(tableView.getSelectionModel().getSelectedItem().getId());
				}
			}
		});

	}

	public void handleDoubleClickAction(String id) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(210, 230);
		Label lKorean, lMath, lEnglish, lName;
		TextField tid, tName, tKorean, tMath, tEnglish;

		lKorean = new Label("국어");
		lKorean.setLayoutX(35);
		lKorean.setLayoutY(73);

		lMath = new Label("수학");
		lMath.setLayoutX(35);
		lMath.setLayoutY(99);

		lEnglish = new Label("영어");
		lEnglish.setLayoutX(35);
		lEnglish.setLayoutY(132);

		lName = new Label("이름");
		lName.setLayoutX(35);
		lName.setLayoutY(160);

		tid = new TextField();
		tid.setLayoutX(72);
		tid.setLayoutY(30);
		tid.setText(id);
		tid.setEditable(false);

		tKorean = new TextField();
		tKorean.setLayoutX(72);
		tKorean.setLayoutY(69);

		tMath = new TextField();
		tMath.setLayoutX(72);
		tMath.setLayoutY(95);

		tEnglish = new TextField();
		tEnglish.setLayoutX(72);
		tEnglish.setLayoutY(128);

		tName = new TextField();
		tName.setLayoutX(72);
		tName.setLayoutY(160);

		for (Student stu : list) {
			if (stu.getId().equals(id)) {
				// 값을 가져옴
				tName.setText(stu.getName());
				tKorean.setText(String.valueOf(stu.getKorean()));
				tMath.setText(String.valueOf(stu.getMath()));
				tEnglish.setText(String.valueOf(stu.getEnglish()));
			}
		}

		Button btnUpdate = new Button("수정");
		btnUpdate.setLayoutX(85);
		btnUpdate.setLayoutY(184);
		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("수정클릭");
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId().equals(id)) {
						Student student = new Student(id, tName.getText(), Integer.parseInt(tKorean.getText()),
								Integer.parseInt(tMath.getText()), Integer.parseInt(tEnglish.getText()));
						list.set(i, student);
						UpdateStulist(list.get(i));
						stage.close();
					}

				}

			}
		});

		Button btnDelete = new Button("삭제");
		btnDelete.setLayoutX(130);
		btnDelete.setLayoutY(184);
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId().equals(id)) {
						Student student = new Student(id, tName.getText(), Integer.parseInt(tKorean.getText()),
								Integer.parseInt(tMath.getText()), Integer.parseInt(tEnglish.getText()));
						list.set(i, student);
						DeleteStulist(list.get(i));
						tableView.setItems(getStulist());
						stage.close();
					}

				}

			}
		});

		ap.getChildren().addAll(tid, tKorean, tMath, tEnglish, lKorean, lMath, lEnglish, btnUpdate, btnDelete);
		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.show();

	}

	public ObservableList<Student> getStulist() {
		Connection conn = ConnectionDB.getDB();
		String sql = "select * from student";
		ObservableList<Student> list2 = FXCollections.observableArrayList();

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Student stu = new Student(rs.getString("id"), rs.getString("name"), rs.getInt("korean"),
						rs.getInt("math"), rs.getInt("engilsh"));
				list2.add(stu);

			}
			;

			return list2;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list2;
	}

	public void handleBtnChartAction() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
		tableView.setItems(getStulist());
		try {
			Parent chart = FXMLLoader.load(getClass().getResource("BarChart.fxml"));
			Scene scene = new Scene(chart);
			stage.setScene(scene);
			stage.show();
			// Chart 가지고 와서 시리즈 추가
			// 컨트롤러로 연결이 안되어 있을때는 룩업 써서 가져온다!
			Button btnClose = (Button) chart.lookup("#btnClose");
			btnClose.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					stage.close();
				}

			});
			BarChart barChart = (BarChart) chart.lookup("#barChart");
			XYChart.Series<String, Integer> seriesK = new XYChart.Series<>();
			seriesK.setName("국어");
			ObservableList<XYChart.Data<String, Integer>> koreanList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				koreanList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getKorean()));
			}
			seriesK.setData(koreanList);
			barChart.getData().add(seriesK);

			XYChart.Series<String, Integer> seriesM = new XYChart.Series<>();
			seriesM.setName("수학");
			ObservableList<XYChart.Data<String, Integer>> mathList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				mathList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getMath()));
			}
			seriesM.setData(mathList);
			barChart.getData().add(seriesM);

			XYChart.Series<String, Integer> seriesE = new XYChart.Series<>();
			seriesE.setName("영어");
			ObservableList<XYChart.Data<String, Integer>> engList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				engList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getMath()));
			}
			seriesE.setData(engList);
			barChart.getData().add(seriesE);

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void handleBtnAddAction() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAdd.getScene().getWindow());
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("AddForm.fxml"));
			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();
			Button btnFormAdd = (Button) parent.lookup("#btnFormAdd");
			btnFormAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					TextField idt = (TextField) parent.lookup("#idt");
					TextField txtName = (TextField) parent.lookup("#txtName");
					TextField txtKorean = (TextField) parent.lookup("#txtKorean");
					TextField txtMath = (TextField) parent.lookup("#txtMath");
					TextField txtEnglish = (TextField) parent.lookup("#txtEnglish");
					Student student = new Student(idt.getText(), txtName.getText(),
							Integer.parseInt(txtKorean.getText()), Integer.parseInt(txtMath.getText()),
							Integer.parseInt(txtEnglish.getText()));
					list.add(student);
					insertStulist(student);
					tableView.setItems(getStulist());
					stage.close();

				}
			});

			Button btnFormCancel = (Button) parent.lookup("#btnFormCancel");
			btnFormCancel.setOnAction(e -> {
				TextField txtName = (TextField) parent.lookup("#txtName");
				TextField txtKorean = (TextField) parent.lookup("#txtKorean");
				TextField txtMath = (TextField) parent.lookup("#txtMath");
				TextField txtEnglish = (TextField) parent.lookup("#txtEnglish");
				txtName.clear();
				txtKorean.clear();
				txtMath.clear();
				txtEnglish.clear();
			});
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void insertStulist(Student student) {
		Connection conn = ConnectionDB.getDB();
		String sql = "INSERT INTO student student values(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, student.getId());
			pstmt.setString(2, student.getName());
			pstmt.setInt(3, student.getKorean());
			pstmt.setInt(4, student.getMath());
			pstmt.setInt(5, student.getEnglish());

			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력됨.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	// 여기서부터 수정
	public void UpdateStulist(Student student) {
		Connection conn = ConnectionDB.getDB();
		String sql = "UPDATE student SET korean= ?,math= ?,engilsh= ? WHERE id = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, student.getKorean());
			pstmt.setInt(2, student.getMath());
			pstmt.setInt(3, student.getEnglish());
			pstmt.setString(4, student.getId());

			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력됨.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void DeleteStulist(Student student) {
		Connection conn = ConnectionDB.getDB();
		String sql = "DELETE FROM STUDENT WHERE id  = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, student.getId());
			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력됨.");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
