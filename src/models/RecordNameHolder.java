/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Mohammed
 */
public class RecordNameHolder {

    private RecordNameHolder() {
    }

    private RecordName recordName;

    private final static RecordNameHolder INSTANCE = new RecordNameHolder();

    public static RecordNameHolder getInstance() {
        return INSTANCE;
    }

    public void setRecordName(RecordName recordName) {
        this.recordName = recordName;
    }


    public RecordName getRecordName() {
        return this.recordName;
    }

    public void clearOPlayer() {
        this.recordName = null;
    }
}
