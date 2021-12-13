package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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
			
			if (person1.isEmpty() || person2.isEmpty())  {
				new Alert(AlertType.ERROR, "Must specify both users").show();
				
			}
			
			else if (!validatePerson(person1)) {
				new Alert(AlertType.ERROR, person1 + " is not a user in the Social Network").show();
				
			}
			else if (!validatePerson(person2)) {
				new Alert(AlertType.ERROR, person1 + " is not a user in the Social Network").show();
			}
			
			else  {
				//TODO: Add back after ManagerClass is implemented
//				connections = socialNetwork.getShortestPath(person1, person2);
				connections.add(new Person("Chris"));
				connections.add(new Person("Brian"));
				connections.add(new Person("Bill"));
				connections.add(new Person("Ben"));
				if (connections.isEmpty()) view.connectPane.getChildren().add(new Label("No connection found"));
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
		Double width = view.connectPane.getWidth();
		Double height = view.connectPane.getHeight();
		int numConnections = connections.size();
		Double circleRadius = width/(numConnections*3 + 1);
		
		for (int i = 0; i < connections.size(); i++) {
			Circle userCircle = new Circle((2 + 3*i)*circleRadius, height/2, 
					circleRadius, Color.BLACK);
			Label userName = new Label(connections.get(i).Name);
			userName.setTextFill(Color.WHITE);

			view.connectPane.getChildren().add(userCircle);
			view.connectPane.getChildren().add(userName);
			
			userName.relocate((2 + 3*i)*circleRadius - userName.getHeight()/2, (height-userName.getHeight())/2);
			
			if (i < connections.size()-1) {
				Line line = new Line((3 + 3*i)*circleRadius, height/2, (4 + 3*i)*circleRadius
						, view.connectPane.getHeight()/2);
				view.connectPane.getChildren().add(line);
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
		return true;
		//return networkManager.network.inNetwork(person);
	}
}
