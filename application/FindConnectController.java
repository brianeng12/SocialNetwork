package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FindConnectController {
	Scene prevScene;
	FindConnectView view;
	ManagerClass networkManager;
	
	public FindConnectController(FindConnectView view, ManagerClass networkManager, Scene prevScene) {
		this.prevScene = prevScene;
		this.view = view;
		this.networkManager = networkManager;
	}
	
	public void initialize() {
		setView();
	}
	
	public void setView() {
		
		view.searchBtn.setOnAction(x -> {
			String person1 = view.tbPerson1.getText();
			String person2 = view.tbPerson2.getText();
			ArrayList<String> connections = new ArrayList<String>();
			
			if (!validatePerson(person1, networkManager)) {
				new Alert(AlertType.ERROR, person1 + " is not a user in the Social Network").show();
				
			}
			else if (!validatePerson(person2, networkManager)) {
				new Alert(AlertType.ERROR, person1 + " is not a user in the Social Network").show();
			}
			
			//connections = networkManager.network.findShortestPath(person1, person2);
			
			else if (connections.isEmpty()) view.connectPane.getChildren().add(new Label("No connection found"));
			else loadConnections(view, connections);
		});
		
		view.backBtn.setOnAction(x -> {
			Stage appStage = (Stage) ((Node) x.getSource()).getScene().getWindow();
			clearView(view);
			appStage.setScene(prevScene);
		});
	}
	
	private void clearView(FindConnectView view) {
		view.tbPerson1.clear();
		view.tbPerson2.clear();
		view.connectPane.getChildren().clear();
	}
	
	private void loadConnections(FindConnectView view, List<String> connections) {
		// TODO Auto-generated method stub
		for (int i = 0; i < connections.size(); i++) {
			Circle userCircle = new Circle(20);
			Text userName = new Text(connections.get(i));
			
			view.connectPane.getChildren().addAll(userCircle, userName);
		}
		
	}

	private boolean validatePerson(String person, ManagerClass networkManager) {
		if (person == "" || person == null) return false;
		return false;
		
		//return networkManager.network.inNetwork(person);
	}
}
