package com.mock.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "generated_data")
public class GeneratedData
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "data")
    private String data;

    public GeneratedData()
    {
        //nothing
    }

    public GeneratedData(String data)
    {
        this.data = data;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
