package com.hixel.hixel.service.models.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CompanyEntity {

    @PrimaryKey
    private String ticker;

    private String name;
    private String cik;

}
