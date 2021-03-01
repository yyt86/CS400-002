package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 * This is a helper class that contains some operations of friend page
 * @author Ateam87
 *
 */
public class FriendList {
	/**
	 * get list of friend of a certain user(input)
	 * @param mgr
	 * @param input
	 * @return
	 */
	public static List<String> getFriends(SocialNetworkManager mgr, TextField input) {
		List<String> list = mgr.getPersonalNetwork(input.getText());
		return list;
	}
		
	/**
	 * get a list of friends of a certain user
	 * @param mgr
	 * @param input
	 * @return
	 */
	public static List<String> getFriends(SocialNetworkManager mgr, String input) {
		List<String> list = mgr.getPersonalNetwork(input);
		return list;
	}

//	Button MutualFriends = new Button("Mutual Friends");
//	Button ShortestPath = new Button("Shortest path between two");
/**
 * This method set the stage layout of the main page
 * @param operation
 * @param title
 * @param importButton
 * @param Export
 * @param AddFriendship
 * @param RemoveAllUsers
 * @param ViewFriend
 * @param AddUser
 * @param DeleteUser
 * @param MutualFriends
 * @param ShortestPath
 * @return
 */
    public static VBox setAsUserOperation(VBox operation, Label title, Button importButton, 
    		Button Export, Button AddFriendship, Button RemoveAllUsers, Button ViewFriend, Button AddUser, 
    		Button DeleteUser, Button MutualFriends, Button ShortestPath) {
    	operation.getChildren().clear();
        operation.getChildren().add(title);
        operation.getChildren().add(importButton);
        operation.getChildren().add(Export);
        operation.getChildren().add(AddFriendship);
        operation.getChildren().add(RemoveAllUsers);
        operation.getChildren().add(ViewFriend);
        operation.getChildren().add(AddUser);
        operation.getChildren().add(DeleteUser);
        operation.getChildren().add(MutualFriends);
        operation.getChildren().add(ShortestPath);

        importButton.setDisable(false);
        Export.setDisable(false);
        AddFriendship.setDisable(false);
        //reset the button
		RemoveAllUsers.setDisable(false);
		ViewFriend.setDisable(true);
		AddUser.setDisable(false);
		DeleteUser.setDisable(true);
		MutualFriends.setDisable(false);
		ShortestPath.setDisable(false);

		
    	return operation;
    }
	
/**
 * This method set the stage layout of the friend page
 * @param operation
 * @param title
 * @param AddFriend
 * @param RemoveFriend
 * @param RemoveSelectedFriend
 * @param RemoveAllFriend
 * @param ViewFriendship
 * @param Menu
 * @return
 */
    public static VBox setAsFriendOperation(VBox operation, Label title, Button AddFriend, 
    		Button RemoveFriend, Button RemoveSelectedFriend, Button RemoveAllFriend, Button ViewFriendship, 
    		  Button Menu) {
    	operation.getChildren().clear();
        operation.getChildren().add(title);
        operation.getChildren().add(AddFriend);
        operation.getChildren().add(RemoveFriend);
        operation.getChildren().add(RemoveSelectedFriend);
        operation.getChildren().add(RemoveAllFriend);
        operation.getChildren().add(ViewFriendship);
        operation.getChildren().add(Menu);
        

        //reset the button
        AddFriend.setDisable(false);
		RemoveFriend.setDisable(false);
		RemoveSelectedFriend.setDisable(true);
		RemoveAllFriend.setDisable(false);
		ViewFriendship.setDisable(true);
		Menu.setDisable(false);
    	return operation;
    }
	}
