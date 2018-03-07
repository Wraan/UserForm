package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    private Button exitButton;

    @FXML
    private Button addToListButton;

    @FXML
    private Label ageErrorLabel;

    @FXML
    private Label lastNameErrorLabel;

    @FXML
    private Label firstNameErrorLabel;

    @FXML
    private TextField ageTextF;

    @FXML
    private TextField lastNameTextF;

    @FXML
    private TextField firstNameTextF;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private ListView<String> list;

    @FXML
    private Label lastNameLabel;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    public void initialize() {
        readFromFile();

    }
    void readFromFile(){
        try(BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String line = br.readLine();

            while (line != null) {
                list.getItems().add(line);
                line = br.readLine();
            }
        }catch (Exception e){
            System.out.println("Nie znaleziono pliku");
        }
    }

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
        firstNameTextF.clear();
        lastNameTextF.clear();
        ageTextF.clear();
    }

    private void clearErrors(){
        ageErrorLabel.setText("");
        lastNameErrorLabel.setText("");
        firstNameErrorLabel.setText("");
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
            if(!tf.getText().trim().equals("")) {
                if (tf.getId().equals("firstNameTextF"))
                    lastNameTextF.requestFocus();
                if (tf.getId().equals("lastNameTextF"))
                    ageTextF.requestFocus();
            }
            else
                textFieldEmptyError(tf);
        }
    }

    private void textFieldEmptyError(TextField tf) {
        if (tf.getId().equals("firstNameTextF"))
            firstNameErrorLabel.setText("Pole nie może być puste!");

        if (tf.getId().equals("nameTextF"))
            lastNameErrorLabel.setText("Pole nie może być puste!");

    }

    private boolean checkForWhitespaces(StringBuilder sb, TextField tf){
        if(!tf.getText().trim().equals("")) {
            sb.append(tf.getText().trim());
            sb.append(" ");
            return true;
        }
        else {
            textFieldEmptyError(tf);

            return false;
        }
    }
    @FXML
    void addRecordToList() {
        clearErrors();

        String userAdd = getUserFromFields();
        if(userAdd == null)
            return;

        if(!list.getItems().contains(userAdd)) {
            list.getItems().add(list.getItems().size(), userAdd);
            clearFields();
            firstNameTextF.requestFocus();
            list.getSelectionModel().clearSelection();

        }
        else {
            firstNameErrorLabel.setText("Już jest na liscie");
            clearFields();
            firstNameTextF.requestFocus();
        }

    }

    @FXML
    void deleteRecordFromList() {
        list.getItems().remove(list.getSelectionModel().getSelectedItem());
        list.getSelectionModel().clearSelection();
    }

    @FXML
    void exitApplication() {
        saveToFile();
        Platform.exit();
    }

    @FXML
    void moveToTextFields(){
        if(list.getSelectionModel().getSelectedItem() == null)
            return;

        String text = list.getSelectionModel().getSelectedItem().toString();
        List<String> list;
        list = Arrays.asList(text.split(" "));

        firstNameTextF.setText(list.get(0));
        lastNameTextF.setText(list.get(1));
        ageTextF.setText(list.get(2));

    }

    @FXML
    void editRecordFromList(){
        clearErrors();

        String userAdd = getUserFromFields();
        if(userAdd == null)
            return;

        if(!list.getItems().contains(userAdd)) {
            if(list.getSelectionModel().getSelectedIndex() != -1)
                list.getItems().set(list.getSelectionModel().getSelectedIndex(), userAdd);
            else
                list.getItems().add(userAdd);

            clearFields();
            firstNameTextF.requestFocus();
            list.getSelectionModel().clearSelection();
        }
        else {
            firstNameErrorLabel.setText("Już jest na liscie");
            clearFields();
            firstNameTextF.requestFocus();
        }
    }
    private String getUserFromFields(){
        StringBuilder sb = new StringBuilder();

        if(!checkForWhitespaces(sb,firstNameTextF))
            return null;

        if(!checkForWhitespaces(sb,lastNameTextF))
            return null;

        int age = -1;
        try{
            age = Integer.parseInt(ageTextF.getText());
        } catch(NumberFormatException e) {
            ageErrorLabel.setText("Podano niepoprawny wiek!");
            ageTextF.setText("");
        }
        if(!validAge(age)){
            return null;
        }
        sb.append(Integer.toString(age));
        return sb.toString();

    }
    @FXML
    void saveToFile(){
        List<String> stringList = list.getItems().subList(0, this.list.getItems().size());
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("records.txt");
            for(String s: stringList){
                writer.println(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.close();


    }

}
