package com.example.eznotes.models;

import java.util.List;

public class NotesModel {

    private String Name;
    private String date;
    private List<String> notes;
    private String title;

    public NotesModel(String name, String date, List<String> notes, String title) {
        this.Name = name;
        this.date = date;
        this.notes = notes;
        this.title = title;
    }

    public NotesModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
