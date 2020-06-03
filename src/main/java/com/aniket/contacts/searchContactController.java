package com.aniket.contacts;

import com.aniket.contacts.datamodel.ContactData;
import com.aniket.contacts.datamodel.ContactItem;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.function.Predicate;


public class searchContactController {

    @FXML
    private TableView<ContactItem> searchTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> filterChoiceBox;

    private FilteredList<ContactItem> filteredList = new FilteredList<>(ContactData.getInstance().getContacts());
    private Predicate<ContactItem> FILTER_OFF;

    public void initialize() {
        this.FILTER_OFF = item -> true;
        this.filteredList.setPredicate(this.FILTER_OFF);
        this.searchTableView.setItems(this.filteredList);
        this.searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> this.search(newValue)));
    }

    public void search(String param) {
        if(param.isEmpty()) {
            this.filteredList.setPredicate(this.FILTER_OFF);
        }else {
            this.filteredList.setPredicate(this.getFilter(this.filterChoiceBox.getSelectionModel().getSelectedIndex(), param));
        }
    }

    private Predicate<ContactItem> getFilter(int choiceParam, String searchParam) {
        return item -> {
            if(choiceParam == 0) {
                return item.getName().contains(searchParam);
            } else if(choiceParam == 1) {
                return item.getPhoneNo().contains(searchParam);
            } else if(choiceParam == 2) {
                return item.getEmail().contains(searchParam);
            } else {
                return item.getAddress().contains(searchParam);
            }
        };
    }
}
