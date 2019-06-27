package com.niehaus.modal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Entry {
    private static final String DATE_PATTERN = "yyyy/MM/dd";

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
        setDebit(entry.getCredit());
        setExplanation(entry.getExplanation());
    }

    public boolean sameClassification(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Entry)) {
            return false;
        }

        Entry c = (Entry) o;

        return (this.getType().equals(c.getType()) &&
                this.getMainCategory().equals(c.getMainCategory()) &&
                this.getSubCategory().equals(c.getSubCategory()));

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

    public void setDate(String date) { // TODO: Fix this date mess
        String pattern = "yyyyMMdd";
        try {
            Date d = new SimpleDateFormat(pattern).parse(date);
            this.date = new SimpleDateFormat(DATE_PATTERN).format(d);
        } catch (ParseException e) {
            this.date = date;
        }
    }

    public String getCredit() {
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
