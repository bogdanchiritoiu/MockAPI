package com.mock.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "generated_data")
public class GeneratedData
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "internalId")
    private int internalId;


    @Column(name = "data")
    private String data;

    public GeneratedData()
    {
        //nothing
    }

    public GeneratedData(String endpoint, String data)
    {
        this.endpoint = endpoint;
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public int getInternalId()
    {
        return internalId;
    }

    public void setInternalId(int internalId)
    {
        this.internalId = internalId;
    }
}
