package basic.control;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BoardController implements Initializable{
	@FXML TableView<Board> boardView;
	@FXML TextField txtTitle;
	@FXML ComboBox<String> comboPublic;
	@FXML TextField txtExitDate;
	@FXML TextArea txtContent;
	@FXML Button btnPrev;
	@FXML Button btnNext;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		TableColumn<Board,String> tcTitle = new TableColumn<>("제목");
		tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcTitle.setPrefWidth(80);
		boardView.getColumns().add(tcTitle);
		TableColumn<Board,String> tcPublicity = new TableColumn<>("공개여부");
		tcPublicity.setCellValueFactory(new PropertyValueFactory<>("publicity"));
		
		tcPublicity.setPrefWidth(80);
		boardView.getColumns().add(tcPublicity);
		boardView.setItems(getBoardList());
		boardView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Board>() {
			@Override
			public void changed(ObservableValue<? extends Board> arg0, Board o, Board n) {
				// TODO Auto-generated method stub
				txtTitle.setText(n.getTitle());
				comboPublic.setValue(n.getPublicity());
				txtExitDate.setText(n.getExitDate());
				txtContent.setText(n.getContent());
			}
			
		});
		
		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				if(boardView.getSelectionModel().selectLast()) {
//					boardView.getSelectionModel().selectFirst();
//				}
				boardView.getSelectionModel().selectNext();
			}
		});
		
		btnPrev.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boardView.getSelectionModel().selectPrevious();
			}
		});
	}
	
	
	
	public ObservableList<Board> getBoardList() {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr", passwd="hr";
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		String sql  ="select * from new_board order by 1";
		ObservableList<Board> list =FXCollections.observableArrayList();
		try {
			PreparedStatement pstmt =conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board(rs.getString("title"),rs.getString("password"),rs.getString("publictiy"),
						rs.getString("exit_date"),rs.getString("content"));
				list.add(board);
			};
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

}
