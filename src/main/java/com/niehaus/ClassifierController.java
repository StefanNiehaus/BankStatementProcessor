package com.niehaus;

import com.niehaus.modal.Datasource;
import com.niehaus.modal.Entry;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ClassifierController {

    @FXML private ChoiceBox<String> TypeEntry;
    @FXML private ChoiceBox<String> MainCategoryEntry;
    @FXML private ChoiceBox<String> SubCategoryEntry;

    @FXML private ObservableList<Object> TypeEntryList;
    @FXML private ObservableList<Object> MainCategoryEntryList;
    @FXML private ObservableList<Object> SubCategoryEntryList;

    @FXML Text statementNumberText;
    @FXML Text sourceText;
    @FXML Text dateText;
    @FXML Text amountText;
    @FXML Text explanationText;

    @FXML TextArea descriptionText;

    @FXML TextField addTypeText;
    @FXML TextField addMainCategoryText;
    @FXML TextField addSubCategoryText;
    @FXML TextField addIdentifierText;

    @FXML Button addIdentifierButton;
    @FXML Button classifyButton;
    @FXML Button addTypeButton;
    @FXML Button addMainCategoryButton;
    @FXML Button addSubCategoryButton;

    @FXML RadioButton skipClassifiedEntriesButton;

    private static final int INDEX_COLUMN_SOURCE = 0;
    private static final int INDEX_COLUMN_DATE = 1;
    private static final int INDEX_COLUMN_DESCRIPTION = 2;
    private static final int INDEX_COLUMN_AMOUNT = 3;
    private static final int INDEX_COLUMN_EXPLANATION = 4;

    private BufferedReader statementFile = null;
    private Datasource datasource;
    private Entry currentEntry;
    private int statementNo = 0;

    @FXML public void initialize() {
        datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource");
        }
        addIdentifierButton.setDisable(true);
    }

    void loadBankStatement(String bankStatementPath) {
        /* Load the first line of the bank statement and display to user
         * and check if it can be classified based on Datasource table*/
        try {
            this.statementFile = new BufferedReader(new FileReader(bankStatementPath));
            /* source, date, description, amount, explanation*/
            statementFile.readLine();
            loadEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML protected void handleClassifyButtonAction() {
        if (TypeEntry.getValue() != null &&
                MainCategoryEntry.getValue() != null &&
                SubCategoryEntry.getValue() != null) {
            currentEntry.setType(TypeEntry.getValue());
            currentEntry.setMainCategory(MainCategoryEntry.getValue());
            currentEntry.setSubCategory(SubCategoryEntry.getValue());
            datasource.writeEntry(currentEntry);
            addIdentifierText.clear();
            loadEntry();
        }
    }

    @FXML protected void handleTypeEntryChange() {
        if (TypeEntry.getValue() != null) {
            populateMainCategoryList(TypeEntry.getValue());
            SubCategoryEntryList.clear();
        }
    }

    @FXML protected void handleMainCategoryEntryChange() {
        if (MainCategoryEntry.getValue() != null) {
            populateSubCategoryList(MainCategoryEntry.getValue());
        }
    }

    @FXML protected void handleSubCategoryEntryChange() {}

    @FXML public void handleKeyReleased() {
        String text = addIdentifierText.getText();
        boolean disableButtons = text.isBlank() ||
                TypeEntry.getValue() == null ||
                MainCategoryEntry.getValue() == null ||
                SubCategoryEntry.getValue() == null;
        addIdentifierButton.setDisable(disableButtons);
    }

    @FXML protected void copyDescriptionToID() {
        String s = descriptionText.getSelectedText();
        if (s != null && !s.isBlank()) {
            addIdentifierText.setText(s);
            addIdentifierButton.setDisable(false);
        }
    }

    @FXML protected void searchType() {
        System.out.println("Searching for matching Types");
    }

    @FXML protected void searchMainCategory() {
        System.out.println("Searching for matching MainC");
    }

    @FXML protected void searchSubCategory() {
        System.out.println("Searching for matching SubC");
    }

    @FXML protected void handleAddIdentifierButtonAction() {
        currentEntry.setType(TypeEntry.getValue().trim());
        currentEntry.setMainCategory(MainCategoryEntry.getValue().trim());
        currentEntry.setSubCategory(SubCategoryEntry.getValue().trim());
        datasource.addIdentifier(currentEntry, addIdentifierText.getText().trim());
        addIdentifierText.clear();
        addIdentifierButton.setDisable(true);
    }

    @FXML protected void handleAddTypeButton() {
        if (!addTypeText.getText().isBlank()) {
            TypeEntryList.add(0, addTypeText.getText());
            TypeEntry.setValue(addTypeText.getText());
            addTypeText.clear();
        }
    }

    @FXML protected void handleAddMainCategoryButton() {
        if (!addMainCategoryText.getText().isBlank()) {
            MainCategoryEntryList.add(0, addMainCategoryText.getText());
            MainCategoryEntry.setValue(addMainCategoryText.getText());
            addMainCategoryText.clear();
        }
    }

    @FXML protected void handleAddSubCategoryButton() {
        if (!addSubCategoryText.getText().isBlank()) {
            SubCategoryEntryList.add(0, addSubCategoryText.getText());
            SubCategoryEntry.setValue(addSubCategoryText.getText());
            addSubCategoryText.clear();
        }
    }

    private void setEntryFields(Entry entry) {
        statementNumberText.setText(Integer.toString(statementNo));
        sourceText.setText(entry.getSourceStatement());
        dateText.setText(entry.getDate());
        descriptionText.setText(entry.getDescription());
        amountText.setText(entry.getAmount());
        explanationText.setText(entry.getExplanation());
    }

    private void populateTypeCategoryList() {
        TypeEntryList.clear();
        List<String> types = datasource.getEntryTypeList();
        TypeEntryList.addAll(types);
    }

    private void populateMainCategoryList(String type) {
        MainCategoryEntryList.clear();
        List<String> mainCategories = datasource.getEntryMainCategoryList(type);
        MainCategoryEntryList.addAll(mainCategories);
    }

    private void populateSubCategoryList(String mainCategory) {
        SubCategoryEntryList.clear();
        List<String> subCategories = datasource.getEntrySubCategoryList(mainCategory);
        SubCategoryEntryList.addAll(subCategories);
    }

    private void loadEntry() {
        try {
            String currLine = statementFile.readLine();
            if (currLine != null) {
                statementNo++;

                // set all fields in Entry object
                String[] entryRaw = currLine.split(",");
                Entry entry = new Entry();
                entry.setAmount(entryRaw[INDEX_COLUMN_AMOUNT].trim());
                entry.setDate(entryRaw[INDEX_COLUMN_DATE].trim());
                entry.setDescription(entryRaw[INDEX_COLUMN_DESCRIPTION].trim());
                entry.setDebit(Double.parseDouble(entryRaw[INDEX_COLUMN_AMOUNT].trim()) > 0.0 ? "Credit" : "Debit");
                entry.setExplanation(entryRaw.length > INDEX_COLUMN_EXPLANATION ? entryRaw[INDEX_COLUMN_EXPLANATION].trim() : "");
                entry.setSourceStatement(entryRaw[INDEX_COLUMN_SOURCE].trim());
                this.currentEntry = entry;

                // set fields for user
                setEntryFields(entry);

                // autoclassify
                autoClassify(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void autoClassify(Entry entry) {
        List<Entry> possibleMatches = datasource.getClassification(entry);
        if (!possibleMatches.isEmpty()) {
            boolean uniqueClassification = true;
            for (Entry e : possibleMatches) {
                if (!e.sameClassification(possibleMatches.get(0))) {
                    uniqueClassification = false;
                }
            }
            Entry e = possibleMatches.get(0);
            /* Populate Lists of options*/
            populateTypeCategoryList();
            populateMainCategoryList(e.getType());
            populateSubCategoryList(e.getMainCategory());

            /* Set values based on best match */
            TypeEntry.setValue(e.getType());
            MainCategoryEntry.setValue(e.getMainCategory());
            SubCategoryEntry.setValue(e.getSubCategory());


            if (skipClassifiedEntriesButton.isSelected() && uniqueClassification) {
                handleClassifyButtonAction();
            }
        } else {
            populateTypeCategoryList();
            TypeEntry.valueProperty().set(null);
            MainCategoryEntryList.clear();
            SubCategoryEntryList.clear();
        }
    }

    private void close() {
        try {
            statementFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
