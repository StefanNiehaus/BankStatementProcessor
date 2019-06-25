package com.niehaus.modal;

public class Entry {
    private String sourceStatement;
    private String Type;
    private String mainCategory;
    private String subCategory;
    private String description;
    private String amount;
    private String date;
    private String debit;
    private String explanation;

    public Entry() {}

    Entry(Entry entry) {
        setSourceStatement(entry.getSourceStatement());
        setType(entry.getType());
        setMainCategory(entry.getMainCategory());
        setSubCategory(entry.getSubCategory());
        setDescription(entry.getDescription());
        setAmount(entry.getAmount());
        setDate(entry.getDate());
        setDebit(entry.getDebit());
        setExplanation(entry.getExplanation());
    }

    public String getSourceStatement() {
        return sourceStatement;
    }

    public void setSourceStatement(String sourceStatement) {
        this.sourceStatement = sourceStatement;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
