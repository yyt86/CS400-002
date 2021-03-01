/**
 * 
 */
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import application.Main.UserNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

/**
 * @author james
 * @param <SocialNetworkManager>
 *
 */
public class Main extends Application {
	// store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also
	private List<String> args;

	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 600;
	private static final String APP_TITLE = "SocialNetworkManager";
	
	// GUI needs to interact with SocialNetworkManager
	private static SocialNetworkManager mgr = new SocialNetworkManager();
	// item that user select
	private static String selectedUser;

	//present the user and friends list
	private ObservableList<String> obl; 
	
	
	//present the number of users for the whole social network
	private Label order;
	//present the number of friendships for the whole social network
	private Label size;
	//present the number of connected components for the whole social network
	private Label connectedComponents;
	//present the number of friends for a specific user
	private Label friendsofcent;
	//present the status/help message
	private Label result;
	
	//buttons for user page
	Button Import;
	Button Export;
	Button AddFriendship;
	Button RemoveAllUsers;
	Button ViewFriend;
	Button AddUser;
	Button DeleteUser;
	Button MutualFriends;
	Button ShortestPath;
	
	//buttons for friends page
	Button AddFriend;
	Button RemoveAllFriend;
	Button RemoveSelectedFriend;
	Button RemoveFriend;
	Button ViewFriendship;
	Button Menu;
	
	//vbox collects operation button in user page 
	//the box is placed on the right pane
	VBox operation;
	Label title; 
	
	//construct the social network manager
	public static SocialNetworkManager getSocialNetworkManager() {
		return mgr;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// save args example
		args = this.getParameters().getRaw();
		
		/**
		 * create a vertical box in top panel for searching the friends of a specified user;
		 * a label, textfield, button are included. 
		 */
		VBox vbox = new VBox();
		Label prompt = new Label("Enter the person you want to see his/her network");
		TextField input = new TextField();
		Button rotateButton = new Button("Check");
		
		/**
		 *  create a observable list for viewing users or friends.
		 *  The observable list will be placed on the left of the border pane.
		 */
		obl = FXCollections.observableList(new ArrayList<String>());
		ListView<String> lv = new ListView<String>(obl);
		lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// create a scroll bar for friend list
		ScrollPane pane = new ScrollPane();
		

		/**
		 * create a label to displays the status/help messages;
		 * the label will be  packed as the first part of a central vertical box.
		 */
		result = new Label();
		result.setFont(new Font("Arial", 16));
		result.setTextFill(Color.web("#0076a3"));

		/**
		 * create a grid pane to displays number of users, friendships, connected components 
		 * for the whole graph. The gridpane will be packed in in the second part of a central vertical box.
		 */
		GridPane numbers = new GridPane();
		numbers.setHgap(10);
	    numbers.setVgap(10);
	    numbers.setPadding(new Insets(20, 150, 10, 10));
	    numbers.add(new Label("Number of users in the Network:"), 0, 0);
	    numbers.add(new Label("Number of friendships in the Network:"), 0, 1);
	    numbers.add(new Label("Number of connected components in the Network: "), 0, 2);
	    numbers.add(new Label("    "), 0, 3);
	    numbers.add(new Label("Number of friends of central user: "), 0, 4);
	    order = new Label();
	    size = new Label();
	    connectedComponents = new Label();
	    friendsofcent = new Label();
	    numbers.add(order, 1, 0); //number of users
	    numbers.add(size, 1, 1); //number of friendships
	    numbers.add(connectedComponents, 1, 2); //number of connected components 
	    numbers.add(new Label("    "), 1, 3);
	    numbers.add(friendsofcent, 1, 4); // number of friends for central user
		
	    /**
		 * create a grid pane to create friendship viewer to display
		 * central user and his/her friends. 
		 * The gridpane will be packed in in the thirs part of a central vertical box.
		 */
	    GridPane centralUserNtwk = new GridPane();
		centralUserNtwk.setPrefHeight(0.5*WINDOW_WIDTH);
		centralUserNtwk.setPrefWidth(0.75*WINDOW_HEIGHT);
		//Specify the gridpane's size and number of rows and columns
		int cols = 11;
		int rows = 11;
		double gpWidth = 0.5*WINDOW_WIDTH;
		double gpHeight = 0.6*WINDOW_HEIGHT;
		for(int i=0;i<cols;i++) {
			//hard code
			centralUserNtwk.getColumnConstraints().add(new ColumnConstraints(gpWidth/cols));
			centralUserNtwk.getRowConstraints().add(new RowConstraints(gpHeight/rows));
		}
		
		/**
		 * create an operation vbox including the title and operation buttons 
		 * in user page or friends buttons in friend page.
		 * The pages between will be switched by clicking the "ViewFriend" button.
		 * The vbox will be placed on the right border pane.
		 */
		// create a vbox for operations
		operation = new VBox();
		operation.setSpacing(4);
		operation.setAlignment(Pos.CENTER);
		operation.setPadding(new Insets(15));
		
		// title for opereation vbox
		title = new Label();
		title.setText("Operations");
		title.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
		title.setAlignment(Pos.CENTER);

		// Buttons for user operations
		Import = new Button("Import");
		Export = new Button("Export");
		AddFriendship = new Button("Add Friendship");
		RemoveAllUsers = new Button("RemoveAllUsers");
		ViewFriend = new Button("View Friendships");
		AddUser = new Button("Add Users");
		DeleteUser = new Button("Delete Users");
		MutualFriends = new Button("Mutual Friends");
		ShortestPath = new Button("Shortest path between two");

		// set size for buttons
		Import.setPrefSize(150, 30);
		Export.setPrefSize(150, 30);
		AddFriendship.setPrefSize(150, 30);
		RemoveAllUsers.setPrefSize(150, 30);
		ViewFriend.setPrefSize(150, 30);
		AddUser.setPrefSize(150, 30);
		DeleteUser.setPrefSize(150, 30);
		MutualFriends.setPrefSize(150, 30);
		ShortestPath.setPrefSize(150, 30);
		
		//initialize the state of the operation buttons
		RemoveAllUsers.setDisable(true);
		ViewFriend.setDisable(true);
		AddUser.setDisable(false);
		DeleteUser.setDisable(true);
		
		// buttons for friend page
		AddFriend = new Button("Add Friend");
		RemoveAllFriend = new Button("Remove All Friends");
		RemoveSelectedFriend = new Button("Remove Selected Friends");
		RemoveFriend = new Button("Remove Friend");
		ViewFriendship = new Button("View Friendship(Friend Page)");
		Menu = new Button("Menu");
		
		AddFriend.setPrefSize(150, 30);
		RemoveFriend.setPrefSize(150, 30);
		RemoveSelectedFriend.setPrefSize(150, 30);
		RemoveAllFriend.setPrefSize(150, 30);
		ViewFriendship.setPrefSize(150, 30);
		Menu.setPrefSize(150, 30);
		
	
		FileChooser fileChooser = new FileChooser();
		
			/**
		 *  handle event for the "DeleteUser" button
		 */
		DeleteUser.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				try {
					if(lv.getSelectionModel().getSelectedItems() != null) {
						// get multi-users
						List<String> selectedUser = lv.getSelectionModel().getSelectedItems();
						String a = "";
						
						// delete them one by one 
						for(int i = selectedUser.size()-1;i>=0;i--) {
							String userToDelete = selectedUser.get(i);
							a+=userToDelete+", "; // intall the deleted users to a string
							List<String> friends = mgr.getPersonalNetwork(userToDelete);
							mgr.removePerson(userToDelete);
						}
						
						//update the users list 
						List<String> updated = new ArrayList<String>();
						Set<String> updatedSet = mgr.getAllUsers();
						for (String item : updatedSet) {
							updated.add(item);
						}
						obl.clear();
						
						// if the remained users is not empty, viewers update the status and numbers information 
						if(updated.size()!=0) {
							result.setText(" [Prompt] : " + a + "is deleted.");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							
							// reset the buttons status
							FriendList.setAsUserOperation(operation, title, Import, Export, AddFriendship, RemoveAllUsers, 
									ViewFriend, AddUser, DeleteUser,MutualFriends, ShortestPath);
							
							obl.addAll(updated);	
							// the there is central user, update the number of his/her friends and friend viewer
							if (!lv.getSelectionModel().getSelectedItems().equals( mgr.getCentralPerson())) {
								friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
								centralUserNtwk.getChildren().clear();
								centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
				
							}
							
							// if the central user was deleted, set the number of his/her friends to be 0
							// and the friend viewer will be cleared.
							else {
								friendsofcent.setText("0");
								centralUserNtwk.getChildren().clear();	
								
							}										
						}					
						else {
							result.setText(" [Prompt] : " + a + "is deleted. List is Empty");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							
							centralUserNtwk.getChildren().clear();
							FriendList.setAsUserOperation(operation, title, Import, Export, AddFriendship, RemoveAllUsers, ViewFriend, AddUser,
									DeleteUser,MutualFriends, ShortestPath);
						}
						DeleteUser.setDisable(true);
					}
				}catch(Exception e) {
				}

			}
		});

		/**
		 *  handle event for the "Import" button
		 */
		Import.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				// get the selected imported file
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				try {
					// if the wrong file is imported, throw exception
					if(!selectedFile.getName().contains(".txt")) {
						throw new IOException();
					}
					
					// construct the social network and update the users in the observable list 
					mgr.constructNetwork(selectedFile);
					List<String> updated = new ArrayList<String>();
					Set<String> updatedSet = mgr.getAllUsers();
					for (String item : updatedSet) {
						updated.add(item);
					}
					obl.clear();
					obl.addAll(updated);

					// update the viewers
					result.setText(" [Prompt] : File import successfully.");
					order.setText(Integer.toString(mgr.order()));
					size.setText((Integer.toString(mgr.size())));
					connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
					friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
					
					// update the buttons status
					RemoveAllUsers.setDisable(false);
					AddUser.setDisable(false);
				
					//update the network viewer
					centralUserNtwk.getChildren().clear();
					centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
					
				} 
				catch (FileNotFoundException exception) {
					// if FileNotFoundException exception is thrown, po an alert dialog. 
					Alert alert1 = new Alert(AlertType.WARNING);
					alert1.setTitle("Warning Dialog");
					alert1.setHeaderText("Warning messager");
					alert1.setContentText("Input file can not be found!");
					alert1.showAndWait();
					result.setText(" [Prompt] : File fail to import.");

				}catch (IOException exception) {
					// if IOException exception is thrown, po an alert dialog. 
					Alert alert2 = new Alert(AlertType.WARNING);
					alert2.setTitle("Warning Dialog");
					alert2.setHeaderText("Warning messager");
					alert2.setContentText("Input file can not be read!");
					alert2.showAndWait();
					result.setText(" [Prompt] : File fail to import.");
				} 
				catch (Exception exception) {
					
				}
			}
		});
    
		
		/**
		 *  handle event for the "Export" button
		 */
		Export.setOnAction(new EventHandler<ActionEvent>() {
			String S;
			public void handle(ActionEvent e) {
		        
	            FileChooser fileChooser = new FileChooser();
	            
	            //Set extension filter for text files
	            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	            fileChooser.getExtensionFilters().add(extFilter);
	 
	            //Show save file dialog
	            File file = fileChooser.showSaveDialog(primaryStage);
	 
	            if (file != null) {
	            	try {
	            		mgr.saveLog(file);
	            	}
	            	catch (IOException io) {
	            		result.setText(" [Prompt] : Export IO Exception");
	            	}
	            }
			}
		});
		

		/**
		 *  handle event for the "RemoveAllUsers" button
		 */
		RemoveAllUsers.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				try {

                    Set<String> a = mgr.getAllUsers();
                    String temp [] = new String[a.size()];
                    int i=0;
                    for(String element:a) {
                    	temp[i]=element;
                    	i++;
                    }
                    //List<history> thisHistory = new ArrayList<history>();
                    for(int j=0; j<temp.length;j++) {
                    	List<String> friends = mgr.getPersonalNetwork(temp[j]);

                    	mgr.removePerson(temp[j]);
                    }

					obl.clear();
					result.setText(" [Prompt] : All users removed.");
					order.setText(Integer.toString(mgr.order()));
					size.setText((Integer.toString(mgr.size())));
					connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
					friendsofcent.setText("0");
					//update button accessibility
					ViewFriend.setDisable(true);
					AddUser.setDisable(false);
					//AddFriendship.setDisable(false);
					DeleteUser.setDisable(true);
					RemoveAllUsers.setDisable(true);
					centralUserNtwk.getChildren().clear();
				} catch (Exception nfe) {
					//nfe.printStackTrace();
               		// result.setText(" [Prompt] : invalid input");
				}
			}
		});
		



		/**
		 *  handle event for the "ViewFriend" button,
		 *  switch the operation buttons(user page) to friend buttons(friend page)
		 */
		ViewFriend.setOnAction(new EventHandler<ActionEvent>() {
			String S;

			public void handle(ActionEvent e) {
				try {
					if (mgr.getCentralPerson() != null) {
						List<String> updated = FriendList.getFriends(mgr, selectedUser);
						mgr.centralize(selectedUser); // set central user
						
						// update friends information
						if (updated == null) {
							obl.clear();
						} else {
							obl.clear();
							obl.addAll(updated);
						}

						FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
								ViewFriend, Menu);
						// update the viewers 
						result.setText(" [Prompt] : Friends of " + selectedUser + " are shown in follow viewer.");
						order.setText(Integer.toString(mgr.order()));
						size.setText((Integer.toString(mgr.size())));
						connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
						friendsofcent.setText(Integer.toString(updated.size()));
					
						//update button accessibility
						ViewFriend.setDisable(true);
						RemoveFriend.setDisable(false);
						RemoveSelectedFriend.setDisable(true);
						RemoveAllFriend.setDisable(false);
						AddFriend.setDisable(false);
						
						//update the network viewer
						centralUserNtwk.getChildren().clear();
						centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
					}

				} catch (Exception nfe) {
//					
				}
			}
		});

		/**
		 *  handle event for the "AddUser" button
		 */
		AddUser.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e)

			{

				try {

					// create a text input dialog and set information
					TextInputDialog td1 = new TextInputDialog();
					td1.setTitle("Add a user");
					td1.setHeaderText("Please Enter Name");
					td1.setContentText("Name:");
					//set the OK button disable
					td1.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
					
					// if the friend list of central user is not null 
					if(mgr.getPersonalNetwork(mgr.getCentralPerson())!=null) {
						// if the input text is not empty or the input user's name is not existed, set the OK button valid.
						td1.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
						td1.getDialogPane().lookupButton(ButtonType.OK).setDisable
						(newValue.trim().isEmpty()||mgr.getPersonalNetwork(mgr.getCentralPerson()).contains(newValue));
						});
					}
					else {
						td1.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
							td1.getDialogPane().lookupButton(ButtonType.OK).setDisable
							(newValue.trim().isEmpty());
				        });
					}
					

					Optional<String> choice1 = td1.showAndWait();

					// if the OK button is clicked
					if (choice1.isPresent()) {
						
						if ((td1.getEditor().getText()) != null) {
							// if the added user is existed, pop a alert dialog
							if(mgr.getAllUsers().contains(td1.getEditor().getText())) {
								Alert alert1 = new Alert(AlertType.WARNING);
								alert1.setTitle("Warning Dialog");
								alert1.setHeaderText("Warning messager");
								alert1.setContentText("User Already Exists!");
								alert1.showAndWait();

							}else {
								mgr.addPerson(td1.getEditor().getText());
							}							
						}

							// update friends information
							List<String> updated = new ArrayList<String>();							
							Set<String> updatedSet = mgr.getAllUsers();
							for (String item : updatedSet) {
									updated.add(item);
							}
							
							// update the viewers
							obl.clear();
							obl.addAll(updated);
							result.setText( td1.getEditor().getText() + " is added.");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							if (mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" ")) {
								friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
							}
							
						}	
						
					} catch (IllegalCharacterException nfe1) {
						//if a Illegal Charater Entered, pop an alert dialog
						Alert alert1 = new Alert(AlertType.WARNING);
						alert1.setTitle("Warning Dialog");
						alert1.setHeaderText("Warning messager");
						alert1.setContentText("Illegal Charater Entered!");
						alert1.showAndWait();
					}
				
				catch (Exception nfe) {
					result.setText(" [Prompt] : invalid input");

				}

			}

		});

		/**
		 *  add all buttons to operation vbox
		 */
		operation.getChildren().add(title);
		operation.getChildren().add(Import);
		operation.getChildren().add(Export);
		operation.getChildren().add(AddFriendship);
		operation.getChildren().add(RemoveAllUsers);
		operation.getChildren().add(ViewFriend);
		operation.getChildren().add(AddUser);
		operation.getChildren().add(DeleteUser);
		operation.getChildren().add(MutualFriends);
		operation.getChildren().add(ShortestPath);

		
        	/**
		 *  handle event for the "AddFriendship" button
		 */
		AddFriendship.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e)
			{
				try {
					 //Create the custom dialog.
			        Dialog<Pair<String, String>> dialog = new Dialog<>();
			        dialog.setTitle("Add new friendship(two users)");
			        dialog.setHeaderText("Please Enter Names");			        
					ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

					// Create the name1 and name2 labels and fields.
					GridPane grid = new GridPane();
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setPadding(new Insets(20, 150, 10, 10));
					
					//create textfield
					TextField name1 = new TextField();
					name1.setPromptText("Name1");
					TextField name2 = new TextField();
					name2.setPromptText("Name2");

					grid.add(new Label("Name1:"), 0, 0);
					grid.add(name1, 1, 0);
					grid.add(new Label("Name2:"), 0, 1);
					grid.add(name2, 1, 1);

					// Enable/Disable login button depending on whether a name1 was entered.
					Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
					loginButton.setDisable(true);

					// Do some validation (using the Java 8 lambda syntax).
					name1.textProperty().addListener((observable, oldValue, newValue) -> {
					    loginButton.setDisable(newValue.trim().isEmpty());
					});
					
					name2.textProperty().addListener((observable, oldValue, newValue) -> {
					    loginButton.setDisable(newValue.trim().isEmpty());
					});

					dialog.getDialogPane().setContent(grid);

					// Convert the result to a name1-name2-pair when the login button is clicked.
					dialog.setResultConverter(dialogButton -> {
					    if (dialogButton == loginButtonType) {
					        return new Pair<>(name1.getText(), name2.getText());
					    }
					    return null;
					});
					
					//pop window
					Optional<Pair<String, String>> choice = dialog.showAndWait();
					
					//determine whether input is valid
					choice.ifPresent(name1name2 -> {
						try {
							if(name1.getText() == null || name1.getText().equals("")) {
								Alert alert1 = new Alert(AlertType.WARNING);
								alert1.setTitle("Warning Dialog");
								alert1.setHeaderText("Warning messager");
								alert1.setContentText("Name 1 cannot be empty!");
								alert1.showAndWait();
							}else if(name2.getText() == null || name2.getText().equals("")) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Name 2 cannot be empty!");
								alert2.showAndWait();
							}else if(name1.getText().equals(name2.getText())) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Two names cannot be the same");
								alert2.showAndWait();
							}else if(mgr.getPersonalNetwork(name1.getText()) != null && mgr.getPersonalNetwork(name1.getText()).contains(name2.getText())) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Friendship already exists");
								alert2.showAndWait();
							}else {
								//set friendship and sync the network
								mgr.setFriendship(name1.getText(), name2.getText());
					    	 List<String> updated = new ArrayList<String>();       
						       Set<String> updatedSet = mgr.getAllUsers();
						       for (String item : updatedSet) {
						         updated.add(item);
						       }
						       obl.clear();
						       obl.addAll(updated);
							result.setText(" [Prompt] : Friendship between " + name1.getText()+ " and " +name2.getText()+" is added.");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							if(mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" "))
								friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
								centralUserNtwk.getChildren().clear();
								centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
							}
						}catch(IllegalCharacterException e1) {
							Alert alert1 = new Alert(AlertType.WARNING);
							alert1.setTitle("Warning Dialog");
							alert1.setHeaderText("Warning messager");
							alert1.setContentText("Illegal Charater Entered!");
							alert1.showAndWait();
						}catch(IllegalArgumentException e1) {
							result.setText(" [Prompt] : Friendship between " + name1.getText()+ " and " +name2.getText()+" is added.");
						}						
					});
				}
				catch (Exception nfe) {
//					nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		});
		
		/**
		 *  handle event for the "MutualFriends" button
		 */
		MutualFriends.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e)
			{
				try {
					 //Create the custom dialog.
					Dialog<Pair<String, String>> dialog = new Dialog<>();
					dialog.setTitle("Mutual friends");
					dialog.setHeaderText("Please Enter Names");

					// Set the icon (must be included in the project).
					//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

					// Set the button types.
					ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

					// Create the name1 and name2 labels and fields.
					GridPane grid = new GridPane();
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setPadding(new Insets(20, 150, 10, 10));

					//cretae textfield
					TextField name1 = new TextField();
					name1.setPromptText("Name1");
					TextField name2 = new TextField();
					name2.setPromptText("Name2");

					grid.add(new Label("Name1:"), 0, 0);
					grid.add(name1, 1, 0);
					grid.add(new Label("Name2:"), 0, 1);
					grid.add(name2, 1, 1);

					// Enable/Disable login button depending on whether a name1 was entered.
					Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
					loginButton.setDisable(true);

					// Do some validation (using the Java 8 lambda syntax).
					name1.textProperty().addListener((observable, oldValue, newValue) -> {
					    loginButton.setDisable(newValue.trim().isEmpty());
					});

					dialog.getDialogPane().setContent(grid);

					// Convert the result to a name1-name2-pair when the login button is clicked.
					dialog.setResultConverter(dialogButton -> {
					    if (dialogButton == loginButtonType) {
					        return new Pair<>(name1.getText(), name2.getText());
					    }
					    return null;
					});
					
					//pop window
					Optional<Pair<String, String>> choice = dialog.showAndWait();
					
					//check input validity
					choice.ifPresent(name1name2 -> {
						 try {
								if(name1.getText() == null || name1.getText().equals("")) {
									Alert alert1 = new Alert(AlertType.WARNING);
									alert1.setTitle("Warning Dialog");
									alert1.setHeaderText("Warning messager");
									alert1.setContentText("Name 1 cannot be empty!");
									alert1.showAndWait();
								}else if(name2.getText() == null || name2.getText().equals("")) {
									Alert alert2 = new Alert(AlertType.WARNING);
									alert2.setTitle("Warning Dialog");
									alert2.setHeaderText("Warning messager");
									alert2.setContentText("Name 2 cannot be empty!");
									alert2.showAndWait();
								}else if(name1.getText().equals(name2.getText())) {
									Alert alert2 = new Alert(AlertType.WARNING);
									alert2.setTitle("Warning Dialog");
									alert2.setHeaderText("Warning messager");
									alert2.setContentText("Two names cannot be the same");
									alert2.showAndWait();
								}else if(!mgr.getAllUsers().contains(name1.getText()) 
										||!mgr.getAllUsers().contains(name2.getText()) ) {
									Alert alert2 = new Alert(AlertType.WARNING);
									alert2.setTitle("Warning Dialog");
									alert2.setHeaderText("Warning messager");
									alert2.setContentText("Users are not in the network");
									alert2.showAndWait();
								}else {
									//search for mutual friends and display
									List<String> updated = mgr.mutualFriends(name1.getText(), name2.getText());
									Alert alert = new Alert(AlertType.INFORMATION);
								      alert.setTitle("Mutual Friends");
								      alert.setHeaderText("Mutual Friends");
								      String output = String.join(" ", updated);
								      alert.setContentText(output);
								      alert.showAndWait();
									result.setText(" [Prompt] : The mutual friends of" + name1.getText()+ " and " +name2.getText()+" are shown on left.");
									order.setText(Integer.toString(mgr.order()));
									size.setText((Integer.toString(mgr.size())));
									connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
									if(mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" "))
										friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
								}
							}catch(IllegalCharacterException e1) {
								Alert alert1 = new Alert(AlertType.WARNING);
								alert1.setTitle("Warning Dialog");
								alert1.setHeaderText("Warning messager");
								alert1.setContentText("Illegal Charater Entered!");
								alert1.showAndWait();
							}				
					});
					}
				catch (Exception nfe) {
//					nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		});
		
		
		/**
		 *  handle event for the "ShortestPath" button
		 */
		ShortestPath.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					// Create the custom dialog.
					Dialog<Pair<String, String>> dialog = new Dialog<>();
					dialog.setTitle("Shortest friendship path");
					dialog.setHeaderText("Please Enter Names");

					ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

					// Create the name1 and name2 labels and fields.
					GridPane grid = new GridPane();
					grid.setHgap(10);
					grid.setVgap(10);
					grid.setPadding(new Insets(20, 150, 10, 10));

					TextField name1 = new TextField();
					name1.setPromptText("Name1");
					TextField name2 = new TextField();
					name2.setPromptText("Name2");

					grid.add(new Label("From:"), 0, 0);
					grid.add(name1, 1, 0);
					grid.add(new Label("To:"), 0, 1);
					grid.add(name2, 1, 1);

					// Enable/Disable login button depending on whether a name1 was entered.
					Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
					loginButton.setDisable(true);

					// Do some validation (using the Java 8 lambda syntax).
					name1.textProperty().addListener((observable, oldValue, newValue) -> {
						loginButton.setDisable(newValue.trim().isEmpty());
					});

					dialog.getDialogPane().setContent(grid);

					// Convert the result to a name1-name2-pair when the login button is clicked.
					dialog.setResultConverter(dialogButton -> {
						if (dialogButton == loginButtonType) {
							return new Pair<>(name1.getText(), name2.getText());
						}
						return null;
					});

					//pop window
					Optional<Pair<String, String>> choice = dialog.showAndWait();

					//check input validity
					choice.ifPresent(name1name2 -> {
						try {
							if (name1.getText() == null || name1.getText().equals("")) {
								Alert alert1 = new Alert(AlertType.WARNING);
								alert1.setTitle("Warning Dialog");
								alert1.setHeaderText("Warning messager");
								alert1.setContentText("Name 1 cannot be empty!");
								alert1.showAndWait();
							} else if (name2.getText() == null || name2.getText().equals("")) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Name 2 cannot be empty!");
								alert2.showAndWait();
							} else if (name1.getText().equals(name2.getText())) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Two names cannot be the same");
								alert2.showAndWait();
							} else if (!mgr.getAllUsers().contains(name1.getText())
									|| !mgr.getAllUsers().contains(name2.getText())) {
								Alert alert2 = new Alert(AlertType.WARNING);
								alert2.setTitle("Warning Dialog");
								alert2.setHeaderText("Warning messager");
								alert2.setContentText("Users are not in the network");
								alert2.showAndWait();
							} else {
								//search for shortest path and display
								List<String> updated = mgr.shortestPath(name1.getText(), name2.getText());
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Shortest Path");
								alert.setHeaderText("Shortest Path");
								String output = String.join("->", updated);
								alert.setContentText(output);
								alert.showAndWait();
						
								result.setText(" [Prompt] : The shortest path between " + name1.getText() + " and "
										+ name2.getText() + " are shown on left.");
								order.setText(Integer.toString(mgr.order()));
								size.setText((Integer.toString(mgr.size())));
								connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
								if (mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" "))
									friendsofcent.setText(Integer
											.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
							}
						} catch (IllegalCharacterException e1) {
							Alert alert1 = new Alert(AlertType.WARNING);
							alert1.setTitle("Warning Dialog");
							alert1.setHeaderText("Warning messager");
							alert1.setContentText("Illegal Charater Entered!");
							alert1.showAndWait();
						}
					});
				}
				catch (Exception nfe) {
					// nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		});
		
		/**
		 *  handle event for the "AddFriend" button
		 */
		AddFriend.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e)

			{
				// create a text input dialog and set information
				TextInputDialog td = new TextInputDialog();
				try {

					td.setTitle("Add a friend");
					td.setHeaderText("Please Enter Name");
					td.setContentText("Name:");
					
					//set OK button disable
					td.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
					
					// if the input text is not empty or the input user's name is not existed, set the OK button valid.
					td.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
						td.getDialogPane().lookupButton(ButtonType.OK).setDisable
						(newValue.trim().isEmpty()||mgr.getPersonalNetwork(mgr.getCentralPerson()).contains(newValue));
			        });

					Optional<String> choice2 = td.showAndWait();

					//if OK button is clicked
					if (choice2.isPresent()) {
						
						//if the input is empty or the added friend is existed, pop a alert dialog
						if(td.getEditor().getText() == null || td.getEditor().getText().equals("")) {
						       Alert alert1 = new Alert(AlertType.WARNING);
						       alert1.setTitle("Warning Dialog");
						       alert1.setHeaderText("Warning messager");
						       alert1.setContentText("Name cannot be empty!");
						       alert1.showAndWait();
						      }else if(td.getEditor().getText().equals(mgr.getCentralPerson())) {
						       Alert alert2 = new Alert(AlertType.WARNING);
						       alert2.setTitle("Warning Dialog");
						       alert2.setHeaderText("Warning messager");
						       alert2.setContentText("Name cannot be the same as central user");
						       alert2.showAndWait();
						      }else {
						       mgr.setFriendship(mgr.getCentralPerson(), td.getEditor().getText());
						      }  
						
						 //if a central user is existed, update the information and viewers
						 if (mgr.getCentralPerson() != null) {
						       List<String> updated = FriendList.getFriends(mgr, mgr.getCentralPerson());
						       
						       // update friends information
						       if (updated == null) {
						        obl.clear();
						       } else {
						        obl.clear();
						        obl.addAll(updated);
						       }
						       
						       //reset the status of button 
						       FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
						         ViewFriend, Menu);
						       
						       //update the viewers
						       result.setText(td.getEditor().getText() + " becomes a new friend of " + mgr.getCentralPerson() +" .");
						       order.setText(Integer.toString(mgr.order()));
						       size.setText((Integer.toString(mgr.size())));
						       connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
						       friendsofcent.setText(Integer.toString(updated.size()));
						       
						       //update the network viewer
						       centralUserNtwk.getChildren().clear();
						       centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
						      }
			
					}
				}catch (IllegalCharacterException nfe1) {
					Alert alert1 = new Alert(AlertType.WARNING);
					alert1.setTitle("Warning Dialog");
					alert1.setHeaderText("Warning messager");
					alert1.setContentText("Illegal Charater Entered!");
					alert1.showAndWait();
				}catch(IllegalArgumentException e1) {
					result.setText(td.getEditor().getText() + " becomes a new friend of " + mgr.getCentralPerson() +" .");
				}
				catch (Exception nfe) {

					//nfe.printStackTrace();

					result.setText(" [Prompt] : invalid input");

				}

			}

		});
		
		
		/**
		 *  handle event for the "RemoveFriend" button
		 */
		RemoveFriend.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e)

			{
				// create a text input dialog and set information
				TextInputDialog td = new TextInputDialog();
				try {
					
					td.setTitle("Remove a friend");
					td.setHeaderText("Please Enter Name");
					td.setContentText("Name:");
					td.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
					td.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
						td.getDialogPane().lookupButton(ButtonType.OK).setDisable
						(newValue.trim().isEmpty()||!mgr.getPersonalNetwork(mgr.getCentralPerson()).contains(newValue));
			        });

					Optional<String> choice = td.showAndWait();

					if (choice.isPresent()) {
						
						mgr.removeFriendship(mgr.getCentralPerson(), td.getEditor().getText());

						if (mgr.getCentralPerson() != null) {
							List<String> updated = FriendList.getFriends(mgr, mgr.getCentralPerson());
							
							// update friends information
							if (updated == null) {
								obl.clear();
							} else {
								obl.clear();
								obl.addAll(updated);
							}
							
							//update the viewers
							FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
									ViewFriend, Menu);
							result.setText(" [Prompt] : Friendship between " + td.getEditor().getText() + "and " + mgr.getCentralPerson() +"is deleted.");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							friendsofcent.setText(Integer.toString(updated.size()));
							centralUserNtwk.getChildren().clear();
							centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
						}

						
					}

				}catch(IllegalArgumentException e1) {
					// if IllegalArgumentException is caught, a prompted information will be dispalyed
					result.setText(" [Prompt] : Friendship between " + td.getEditor().getText() + " and " + mgr.getCentralPerson() +"is deleted.");
				}

				catch (Exception nfe) {

					result.setText(" [Prompt] : invalid input");

				}

			}

		});

		/**
		 *  handle event for the "RemoveSelectedFriend" button
		 */
		RemoveSelectedFriend.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e)
			{
				try {
						mgr.removeFriendship(mgr.getCentralPerson(), lv.getSelectionModel().getSelectedItem());
						if (mgr.getCentralPerson() != null) {
							List<String> updated = FriendList.getFriends(mgr, mgr.getCentralPerson());
							// update friends information
							if (updated == null) {
								obl.clear();
							} else {
								obl.clear();
								obl.addAll(updated);
							}
							//keep at friend page
							FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
									ViewFriend, Menu);
						

							result.setText(" [Prompt] : Friendship between " +  selectedUser + " and " + mgr.getCentralPerson() +" is deleted.");
							order.setText(Integer.toString(mgr.order()));
							size.setText((Integer.toString(mgr.size())));
							connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
							friendsofcent.setText(Integer.toString(updated.size()));
							centralUserNtwk.getChildren().clear();
							centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
					}

				}catch(IllegalArgumentException e1) {
					result.setText(" [Prompt] : Friendship between " +  selectedUser + " and " + mgr.getCentralPerson() +" is deleted.");
				}
				catch (Exception nfe) {
//					nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		});

		/**
		 *  handle event for the "RemoveAllFriend" button
		 */
		RemoveAllFriend.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e)
			{
				try {
					//delete all friends
					 String delete = mgr.getCentralPerson();
	                    List<String> deleteList = mgr.getPersonalNetwork(delete);
	                    for (int i = deleteList.size()-1; i >=0;  i--) {
	                    	mgr.removeFriendship(delete, deleteList.get(i));
	                    }
	                    //update listview
	                    obl.clear();
	                    result.setText(" [Prompt] : All friendships of " + mgr.getCentralPerson() + " are deleted.");
						order.setText(Integer.toString(mgr.order()));
						size.setText((Integer.toString(mgr.size())));
						connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
						friendsofcent.setText(Integer.toString(0));
						centralUserNtwk.getChildren().clear();
						centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
				}catch(IllegalArgumentException e1) {
					result.setText(" [Prompt] : All friendships of " + mgr.getCentralPerson() + " are deleted.");
				}

				catch (Exception nfe) {
					//nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");

				}
			}
		});


		/**
		 *  handle event for the "Menu" button
		 */
		Menu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					List<String> updated = new ArrayList<String>();
					Set<String> updatedSet = mgr.getAllUsers();
					for (String item : updatedSet) {
						updated.add(item);
					}
					// update users information
					obl.clear();
					obl.addAll(updated);
					result.setText(" [Prompt] : You are now at user page (Menu)");
					order.setText(Integer.toString(mgr.order()));
					size.setText((Integer.toString(mgr.size())));
					connectedComponents.setText(Integer.toString(mgr.connectedComponents()));

					//change to user page
					FriendList.setAsUserOperation(operation, title, Import, Export, AddFriendship, RemoveAllUsers, ViewFriend, AddUser,
							DeleteUser,MutualFriends, ShortestPath);

					//update button accessibility
					ViewFriend.setDisable(true);
					DeleteUser.setDisable(true);
					Import.setDisable(false);
					Export.setDisable(false);
					AddFriendship.setDisable(false);
					RemoveAllUsers.setDisable(false);
					AddUser.setDisable(false);
				} catch (Exception nfe) {
//					nfe.printStackTrace();
//                		 result.setText(" [Prompt] : invalid input");
				}
			}
		});

		/**
		 *  handle event for the "Check" button
		 */
		EventHandler<ActionEvent> EventByButton = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					List<String> updated = FriendList.getFriends(mgr, input);
					// update friends information
					if (updated == null) {
						obl.clear();
						// set listview
						result.setText(" [Prompt] : Cannot Find: " + input.getText());
					} else {
						//update listview
						obl.clear();
						obl.addAll(updated);
						mgr.centralize(input.getText());
						result.setText(" [Prompt] : Friend List : " + mgr.getPersonalNetwork(input.getText()));
						order.setText(Integer.toString(mgr.order()));
						size.setText((Integer.toString(mgr.size())));
						connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
						if(mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" "))
							friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
					}
					
				} catch (Exception nfe) {
					//nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		};

		/**
		 *  handle event for the search text field by clicking Enter
		 */
		EventHandler<ActionEvent> EventByEnter = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					List<String> updated = FriendList.getFriends(mgr, input);
					// update friends information
					if (updated == null) {
						obl.clear();
						//set listview
						result.setText(" [Prompt] : Cannot Find: " + input.getText());
					} else {
						obl.clear();
						obl.addAll(updated);
						mgr.centralize(input.getText());
						// set listview
						result.setText(" [Prompt] : Friend List : " + mgr.getPersonalNetwork(input.getText()));
						order.setText(Integer.toString(mgr.order()));
						size.setText((Integer.toString(mgr.size())));
						connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
						if(mgr.getCentralPerson() != null  && !mgr.getCentralPerson().equals(" "))
							friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
					}			
				} catch (Exception nfe) {
					//nfe.printStackTrace();
//					result.setText(" [Prompt] : invalid input");
				}
			}
		};

		// event of clicking a friend in the list
		EventHandler<MouseEvent> EventByMouse = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// for test purpose
				if(lv.getSelectionModel().getSelectedItems() != null) {
					if(lv.getSelectionModel().getSelectedItems().size()>1) {
						result.setText(" [Prompt] : Multiple users selected.");
						//FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
							//	ViewFriend, Back, Menu, Recall,Undo,Redo);
						ViewFriend.setDisable(true);
						Import.setDisable(true);
						Export.setDisable(true);
						RemoveAllUsers.setDisable(true);
						AddUser.setDisable(true);
						//button in friend page
						RemoveFriend.setDisable(false);
						RemoveSelectedFriend.setDisable(false);
						RemoveAllFriend.setDisable(true);
						AddFriend.setDisable(true);
						
					}
					else if(lv.getSelectionModel().getSelectedItems().size()==0) {
//					FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
//								ViewFriend, Back, Menu, Recall);
						ViewFriend.setDisable(true);
						DeleteUser.setDisable(true);
						Import.setDisable(false);
						Export.setDisable(false);
						RemoveAllUsers.setDisable(true);
						AddUser.setDisable(false);
						//button in friend page
						RemoveFriend.setDisable(false);
						RemoveSelectedFriend.setDisable(false);
						RemoveAllFriend.setDisable(true);
						AddFriend.setDisable(true);
					}
					else {
						//FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
						//		ViewFriend, Back, Menu, Recall,Undo,Redo);
					selectedUser = lv.getSelectionModel().getSelectedItem();
					result.setText(" [Prompt] : " + selectedUser + " is selected.");
					order.setText(Integer.toString(mgr.order()));
					size.setText((Integer.toString(mgr.size())));
					connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
					
				
//					System.out.println("clicked on " + lv.getSelectionModel().getSelectedItem());
					
					//update button accessibility
					//button in user page
					ViewFriend.setDisable(false);
					DeleteUser.setDisable(false);
					Import.setDisable(true);
					Export.setDisable(true);
					AddFriendship.setDisable(true);
					RemoveAllUsers.setDisable(true);
					AddUser.setDisable(true);
					//button in friend page
					RemoveFriend.setDisable(true);
					RemoveSelectedFriend.setDisable(false);
					RemoveAllFriend.setDisable(true);
					AddFriend.setDisable(true);
				}}

			}
		};

		// event of clicking a friend in the list
		EventHandler<MouseEvent> EventByMouse2 = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if(!obl.isEmpty()) {
					
					lv.getSelectionModel().clearSelection();
					selectedUser = null;
						//update button accessibility
						//button in user page
						ViewFriend.setDisable(true);
						DeleteUser.setDisable(true);
						Import.setDisable(false);
						Export.setDisable(false);
						AddFriendship.setDisable(false);
						RemoveAllUsers.setDisable(false);
						AddUser.setDisable(false);
//						//button in friend page
						RemoveFriend.setDisable(false);
						RemoveAllFriend.setDisable(false);
						AddFriend.setDisable(false);
						RemoveSelectedFriend.setDisable(true);
						Menu.setDisable(false);
						
//					
				}
				
				

			}
		};
		
		// handle the event for the input vbox
		input.setOnAction(EventByEnter);
		// handle the event for the "check" button
		rotateButton.setOnAction(EventByButton);
		// handle the event for the choose a user in the observable list
		lv.setOnMouseClicked(EventByMouse);

		// add interface to the top vbox
		vbox.getChildren().add(prompt);
		vbox.getChildren().add(input);
		vbox.getChildren().add(rotateButton);
		vbox.getChildren().add(result);
		
		// add interface to the central vbox
		VBox middle = new VBox();
		middle.getChildren().addAll(result, numbers, centralUserNtwk);
		
		// creat the whole border pane
		BorderPane root = new BorderPane();
		
		//set the layout
		root.setRight(operation);
		root.setTop(vbox);
		root.setLeft(lv);
		root.setCenter(middle);
		root.setOnMouseClicked(EventByMouse2);
		
		// set scroll bar for friend list
		lv.setItems(obl);
		pane.prefWidthProperty().bind(lv.widthProperty());
		pane.prefHeightProperty().bind(lv.heightProperty());
		pane.setContent(lv);
		pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		Group group = new Group();
		group.getChildren().add(pane);

		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Add the stuff and set the primary stage
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		primaryStage.setMinWidth(700);
		primaryStage.setMinHeight(700);
	        primaryStage.show();


			/**
 			*  handle event for the exit dialog
 			*/
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent event) {
	            	//display alert
	                Alert closeConfirmation = new Alert(
	                        Alert.AlertType.CONFIRMATION,
	                        "Are you sure you want to exit?"
	                );
	                //set exit without saving button
	                Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
	                        ButtonType.OK
	                );
	                exitButton.setText("Exit Without Saving");
	                closeConfirmation.setHeaderText("Confirm Exit");
	                closeConfirmation.initModality(Modality.APPLICATION_MODAL);
	                
	              //set exit with saving button
	                Button exitSaveButton = (Button) closeConfirmation.getDialogPane().lookupButton(
	                        ButtonType.CANCEL
	                );
	                exitSaveButton.setText("Save and Exit");
	                
	                //event for save and exit button
	                exitSaveButton.setOnAction(new EventHandler<ActionEvent>() {
	        			public void handle(ActionEvent e) {	        				
	        		        
	        	            FileChooser fileChooser = new FileChooser();
	        	            
	        	            //Set extension filter for text files
	        	            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	        	            fileChooser.getExtensionFilters().add(extFilter);
	        	 
	        	            //Show save file dialog
	        	            File file = fileChooser.showSaveDialog(primaryStage);
	        	 
	        	            if (file != null) {
	        	        		try {	
	        	        	    	mgr.saveLog(file);
	        	        	    } catch (IOException ex) {
	        	        	    	//intentionally blank
	        	        	    }
	        	            }
	        			}
	        		}
	                
	        );
	                //check for user response
	                Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
	                if (ButtonType.OK.equals(closeResponse.get()) || ButtonType.CANCEL.equals(closeResponse.get())) {
	                }
	                else {
	                    event.consume();
	                }                 
	            }
	        });
	}
	
	
	/**
	 * Create file "log.txt" in the root folder
	 * Contains the log of all the operations occurred during
	 * the running of the social network app
	 * @throws IOException 
	 */
	@Override
	public void stop() throws IOException {
   	 	File file = new File("log.txt");
   	 	file.createNewFile();
		try {	
	    	mgr.saveLog(file);
	    } catch (IOException ex) {
	    	System.out.println("write log IOException");
	    }
	}
	
	
	//text circle - represents user nodes
	public class UserNode extends StackPane{
	    private final Circle circle;
	    private final Text text;

	    UserNode(String text, double r)
	    {            
	        this.text = new Text(text);
	        circle = new Circle (r);
	        if(r == 70.0) {
	        	circle.setFill(Color.ORANGE);
	        }else {
	        	circle.setFill(Color.AQUA);
	        }
	        
	        getChildren().addAll(circle, this.text);
	    }
	}
	
	//modify the Gridpane passed in based on a social network manager
	//The radius of the friend network (the distance between the central user to each friend) is predefined as 4
	//Plots the friend network for central user defined by the SocialNetworkManager mgr
	//Circle node radius is predefined as 30
	public GridPane plotCentralUserNtwk(GridPane centralUserNtwk,SocialNetworkManager mgr) {
		//the radius of the friends circle
		//NOT THE NODE RADIUS
		int friendCircleR = 4;
		int nodeSize = 30;
		int cols = 11;
		int rows = 11;
		//stores a list of friend nodes of the central user
		List<UserNode> friendNodes = new ArrayList<UserNode>();
		//stores the central user specified by the manager
		String centralUser = mgr.getCentralPerson();
		//get the central user's friends
		List<String> friends = mgr.getPersonalNetwork(centralUser);
		
		//stores the coordinates of each friend on the GridPane
		List<Integer> X = new ArrayList<Integer>();
		List<Integer> Y = new ArrayList<Integer>();
		

		//friend list size
		int n=0;
		//If the central user has no friends
		if(friends==null) {
			//If there is a central users
			if(centralUser!=null && !centralUser.equals(" "))
				centralUserNtwk.add(new UserNode(centralUser,50), cols/2, rows/2);
			return centralUserNtwk;
		}
		
		//Otherwise there are friends
		n=friends.size();
		//the X and Y coordiantes of the central user
		//half of the number of the columns
		//which is the same with half of the number of rows
		//since there are same number of rows and columns
		int centerCoord = cols/2;

		
		//Calculate the coordiantes of each friends based on the rules:
		//each friend will lies on a circle whose radius is friendCircleR
		//and all friends will be spaced evenly
		for(int i=0;i<friends.size();i++) {
			int x = (int) (centerCoord+friendCircleR*Math.cos(2*Math.PI*i/n));
			int y = (int) (centerCoord+friendCircleR*Math.sin(2*Math.PI*i/n));
			X.add(x);
			Y.add(y);
		}
		
		
		//for each friend
		for(int i=0;i<friends.size();i++) {
			//create a texted node representing this friend
			friendNodes.add(new UserNode(friends.get(i),30));
			//stores a reference to the newly created node
			UserNode thisUser = friendNodes.get(i); 
			//Give each friend node an eventhandler
			//so when the mouse clicks on one friend
			//that friend becomes the new central user
			thisUser.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
				
				thisUser.circle.setFill(Color.AZURE);
				String newCentralUser = thisUser.text.getText();
				mgr.centralize(newCentralUser);
				centralUserNtwk.getChildren().clear();
				try {
					List<String> updated = FriendList.getFriends(mgr, newCentralUser);
					// update friends information
					if (updated == null) {
						obl.clear();
					} else {
						obl.clear();
						obl.addAll(updated);
					}
					FriendList.setAsFriendOperation(operation, title, AddFriend, RemoveFriend, RemoveSelectedFriend, RemoveAllFriend,
							ViewFriend, Menu);
					result.setText(" [Prompt] : Friends of " + newCentralUser + " are shown in follow viewer.");
					order.setText(Integer.toString(mgr.order()));
					size.setText((Integer.toString(mgr.size())));
					connectedComponents.setText(Integer.toString(mgr.connectedComponents()));
					if(mgr.getCentralPerson() != null && !mgr.getCentralPerson().equals(" "))
						friendsofcent.setText(Integer.toString(FriendList.getFriends(mgr, mgr.getCentralPerson()).size()));
					centralUserNtwk.getChildren().add(plotCentralUserNtwk(centralUserNtwk,mgr));
					
				}catch (Exception ex){
					//intentionally blank
				}
			});
			//add that friend to the appropriate position of the network
			centralUserNtwk.add(thisUser, X.get(i),Y.get(i));
		}
		//add the central user to the center of the gridpane
		centralUserNtwk.add(new UserNode(centralUser, 70), cols/2, rows/2);
		//We DON'T need an event handler for the central user
		return centralUserNtwk;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
