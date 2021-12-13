package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FindConnectView {
	
	private static final int WINDOW_WIDTH = 1400;
	private static final int WINDOW_HEIGHT = 700;
	private static final Insets PADDING = new Insets(10, 10, 10, 10);
	private static final int SMALL_SPACING = 4;
	
	Parent view;
	
	Button backBtn = new Button("Back");
	Label title = new Label("Friend Connection");
//	GridPane connectPane = new GridPane();
	Pane connectPane = new Pane();
	Label lPerson1 = new Label("Person 1:");
	Label lPerson2 = new Label("Person 2:");
	TextField tbPerson1 = new TextField();
	TextField tbPerson2 = new TextField();
	Button searchBtn = new Button("Search");
	
	public FindConnectView() {
		view = createView();
	}

	private Parent createView() {
		
		BorderPane.setAlignment(backBtn, Pos.CENTER_LEFT);
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setAlignment(connectPane, Pos.CENTER_LEFT);
		
		BorderPane bp = new BorderPane();
		BorderPane bpTop = new BorderPane();
		
		bpTop.setLeft(backBtn);
		bpTop.setCenter(title);
		
		
		HBox hbox2 = new HBox();
		hbox2.getChildren().addAll(lPerson1, tbPerson1, lPerson2, tbPerson2, searchBtn);
		hbox2.setPadding(PADDING);
		hbox2.setSpacing(SMALL_SPACING);
		
		connectPane.setStyle("-fx-border-color: black");
		
		bp.setPrefWidth(WINDOW_WIDTH);
		bp.setPrefHeight(WINDOW_HEIGHT);

		bp.setTop(bpTop);
		bp.setCenter(connectPane);
		bp.setBottom(hbox2);
		
		bp.setPadding(new Insets(10, 10, 10, 10));
		
		return bp;
	}
	
	public Parent getView() {
		return view;
	}
}
