package com.sabirov.client;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages", indices = {@Index(value = {"ID"}, unique = true)})
public class Message {
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name="Text1")
    private String text1;

    @ColumnInfo(name = "Text2")
    private String text2;

    @ColumnInfo(name = "Text3")
    private String text3;

    int getID() {
        return ID;
    }

    void setID(int ID) {
        this.ID = ID;
    }

    String getText1() {
        return text1;
    }

    void setText1(String text1) {
        this.text1 = text1;
    }

    String getText2() {
        return text2;
    }

    void setText2(String text2) {
        this.text2 = text2;
    }

    String getText3() {
        return text3;
    }

    void setText3(String text3) {
        this.text3 = text3;
    }
}
