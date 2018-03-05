package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class Controller {

    @FXML
    private Button exitButton;

    @FXML
    private Button addToListButton;

    @FXML
    private Label ageErrorLabel;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label fornameErrorLabel;

    @FXML
    private TextField ageTextF;

    @FXML
    private TextField nameTextF;

    @FXML
    private TextField fornameTextF;

    @FXML
    private Label fornameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private ListView<String> list;

    @FXML
    private Label nameLabel;

    @FXML
    private MenuItem deleteMenuItem;


    @FXML
    void checkIfNumber(KeyEvent key) {
        clearErrors();
        char c = key.getCharacter().charAt(0);

        if(!(Character.isDigit(c) || (c == Keys.DELETE) || (c == Keys.BACKSPACE) || (c == Keys.ENTER))){
            key.consume();
        }
        else if((ageTextF.getText().length() >= 2) && (c != Keys.DELETE) && (c != Keys.BACKSPACE) && (c != Keys.ENTER)){
            key.consume();
        }
        else if(c == Keys.ENTER)
            addRecordToList();


    }

    private boolean validAge(int age){
        if(age == -1)
            return false;

        if(age < 18) {
            ageErrorLabel.setText("Minimalny wiek to 18 lat");
            ageTextF.setText("");
            return false;
        }

        if(age > 70) {
            ageErrorLabel.setText("Maksymalny wiek to 70 lat");
            ageTextF.setText("");
            return false;
        }
        return true;
    }

    private void clearFields(){
        fornameTextF.clear();
        nameTextF.clear();
        ageTextF.clear();
    }

    private void clearErrors(){
        ageErrorLabel.setText("");
        nameErrorLabel.setText("");
        fornameErrorLabel.setText("");
    }

    @FXML
    void checkIfLetter(KeyEvent key) {
        clearErrors();

        TextField tf = (TextField) key.getSource();
        char c = key.getCharacter().charAt(0);

        if(!(Character.isLetter(c) || c == Keys.DELETE || c == Keys.BACKSPACE || c == Keys.ENTER || c == Keys.DASH || c == Keys.SPACE)){
            key.consume();
        }
        if(c == 13){
            if(tf.getId().equals("fornameTextF"))
                nameTextF.requestFocus();
            if(tf.getId().equals("nameTextF"))
                ageTextF.requestFocus();
        }
    }

    private boolean checkForWhitespaces(StringBuilder sb, TextField tf){
        if(!tf.getText().trim().equals("")) {
            sb.append(tf.getText().trim());
            sb.append(" ");
            return true;
        }
        else {
            if(tf.getId().equals("fornameTextF")) {
                fornameErrorLabel.setText("Pole nie może być puste!");
            }
            if(tf.getId().equals("nameTextF")) {
                nameErrorLabel.setText("Pole nie może być puste!");
            }

            return false;
        }
    }
    @FXML
    void addRecordToList() {
        clearErrors();
        StringBuilder sb = new StringBuilder();

        if(!checkForWhitespaces(sb,fornameTextF))
            return;

        if(!checkForWhitespaces(sb,nameTextF))
            return;

        int age = -1;
        try{
            age = Integer.parseInt(ageTextF.getText());
        } catch(NumberFormatException e) {
            ageErrorLabel.setText("Podano niepoprawny wiek!");
            ageTextF.setText("");
        }
        if(!validAge(age)){
            return;
        }
        sb.append(Integer.toString(age));

        list.getItems().add(list.getItems().size(), sb.toString());
        clearFields();
        fornameTextF.requestFocus();
    }

    @FXML
    void deleteRecordFromList() {
        list.getItems().remove(list.getSelectionModel().getSelectedItem());
    }

    @FXML
    void exitApplication() {
        Platform.exit();
    }

}
