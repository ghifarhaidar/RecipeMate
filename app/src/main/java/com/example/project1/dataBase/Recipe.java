package com.example.project1.dataBase;


public class Recipe  {
    private long id ;
    private String recipeName;
    private int rating;
    private String cuisine;
    private String category;
    private String imagePath;
    private int servingSize;



    private String steps;

    public Recipe(long id , String recipeName, int rating,String category , String cuisine,int servingSize, String imagePath
                  , String steps) {
        this.id = id ;
        this.recipeName = recipeName;
        this.rating = rating;
        this.category = category ;
        this.cuisine = cuisine;
        this.imagePath = imagePath;
        this.servingSize = servingSize;
        this.steps = steps;
    }
    public long getId() {
        return id;
    }
    public String getRecipeName() {
        return recipeName;
    }

    public int getRating() {
        return rating;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getServingSize() {
        return servingSize;
    }

    public String getSteps() {
        return steps;
    }

    public String getCategory() {
        return this.category ;
    }
}

