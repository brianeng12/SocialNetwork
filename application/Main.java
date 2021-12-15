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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final int WINDOW_WIDTH = 1400;
	private static final int WINDOW_HEIGHT = 700;
	private static final Insets PADDING = new Insets(10, 10, 10, 10);
	private static final Font LABEL_FONT = new Font("Arial", 20);
	private static final int SMALL_SPACING = 4;
	private static final int BUTTON_WIDTH = 150;
	private static final String DEFAULT_NUM_GROUP = "Nobody entered yet";
	private static final int MIN_USER_NAME_WIDTH = 250;
	private enum ACTIONS {
		NONE("No action taken"),
		ADDPERSON("Added user"),
		REMOVEPERSON("Removed user"),
		ADDFRIENDSHIP("Added friendship"),
		REMOVEFRIENDSHIP("Removed friendship"),
		RESET("Reset Social Network");
		
		private String action;

		ACTIONS(String string) {
			this.action = string;
		}
		
		private String getAction() {
			return this.action;
		}
	}

	
	private SocialNetwork SocialNetwork = new SocialNetwork();
	private String center = null;
	private Label LastActionDisplayLabel = new Label("No action taken");
	private Label NetworkLabel = new Label("No friendships have been entered");
	private BorderPane NetworkDisplay = new BorderPane();
	private Label NumberOfPeopleDisplayLabel = new Label(DEFAULT_NUM_GROUP);

	@Override
	public void start(Stage arg0) throws Exception {
		BorderPane root = new BorderPane();
		HBox topRow = new HBox();
		//BorderPane networkDisplay = new BorderPane();
		VBox leftColumn = new VBox();
		Label numberOfGroupsLabel = new Label("Number of Groups");
		Stage primaryStage = new Stage();
		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		FindConnectView findConnectView = new FindConnectView();
		FindConnectController findConnectControl = new FindConnectController(findConnectView, SocialNetwork, mainScene);
		FileChooser fileChooser = new FileChooser();
		
		//Setup file chooser
		configureFileChooser(fileChooser);
		
		//Initialize and create scene for finding connections between users
		findConnectControl.initialize();
		Scene findConnectScene = new Scene(findConnectView.getView(), WINDOW_WIDTH, WINDOW_HEIGHT);
		
		LastActionDisplayLabel.setUserData(arg0);
		
		/*
		 * setup the top row with buttons
		 */
		
		/*
		 * load button should prompt for a file but for now, 
		 * just randomize the list
		 */
		Button loadButton = new Button("Load");
		loadButton.setPrefWidth(BUTTON_WIDTH);
		loadButton.setOnAction(x -> {
			File loadFile = fileChooser.showOpenDialog(primaryStage);
			if (loadFile != null)
				try {
					SocialNetwork.loadFromFile(loadFile);
					if (SocialNetwork.selectedUser != null) center = SocialNetwork.selectedUser.getName();
					reloadNetwork();
					updateNumberOfGroups();
				} catch (FileNotFoundException e) {
					new Alert(AlertType.ERROR, "Unable to import file " + loadFile.getName()).show();
				}
		});
		
		/*
		 * TODO: implement
		 */
		Button exportButton = new Button("Export");
		exportButton.setPrefWidth(BUTTON_WIDTH);
		exportButton.setOnAction(x -> {
			File saveFile = fileChooser.showSaveDialog(primaryStage);
			if (saveFile != null) {
				try {
					SocialNetwork.saveToFile(saveFile);
				}
				catch (IOException e) {
					new Alert(AlertType.ERROR, "Unable to save file " + saveFile.getName()).show();
				} 
			}
		});
		
		/*
		 * clear all button should reset the network
		 */
		Button clearAllButton = new Button("Clear All");
		clearAllButton.setPrefWidth(BUTTON_WIDTH);
		clearAllButton.setOnAction(x -> { 
			SocialNetwork = new SocialNetwork();
			changeCenterUser(null);
			reloadNetwork();
			updateNumberOfGroups();
			updateLastAction(ACTIONS.RESET, null, null);
		});
		
		/*
		 * add person button should allow a new person to be added
		 */
		Button addPersonButton = new Button("Add Person");
		addPersonButton.setPrefWidth(BUTTON_WIDTH);
		addPersonButton.setOnAction( x -> {
			Stage stage = new Stage();
			stage.setTitle("Add a new person");
			BorderPane pane = new BorderPane();
			TextField newUserField = new TextField();
			newUserField.setPromptText("Enter the new person's name");
			newUserField.setMinWidth(MIN_USER_NAME_WIDTH);
			Button addButton = new Button("Add");
			addButton.setOnAction(y -> {
				addPersonOnClick(newUserField.getText());
				stage.close();
			});
			HBox hbox = new HBox();
			hbox.getChildren().addAll(newUserField, addButton);
			hbox.setSpacing(SMALL_SPACING);
			hbox.setPadding(PADDING);
			hbox.setAlignment(Pos.CENTER);
			pane.setCenter(hbox);
			Scene scene = new Scene(pane, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
			stage.setScene(scene);
			stage.show();
			});
		
		/*
		 * TODO: implement
		 */
		Button undo = new Button("Undo");
		undo.setPrefWidth(BUTTON_WIDTH);
		undo.setOnAction(x -> {
			ACTIONS action = (ACTIONS) LastActionDisplayLabel.getProperties().get("action");
			if(action != ACTIONS.NONE && action != ACTIONS.RESET) {
				String user1 = (String) LastActionDisplayLabel.getProperties().get("user1");
				if(action == ACTIONS.ADDPERSON) {
					if(SocialNetwork.removeUser(user1)) {
						if(center.equals(user1))
							changeCenterUser(null);
						reloadNetwork();
						updateLastAction(ACTIONS.REMOVEPERSON, user1, null);
					}
				}
				else if(action == ACTIONS.REMOVEPERSON) {
					addPersonOnClick(user1);
				}
				else {
					String user2 = (String) LastActionDisplayLabel.getProperties().get("user2");
					if(action == ACTIONS.ADDFRIENDSHIP) {
						removeFriendshipOnClick(user1, user2);
					}
					else if(action == ACTIONS.REMOVEFRIENDSHIP) {
						addFriendshipOnClick(user1, user2);
					}
				}
			}
		});
		
		/*
		 * button to change a friendship
		 * opens a separate pane to collect two people's names and 
		 * whether to add or remove a friendship
		 */
		Button changeFriendshipButton = new Button("Change Friendship");
		changeFriendshipButton.setPrefWidth(BUTTON_WIDTH);
		changeFriendshipButton.setOnAction( x -> {
			Stage stage = new Stage();
			stage.setTitle("Change friendship");
			BorderPane pane = new BorderPane();
			TextField userField1 = new TextField();
			userField1.setPromptText("Enter a person's name");
			userField1.setMinWidth(MIN_USER_NAME_WIDTH);
			Button addButton = new Button("Add friendship");
			TextField userField2 = new TextField();
			userField2.setPromptText("Enter a person's name");
			userField2.setMinWidth(MIN_USER_NAME_WIDTH);
			
			addButton.setOnAction(y -> {
				if(center == null && userField1.getText() != null &&
						SocialNetwork.personExists(userField1.getText()))
					center = userField1.getText();
				addFriendshipOnClick(userField1.getText(), userField2.getText());
				stage.close();
			});
			HBox hbox1 = new HBox();
			hbox1.getChildren().addAll(userField1);
			hbox1.setSpacing(SMALL_SPACING);
			hbox1.setPadding(PADDING);
			hbox1.setAlignment(Pos.CENTER);
			
			

			Button removeButton = new Button("Remove friendship");
			removeButton.setOnAction(z -> {
				String user1 = userField1.getText();
				String user2 = userField2.getText();
				removeFriendshipOnClick(userField1.getText(), userField2.getText());
				stage.close();
			});
			HBox hbox2 = new HBox();
			hbox2.getChildren().addAll(userField2);
			hbox2.setSpacing(SMALL_SPACING);
			hbox2.setPadding(PADDING);
			hbox2.setAlignment(Pos.CENTER);
			
			
			HBox hbox3 = new HBox();
			hbox3.getChildren().addAll(addButton, removeButton);
			hbox3.setSpacing(SMALL_SPACING);
			hbox3.setPadding(PADDING);
			hbox3.setAlignment(Pos.CENTER);
			
			VBox vbox = new VBox();
			vbox.getChildren().addAll(hbox1, hbox2, hbox3);
			pane.setCenter(vbox);
			Scene scene = new Scene(pane, WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
			stage.setScene(scene);
			stage.show();
			});
		
		/*
		 * find connections button should load separate scene
		 */
		Button findConnectionsButton = new Button("Find Connections");
		findConnectionsButton.setPrefWidth(BUTTON_WIDTH);
		findConnectionsButton.setOnAction(x -> {
			primaryStage.setScene(findConnectScene);
		});
		
		Button exitButton = new Button("Exit");
		exitButton.setPrefWidth(BUTTON_WIDTH);
		exitButton.setOnAction(x -> {
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
		NumberOfPeopleDisplayLabel.setFont(new Font("Arial", 32));
		numberOfGroups.getChildren().add(NumberOfPeopleDisplayLabel);
		numberOfGroups.setAlignment(Pos.CENTER);
		
		/*
		 * box for mutual friends
		 */
		VBox mutualFriends = new VBox();
		mutualFriends.setPrefSize(400, 400);
		TableView<Person> mutualFriendsTable = new TableView<Person>();
		mutualFriendsTable.setPrefHeight(350);
		TableColumn mutualFriendsColumn = new TableColumn("Mutual Friends");
		mutualFriendsColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
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
		searchForMutuals.setOnAction(x -> {
			mutualFriendsTable.getItems().clear();
			String user1 = person1.getText();
			String user2 = person2.getText();
			if(user1 != null && user2 != null) {
				Set<Person> mutuals = SocialNetwork.getMutualFriends(user1, user2);
				for(Person person : mutuals) {
					mutualFriendsTable.getItems().add(person);
				}
			}
		});
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
		LastActionDisplayLabel.setFont(LABEL_FONT);
		statusBox.setPadding(PADDING);
		statusBox.setSpacing(SMALL_SPACING);
		statusBox.getChildren().addAll(statusLabel, LastActionDisplayLabel);
		
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
		NetworkDisplay.setPrefSize(800, 450);
		NetworkDisplay.setCenter(NetworkLabel);
		
		HBox bottomRow = new HBox();
		TextField searchField = new TextField();
		searchField.setPromptText("Enter a name to search for");
		searchField.setMinWidth(MIN_USER_NAME_WIDTH);
		Button searchButton = new Button("Search");
		searchButton.setOnAction( x -> {
			if(searchField.getText() != null &&  
					SocialNetwork.personExists(searchField.getText()))
				changeCenterUser(searchField.getText());
			reloadNetwork();
		});
		
		bottomRow.getChildren().addAll(searchField, searchButton);
		bottomRow.setSpacing(SMALL_SPACING);
		bottomRow.setPadding(PADDING);
		bottomRow.setAlignment(Pos.CENTER);
		
		/*
		 * add everything to main box
		 */
		mainBox.getChildren().addAll(mainHeader, NetworkDisplay, bottomRow);
		
		root.setTop(topRow);
		root.setLeft(leftColumn);
		root.setCenter(mainBox);
		
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
	private void reloadNetwork() {
		Bounds bounds = NetworkDisplay.localToScreen(NetworkDisplay.getBoundsInLocal());
		StackPane stackPane = new StackPane();
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
		if(center != null) {
			Label label = new Label(center);
			label.setTextFill(Color.WHITE);
			label.setWrapText(true);
			Circle centerCircle = new Circle(50, Color.BLACK);
			
			/*
			 * store some of the top and center coordinates for easier use later
			 */
			double topX = bounds.getMinX();
			double topY = bounds.getMinY();
			double centerX = topX + (bounds.getWidth() / 2);
			double centerY = topY + (bounds.getHeight() / 2);
			double circleX = centerX;
			double circleY = centerY;
			centerCircle.relocate(circleX, circleY);
			stackPane.getChildren().addAll(centerCircle, label);
			
			/*
			 * go through the friends list and display them as well
			 */
			Set<Person> friends = SocialNetwork.getFriends(center);
			if(friends != null && !friends.isEmpty()) {
				int numberOfFriends = friends.size();
				int counter = 0;
				for(Person friend : friends) {
					label = new Label(friend.Name);
					label.setTextFill(Color.WHITE);
					Circle friendCircle = new Circle(30, Color.GRAY);
					stackPane = new StackPane();
					stackPane.getChildren().addAll(friendCircle, label);
					stackPane.setOnMouseClicked(x -> {
						changeCenterUser(friend.Name);
						reloadNetwork();
					});
				
					/*
					 * based on the number of people loaded,
					 * add the new node to one of the sections of the pane
					 * the top and bottom can hold more than the left and right
					 */
					if(counter < (2 * numberOfFriends / 5))
						topBox.getChildren().add(stackPane);
					else if (counter < 4 * numberOfFriends / 5)
						bottomBox.getChildren().add(stackPane);
					else if (counter < (9 * numberOfFriends / 10))
						leftBox.getChildren().add(stackPane);
					else
						rightBox.getChildren().add(stackPane);
					counter++;
						
				}
			}
		}
		
		/*
		 * add all of the sections
		 */
		NetworkDisplay.setTop(topBox);
		NetworkDisplay.setCenter(centerBox);
		NetworkDisplay.setBottom(bottomBox);
		NetworkDisplay.setLeft(leftBox);
		NetworkDisplay.setRight(rightBox);
	}
	
	/*
	 * private helper method to update the number of groups that have been entered
	 */
	private void updateNumberOfGroups() {
		NumberOfPeopleDisplayLabel.setText(SocialNetwork.getConnectedComponents() > 0 ? 
				SocialNetwork.getConnectedComponents() + "" : DEFAULT_NUM_GROUP);
	}
	
	/*
	 * private helper method to update the last action label
	 */
	private void updateLastAction(ACTIONS action, String user1, String user2) {
		String display;
		switch (action) {
		case ADDPERSON: display = action.getAction() + " " + user1;
			break;
		case REMOVEPERSON:; display = action.getAction() + " " + user1;
			break;
		case ADDFRIENDSHIP: display = action.getAction() + 
				" for " + user1 + " and " + user2;
			break;
		case REMOVEFRIENDSHIP: display = action.getAction() +  
				" for " + user1 + " and " + user2;
			break;
		case RESET: display = action.getAction();
			break;
		default: display = ACTIONS.NONE.getAction();
		}
		LastActionDisplayLabel.setText(display);
		LastActionDisplayLabel.getProperties().put("action", action);
		LastActionDisplayLabel.getProperties().put("user1", user1);
		LastActionDisplayLabel.getProperties().put("user2", user2);
	}
	
	private EventHandler<? super MouseEvent> addPersonOnClick(String user) {
		if(SocialNetwork.addUser(user)) {
			if(center == null && user != null &&
					SocialNetwork.personExists(user))
				changeCenterUser(user);
			reloadNetwork();
			updateNumberOfGroups();
			updateLastAction(ACTIONS.ADDPERSON, user, null);
		}
		return null;
	}
	
	private EventHandler<? super MouseEvent> addFriendshipOnClick(String user1,
			String user2) {
		if(SocialNetwork.addFriends(user1, user2)) {
			reloadNetwork();
			updateNumberOfGroups();
			updateLastAction(ACTIONS.ADDFRIENDSHIP, user1, user2);
		}
		
		return null;
	}
	
	private EventHandler<? super MouseEvent> removeFriendshipOnClick(String user1,
			String user2) {
		if(SocialNetwork.removeFriends(user1, user2)) {
			updateNumberOfGroups();
			reloadNetwork();
			updateLastAction(ACTIONS.REMOVEFRIENDSHIP, user1, user2);
		}
		
		return null;
	}
	
	/**
	 * configureFileChooser: Sets the initial directory and filter extension for loading and saving file
	 * @param fileChooser: FileChooser to load and save data from app
	 */
	private void configureFileChooser(FileChooser fileChooser) {
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("txt", "*.txt"));
	}
	
	private void changeCenterUser(String user) {
		center = user;
		SocialNetwork.selectedUser = SocialNetwork.graph.getNode(user);
	}
}
