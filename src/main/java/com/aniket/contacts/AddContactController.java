package com.aniket.contacts;

import com.aniket.contacts.datamodel.ContactItem;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;


public class AddContactController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextArea addressTextArea;
    @FXML
    private TextField notesTextField;


    public void initialize() {

    }

    public void populateExistingContactData(ContactItem existing) {
        this.nameTextField.setText(existing.getName());
        this.phoneTextField.setText(existing.getPhoneNo());
        this.emailTextField.setText(existing.getEmail());
        this.addressTextArea.setText(existing.getAddress());
        this.notesTextField.setText(existing.getNotes());
    }

    public Map<String, String> fetchDataFromTextFields(){
        Map<String, String> data = new HashMap<>();
        data.put("name", this.nameTextField.getText());
        data.put("phoneNo", this.phoneTextField.getText());
        data.put("email", this.emailTextField.getText());
        data.put("address", this.addressTextArea.getText());
        data.put("notes",this.notesTextField.getText());
        return data;
    }

    public ContactItem getNewContact(){
        Map<String, String> dataInput  = this.fetchDataFromTextFields();
        if(dataInput.get("name").isEmpty() || dataInput.get("phoneNo").isEmpty() || dataInput.get("email").isEmpty() || dataInput.get("address").isEmpty()){
            return null;
        }else{
            if(dataInput.get("address").length() > 50){
                return null;
            }else{
                return new ContactItem(dataInput.get("name"),dataInput.get("phoneNo"),dataInput.get("email"),dataInput.get("address"),dataInput.get("notes"));
            }
        }
    }

    public ContactItem updateExistingContact() {
        return this.getNewContact();
    }
}
