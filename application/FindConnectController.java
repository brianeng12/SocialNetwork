package application;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class FindConnectController {
	Scene prevScene;
	FindConnectView view;
	SocialNetwork socialNetwork;
	
	public FindConnectController(FindConnectView view, SocialNetwork socialNetwork, Scene prevScene) {
		this.prevScene = prevScene;
		this.view = view;
		this.socialNetwork = socialNetwork;
	}
	
	/**
	 * initialize: initial set up for controller, binds events to view
	 */
	public void initialize() {
		setView();
	}
	
	/**
	 * setView: sets actions for search and back buttons
	 */
	private void setView() {
		
		view.searchBtn.setOnAction(x -> {
			String person1 = view.tbPerson1.getText();
			String person2 = view.tbPerson2.getText();
			List<Person> connections = new ArrayList<Person>();
			view.connectPane.getChildren().clear();
			
			if (person1.isEmpty() || person2.isEmpty())  {
				new Alert(AlertType.ERROR, "Must specify both users").show();
				
			}
			
			else if (!validatePerson(person1)) {
				new Alert(AlertType.ERROR, person1 + " is not a user in the Social Network").show();
				
			}
			else if (!validatePerson(person2)) {
				new Alert(AlertType.ERROR, person2 + " is not a user in the Social Network").show();
			}
			
			else  {
				connections = socialNetwork.getShortestPath(person1, person2);
				if (connections == null) view.connectPane.getChildren().add(new Label("No connection found"));
				else loadConnections(connections);
			}
		});
		
		view.backBtn.setOnAction(x -> {
			Stage appStage = (Stage) ((Node) x.getSource()).getScene().getWindow();
			clearView();
			appStage.setScene(prevScene);
		});
	}
	
	/**
	 * clearView: clears data from textboxes and connectPane
	 */
	private void clearView() {
		view.tbPerson1.clear();
		view.tbPerson2.clear();
		view.connectPane.getChildren().clear();
	}
	
	/**
	 * loadConnections: Populates the pane showing the shortest list of connections from
	 * 					user 1 to user 2
	 * @param connections: List of Persons connecting user 1 to user 2
	 */
	private void loadConnections(List<Person> connections) {
		Double circleRadius = 40.00;
		view.connectPane.setAlignment(Pos.CENTER);
		
		for (int i = 0; i < connections.size(); i++) {
			Circle userCircle = new Circle(circleRadius, Color.BLACK);
			Label userName = new Label(connections.get(i).Name);
			userName.setTextFill(Color.WHITE);
			userName.setStyle("-fx-font-size: 15px;"
					+ "-fx-font-weight: bold;");
			
			StackPane node = new StackPane();
			node.getChildren().addAll(userCircle, userName);
			
			view.connectPane.add(node, 2*i, 0);
			
			if (i < connections.size()-1) {
				Line line = new Line(0, 0, circleRadius, 0);
				view.connectPane.add(line, 2*i+1, 0);
			}
		}
	}

	/**
	 * validatePerson: Checks if the person is a valid input
	 * @param person
	 * @return true if the person exists in the network
	 */
	private boolean validatePerson(String person) {
		if (person == null || person.isEmpty()) return false;
		
		return socialNetwork.personExists(person);
	}
}
