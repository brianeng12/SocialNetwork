/**
 * @author Ben Hutchison
 * CS400 010
 * Project:	Final Project
 * Team: A12
 * Collaborators:
 * 		Chris Lansford
 * 		Bill Johnson
 * 		Brian Eng 
 * 
 * Social Network GUI
 */

package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final int WINDOW_WIDTH = 1400;
	private static final int WINDOW_HEIGHT = 700;
	private static final Insets PADDING = new Insets(10, 10, 10, 10);
	private static final Font LABEL_FONT = new Font("Arial", 20);
	private static final int SMALL_SPACING = 4;
	private static final int BUTTON_WIDTH = 150;
	private static final String DEFAULT_NUM_GROUP = "No friendships entered";
	
	private ArrayList<Person> network = new ArrayList<Person>();
	
	private class Person {
		String name;
		ArrayList<Person> friends = new ArrayList<Person>();
		Circle displayShape = null;
		
		private Person(String name) {
			this.name = name;
			this.displayShape = new Circle(30, Color.BLACK);
		}
		
		private Person() {
			this("ExampleName" + network.size());
		}
	}

	@Override
	public void start(Stage arg0) throws Exception {
		BorderPane root = new BorderPane();
		HBox topRow = new HBox();
		BorderPane networkDisplay = new BorderPane();
		VBox leftColumn = new VBox();
		Label numberOfGroupsLabel = new Label("Number of Groups");
		Label numberOfGroupsValue = new Label(DEFAULT_NUM_GROUP);
		Stage primaryStage = new Stage();
		Label lastActionStatus = new Label("No action taken");
		
		/*
		 * setup the top row with buttons
		 */
		
		/*
		 * load button should prompt for a file but for now, 
		 * just randomize the list
		 */
		Button loadButton = new Button("Load");
		loadButton.setPrefWidth(BUTTON_WIDTH);
		loadButton.setOnMouseClicked(x -> {
			network.clear();
			networkDisplay.getChildren().clear();
			Person person = null;
			for(int i = 0; i < 3; i++) {
				person = new Person();
				network.add(person);
				reloadNetwork(networkDisplay, person);
			}
			updateNumberOfGroups(numberOfGroupsValue);
			updateLastAction(lastActionStatus, "Added " + person.name);
		});
		
		/*
		 * TODO: implement
		 */
		Button exportButton = new Button("Export");
		exportButton.setPrefWidth(BUTTON_WIDTH);
		
		/*
		 * clear all button should reset the network
		 */
		Button clearAllButton = new Button("Clear All");
		clearAllButton.setPrefWidth(BUTTON_WIDTH);
		clearAllButton.setOnMouseClicked(x -> { 
			network.clear();
			networkDisplay.getChildren().clear();
			updateNumberOfGroups(numberOfGroupsValue);
			updateLastAction(lastActionStatus, "Cleared everyone");
			});
		
		/*
		 * add person button should allow a new person to be added
		 */
		Button addPersonButton = new Button("Add Person");
		addPersonButton.setPrefWidth(BUTTON_WIDTH);
		addPersonButton.setOnMouseClicked( x -> {
			Person person = new Person();
			network.add(person);
			reloadNetwork(networkDisplay, person);
			updateNumberOfGroups(numberOfGroupsValue);
			updateLastAction(lastActionStatus, "Added " + person.name);
			});
		
		/*
		 * TODO: implement
		 */
		Button undo = new Button("Undo");
		undo.setPrefWidth(BUTTON_WIDTH);
		
		/*
		 * TODO: implement
		 */
		Button changeFriendshipButton = new Button("Change Friendship");
		changeFriendshipButton.setPrefWidth(BUTTON_WIDTH);
		
		/*
		 * find connections button should load separate scene
		 */
		Button findConnectionsButton = new Button("Find Connections");
		findConnectionsButton.setPrefWidth(BUTTON_WIDTH);
		
		Button exitButton = new Button("Exit");
		exitButton.setPrefWidth(BUTTON_WIDTH);
		exitButton.setOnMouseClicked(x -> {
			primaryStage.close();
		});
		
		topRow.getChildren().addAll(loadButton, exportButton, clearAllButton, undo,
				addPersonButton, changeFriendshipButton, findConnectionsButton, exitButton);
		topRow.setSpacing(25);
		topRow.setAlignment(Pos.CENTER);
		
		
		/*
		 * box for number of groups entered into the network
		 */
		VBox numberOfGroups = new VBox();
		numberOfGroups.setPrefSize(400, 200);
		numberOfGroupsLabel.setFont(LABEL_FONT);
		numberOfGroups.getChildren().add(numberOfGroupsLabel);
		numberOfGroupsValue.setFont(new Font("Arial", 32));
		numberOfGroups.getChildren().add(numberOfGroupsValue);
		numberOfGroups.setAlignment(Pos.CENTER);
		
		/*
		 * box for mutual friends
		 */
		VBox mutualFriends = new VBox();
		mutualFriends.setPrefSize(400, 400);
		TableView<String> mutualFriendsTable = new TableView<String>();
		mutualFriendsTable.setPrefHeight(350);
		TableColumn mutualFriendsColumn = new TableColumn("Mutual Friends");
		mutualFriendsColumn.setPrefWidth(400);
		mutualFriendsTable.getColumns().add(mutualFriendsColumn);
		mutualFriendsTable.setPlaceholder(new Label("No friendships to show"));
		TextField person1 = new TextField();
		person1.setPromptText("Enter person name");
		person1.setPrefWidth(125);
		TextField person2 = new TextField();
		person2.setPrefWidth(125);
		person2.setPromptText("Enter person name");
		Button searchForMutuals = new Button("Search");
		HBox searchBoxes = new HBox();
		searchBoxes.setPadding(PADDING);
		searchBoxes.setSpacing(SMALL_SPACING);
		searchBoxes.getChildren().addAll(person1, new Label(" and "), person2, searchForMutuals);
		mutualFriends.getChildren().addAll(mutualFriendsTable, searchBoxes);
		
		/*
		 * box for status/last action
		 */
		HBox statusBox = new HBox();
		Label statusLabel = new Label("Status:");
		statusLabel.setFont(LABEL_FONT);
		lastActionStatus.setFont(LABEL_FONT);
		statusBox.setPadding(PADDING);
		statusBox.setSpacing(SMALL_SPACING);
		statusBox.getChildren().addAll(statusLabel, lastActionStatus);
		
		/*
		 * add boxes to column
		 */
		leftColumn.getChildren().addAll(numberOfGroups, mutualFriends, statusBox);
		
		/*
		 * setup right side with display
		 */
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		Label mainLabel = new Label("Social Network");
		mainLabel.setFont(LABEL_FONT);
		HBox mainHeader = new HBox();
		mainHeader.getChildren().add(mainLabel);
		mainHeader.setAlignment(Pos.CENTER);
		
		VBox networkBox = new VBox();
		networkBox.setPrefSize(800, 500);
		networkDisplay.setPrefSize(800, 450);
		Label networkLabel = new Label("No friendships have been loaded");
		networkDisplay.setCenter(networkLabel);
		
		HBox bottomRow = new HBox();
		TextField searchField = new TextField();
		searchField.setPromptText("Enter a name to search for");
		searchField.setMinWidth(250);
		Button searchButton = new Button("Search");
		bottomRow.getChildren().addAll(searchField, searchButton);
		bottomRow.setSpacing(SMALL_SPACING);
		bottomRow.setPadding(PADDING);
		bottomRow.setAlignment(Pos.CENTER);
		
		/*
		 * add everything to main box
		 */
		mainBox.getChildren().addAll(mainHeader, networkDisplay, bottomRow);
		
		root.setTop(topRow);
		root.setLeft(leftColumn);
		root.setCenter(mainBox);
		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		primaryStage.setTitle("ATeam 12 - Social Network");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * private helper method to reload the network that is displayed
	 * makes the center argument the center of the display and displays the adjacent 
	 * people as well
	 */
	private void reloadNetwork(BorderPane display, Person center) {
		Bounds bounds = display.localToScreen(display.getBoundsInLocal());
		StackPane stackPane = new StackPane();
		Label label = new Label(center.name);
		label.setTextFill(Color.WHITE);
		label.setWrapText(true);
		center.displayShape.radiusProperty().bind(label.widthProperty());
		
		/*
		 * store some of the top and center coordinates for easier use later
		 */
		double topX = bounds.getMinX();
		double topY = bounds.getMinY();
		double centerX = topX + (bounds.getWidth() / 2);
		double centerY = topY + (bounds.getHeight() / 2);
		double circleX = centerX;
		double circleY = centerY;
		center.displayShape.relocate(circleX, circleY);
		stackPane.getChildren().addAll(center.displayShape, label);
		
		HBox topBox = new HBox();
		HBox centerBox = new HBox();
		HBox bottomBox = new HBox();
		VBox leftBox = new VBox();
		VBox rightBox = new VBox();
		
		/*
		 * initialize some basic properties to be able to try
		 * to distribute the nodes somewhat evenly
		 */
		topBox.setMaxHeight(bounds.getHeight() / 3);
		topBox.setPrefHeight(bounds.getHeight() / 3);
		centerBox.setMaxHeight(bounds.getHeight() / 3);
		centerBox.setPrefHeight(bounds.getHeight() / 3);
		centerBox.setPrefWidth(bounds.getWidth() / 3);
		bottomBox.setMaxHeight(bounds.getHeight() / 3);
		bottomBox.setPrefHeight(bounds.getHeight() / 3);
		leftBox.setPrefWidth(bounds.getWidth() / 3);
		rightBox.setPrefWidth(bounds.getWidth() / 3);
		
		
		//first, set the center/focus person
		centerBox.getChildren().add(stackPane);
		centerBox.setAlignment(Pos.CENTER);
		
		/*
		 * set some alignment and spacing for readability
		 */
		topBox.setAlignment(Pos.CENTER);
		topBox.setSpacing(25);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setSpacing(25);
		
		
		for(int i = 0; i < network.size(); i++) {
			if(network.get(i) == center)
				continue;
			
			label = new Label(network.get(i).name);
			label.setTextFill(Color.WHITE);
			network.get(i).displayShape.radiusProperty().unbind();
			network.get(i).displayShape.setRadius(50);
			stackPane = new StackPane();
			stackPane.getChildren().addAll(network.get(i).displayShape, label);
			
			/*
			 * based on the number of people loaded,
			 * add the new node to one of the sections of the pane
			 * the top and bottom can hold more than the left and right
			 */
			if(i < (2 * network.size() / 5))
				topBox.getChildren().add(stackPane);
			else if (i < 4 * network.size() / 5)
				bottomBox.getChildren().add(stackPane);
			else if (i < (9 * network.size() / 10))
				leftBox.getChildren().add(stackPane);
			else
				rightBox.getChildren().add(stackPane);
				
		}
		
		/*
		 * add all of the sections
		 */
		display.setTop(topBox);
		display.setCenter(centerBox);
		display.setBottom(bottomBox);
		display.setLeft(leftBox);
		display.setRight(rightBox);
	}
	
	/*
	 * private helper method to update the number of groups that have been entered
	 */
	private void updateNumberOfGroups(Label label) {
		label.setText(network.size() > 0 ? network.size() + "" : DEFAULT_NUM_GROUP);
	}
	
	/*
	 * private helper method to update the last action label
	 */
	private void updateLastAction(Label label, String text) {
		label.setText(text.isEmpty() ? "No action taken" : text);
	}
}
