package com.application;

@SuppressWarnings("unused")
public class Population {

    private Long id;
    private String name;
    private int population;

    public Population() {
    }

    public Population(Long id, String city, int population) {
        this.id = id;
        this.name = city;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPopulation() { return this.population;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopulation(int population){  this.population = population;}


}