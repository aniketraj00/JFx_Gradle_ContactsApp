package com.aniket.contacts;

import com.aniket.contacts.datamodel.ContactData;
import com.aniket.contacts.datamodel.ContactItem;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainWindow;
    @FXML
    private TableView<ContactItem> mainWindowTableView;

    public void initialize() {
        this.mainWindowTableView.setItems(ContactData.getInstance().getContacts());
    }

    @FXML
    public void addItem() {
        Dialog<ButtonType> addContactDialog = new Dialog<>();
        addContactDialog.initOwner(this.mainWindow.getScene().getWindow());
        addContactDialog.setTitle("New");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addContact.fxml"));
        try {
            addContactDialog.setDialogPane(loader.load());
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
        addContactDialog.setHeaderText("Add a New Contact");
        addContactDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        addContactDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> userResponse = addContactDialog.showAndWait();
        if(userResponse.isPresent() && userResponse.get() == ButtonType.OK) {
            AddContactController contactController = loader.getController();
            ContactItem item = contactController.getNewContact();
            if(item != null) {
                ContactData.getInstance().getContacts().add(item);
            }
        }
    }

    @FXML
    public void editItem() {
        Dialog<ButtonType> editContactDialog = new Dialog<>();
        editContactDialog.initOwner(this.mainWindow.getScene().getWindow());
        editContactDialog.setTitle("Edit");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addContact.fxml"));
        try {
            editContactDialog.setDialogPane(loader.load());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        editContactDialog.getDialogPane().setHeaderText("Update an Existing Contact");
        editContactDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editContactDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        if(this.mainWindowTableView.getItems().isEmpty()) {
            alert.setHeaderText("Nothing to edit. Table is empty !");
            alert.show();
        }else {
            ContactItem selected = this.mainWindowTableView.getSelectionModel().getSelectedItem();
            int selectedIndex = this.mainWindowTableView.getItems().indexOf(selected);
            if(selected == null) {
                alert.setHeaderText("Select a Contact first !");
                alert.show();
            }else {
                AddContactController contactController = loader.getController();
                contactController.populateExistingContactData(selected);
                Optional<ButtonType> userResponse = editContactDialog.showAndWait();
                if(userResponse.isPresent() && userResponse.get() == ButtonType.OK){
                    ContactItem updated = contactController.updateExistingContact();
                    this.mainWindowTableView.getItems().set(selectedIndex, updated);
                }
            }
        }
    }

    @FXML
    public void deleteItem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        if(this.mainWindowTableView.getItems().isEmpty()) {
            alert.setHeaderText("Nothing to delete. Table is Empty!");
            alert.show();
        }else {
            ContactItem selected = this.mainWindowTableView.getSelectionModel().getSelectedItem();
            if(selected == null) {
                alert.setHeaderText("Select a Contact first !");
                alert.show();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("Do you want to delete this Contact ?");
                Optional<ButtonType> userResponse = alert.showAndWait();
                if(userResponse.isPresent() && userResponse.get() == ButtonType.OK){
                    this.mainWindowTableView.getItems().remove(selected);
                }
            }
        }
    }

    @FXML
    public void searchItem(){
        Dialog<ButtonType> searchContactDialog = new Dialog<>();
        searchContactDialog.initOwner(this.mainWindow.getScene().getWindow());
        searchContactDialog.setTitle("Search");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchContact.fxml"));
        try {
            searchContactDialog.setDialogPane(loader.load());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        searchContactDialog.getDialogPane().setHeaderText("Search an Existing Contact");
        searchContactDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        searchContactDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> userResponse = searchContactDialog.showAndWait();
        if(userResponse.isPresent() && userResponse.get() == ButtonType.OK){

        }
    }

    @FXML
    public void quit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Want to quit the application ?");
        Optional<ButtonType> userResponse = alert.showAndWait();
        if (userResponse.isPresent() && userResponse.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

}
