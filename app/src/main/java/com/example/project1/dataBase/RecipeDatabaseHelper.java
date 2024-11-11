package com.example.project1.dataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "recipe_database";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    private static final String TABLE_SHOPPING_CART = "shopping_cart";




    // Recipes table columns
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_RECIPE_NAME = "recipe_name";
    private static final String KEY_RATING = "rating";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CUISINE = "cuisine";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_SERVING_SIZE = "serving_size";
    private static final String KEY_STEPS = "steps";

    // Ingredients table columns
    private static final String KEY_INGREDIENT_ID = "ingredient_id";
    private static final String KEY_INGREDIENT_NAME = "ingredient_name";
    private static final String KEY_UNIT = "unit";

    // RecipeIngredients table columns
    private static final String KEY_AMOUNT = "amount";

    // Constructor
    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating recipes table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPE_NAME + " TEXT,"
                + KEY_RATING + " INTEGER,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_CUISINE + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_SERVING_SIZE + " INTEGER,"
                + KEY_STEPS + " TEXT"
                + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        // Creating ingredients table
        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_INGREDIENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_INGREDIENT_NAME + " TEXT,"
                + KEY_UNIT + " TEXT"
                + ")";
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        // Creating recipe_ingredients table
        String CREATE_RECIPE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + KEY_RECIPE_ID + " INTEGER,"
                + KEY_INGREDIENT_ID + " INTEGER,"
                + KEY_AMOUNT + " REAL,"
                + "PRIMARY KEY(" + KEY_RECIPE_ID + ", " + KEY_INGREDIENT_ID + "),"
                + "FOREIGN KEY(" + KEY_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + KEY_RECIPE_ID + "),"
                + "FOREIGN KEY(" + KEY_INGREDIENT_ID + ") REFERENCES " + TABLE_INGREDIENTS + "(" + KEY_INGREDIENT_ID + ")"
                + ")";
        db.execSQL(CREATE_RECIPE_INGREDIENTS_TABLE);

        // Creating shopping_cart table
        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART + "("
                + KEY_INGREDIENT_ID + " INTEGER PRIMARY KEY,"
                + KEY_AMOUNT + " REAL,"
                + "FOREIGN KEY(" + KEY_INGREDIENT_ID + ") REFERENCES " + TABLE_INGREDIENTS + "(" + KEY_INGREDIENT_ID + ")"
                + ")";
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
    }

    // Upgrading database (if needed)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART);

        // Create tables again
        onCreate(db);
    }

    // Function to create an ID for a table
    public long  createId(String tableName , String keyId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the current maximum ID from the table
        String query = "SELECT MAX(" + keyId + ") FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);
        long currentMaxId = (cursor != null && cursor.moveToFirst()) ? cursor.getLong(0) : 0;

        // Increment the current maximum ID to get a new ID
        long newId = currentMaxId + 1;
        return  newId;
    }

    // Function to add a recipe to the database
    public void addRecipe(@NonNull Recipe recipe , List<Ingredient> recipeIngredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        long id =  createId(TABLE_RECIPES , KEY_RECIPE_ID);
        values.put(KEY_RECIPE_ID , id );
        values.put(KEY_RECIPE_NAME, recipe.getRecipeName());
        values.put(KEY_RATING, recipe.getRating());
        values.put(KEY_CATEGORY, recipe.getCategory());
        values.put(KEY_CUISINE, recipe.getCuisine());
        values.put(KEY_IMAGE_PATH, recipe.getImagePath());
        values.put(KEY_SERVING_SIZE, recipe.getServingSize());
        values.put(KEY_STEPS, recipe.getSteps());
        db.insert(TABLE_RECIPES, null, values);
        addIngredientsToRecipe(id, recipeIngredient);
        db.close();
    }

    // Function to add ingredients to a specific recipe
    public void addIngredientsToRecipe(long recipeId, List<Ingredient> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(KEY_RECIPE_ID, recipeId);
            values.put(KEY_INGREDIENT_ID, ingredient.getIngredientId());
            values.put(KEY_AMOUNT, ingredient.getAmount());

            db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
        }

        db.close();
    }

    // Function to add the ingredients of a recipe to the shopping cart
    @SuppressLint("Range")
    public void addRecipeIngredientsToShoppingCart(List<Ingredient> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(KEY_INGREDIENT_ID, ingredient.getIngredientId());
            values.put(KEY_AMOUNT, ingredient.getAmount());

            db.insert(TABLE_SHOPPING_CART, null, values);
        }

        db.close();
    }

    // Function to delete all ingredients with status true
    public void deleteBoughtIngredients(Set<Long> ingredientIdToDelete) {
        if(ingredientIdToDelete == null){
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        // Convert the set of ingredient IDs to a comma-separated string
        String ingredientIds = TextUtils.join(",", ingredientIdToDelete);

        // Delete the specified ingredients from the shopping cart
        String query = "DELETE FROM " + TABLE_SHOPPING_CART +
                " WHERE " + KEY_INGREDIENT_ID + " IN (" + ingredientIds + ")";

        db.execSQL(query);
        db.close();
    }

    // Function to retrieve recipes based on category, cuisine, and sort status
    public List<Recipe> getFilteredRecipes(String recipeName, String category, String cuisine, boolean sort) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_RECIPES;


        query += " WHERE ";

        query += KEY_RECIPE_NAME + " LIKE '%" + recipeName + "%'";

        if ( category != null || cuisine != null) {
            query += " AND ";
            if (category != null) {
                query += KEY_CATEGORY + " = '" + category + "'";
                if (cuisine != null) {
                    query += " AND ";
                }
            }
            if (cuisine != null) {
                query += KEY_CUISINE + " = '" + cuisine + "'";
            }
        }

        if (sort) {
            query += " ORDER BY " + KEY_RATING + " DESC";
        }

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndex(KEY_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_RECIPE_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_RATING)),
                        cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(KEY_CUISINE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SERVING_SIZE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndex(KEY_STEPS))
                );
                recipeList.add(recipe);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return recipeList;
    }

    public List<Ingredient> getIngredientsList() {
        List<Ingredient> ingredientsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_INGREDIENTS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Ingredient ingredient = new Ingredient(
                        cursor.getInt(cursor.getColumnIndex(KEY_INGREDIENT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_UNIT)),
                        1
                );
                ingredientsList.add(ingredient);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return ingredientsList;
    }
    public List<Ingredient> getIngredientsList(long recipeId) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + TABLE_INGREDIENTS + "." + KEY_INGREDIENT_ID + ", " +
                TABLE_INGREDIENTS + "." + KEY_INGREDIENT_NAME + ", " +
                TABLE_INGREDIENTS + "." + KEY_UNIT + ", " +
                TABLE_RECIPE_INGREDIENTS + "." + KEY_AMOUNT +
                " FROM " + TABLE_INGREDIENTS +
                " INNER JOIN " + TABLE_RECIPE_INGREDIENTS +
                " ON " + TABLE_INGREDIENTS + "." + KEY_INGREDIENT_ID + " = " +
                TABLE_RECIPE_INGREDIENTS + "." + KEY_INGREDIENT_ID +
                " WHERE " + TABLE_RECIPE_INGREDIENTS + "." + KEY_RECIPE_ID + " = " + recipeId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Ingredient ingredient = new Ingredient(
                        cursor.getInt(cursor.getColumnIndex(KEY_INGREDIENT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_UNIT)),
                        cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                );
                ingredientsList.add(ingredient);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return ingredientsList;
    }
    @SuppressLint("Range")
    public Recipe getRecipe(long recipeId) {
        Recipe recipe= null ;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RECIPES + " WHERE "+ KEY_RECIPE_ID + " = " + recipeId ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe(
                    cursor.getLong(cursor.getColumnIndex(KEY_RECIPE_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_RECIPE_NAME)),
                    cursor.getInt(cursor.getColumnIndex(KEY_RATING)),
                    cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(KEY_CUISINE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_SERVING_SIZE)),
                    cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
                    cursor.getString(cursor.getColumnIndex(KEY_STEPS))
            );
            cursor.close();
            return recipe;

        }
        return recipe;
    }
    public List<Ingredient> getShoppingCartIngredients() {
        List<Ingredient> shoppingCartIngredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                TABLE_SHOPPING_CART + "." + KEY_INGREDIENT_ID + ", " +
                TABLE_INGREDIENTS + "." + KEY_INGREDIENT_NAME + ", " +
                TABLE_INGREDIENTS + "." + KEY_UNIT + ", " +
                TABLE_SHOPPING_CART + "." + KEY_AMOUNT +
                " FROM " + TABLE_SHOPPING_CART +
                " INNER JOIN " + TABLE_INGREDIENTS +
                " ON " + TABLE_SHOPPING_CART + "." + KEY_INGREDIENT_ID + " = " +
                TABLE_INGREDIENTS + "." + KEY_INGREDIENT_ID;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Ingredient ingredient = new Ingredient(
                        cursor.getInt(cursor.getColumnIndex(KEY_INGREDIENT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_UNIT)),
                        cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                );
                shoppingCartIngredients.add(ingredient);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return shoppingCartIngredients;
    }
    public void addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT_ID , createId(TABLE_INGREDIENTS , KEY_INGREDIENT_ID));
        values.put(KEY_INGREDIENT_NAME, ingredient.getName());
        values.put(KEY_UNIT,ingredient.getUnit());

        db.insert(TABLE_INGREDIENTS, null, values);
        db.close();
    }
    public void deleteCourse() {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SHOPPING_CART,null,null);
        db.delete(TABLE_RECIPE_INGREDIENTS,null,null);
        db.delete(TABLE_INGREDIENTS,null,null);
        db.delete(TABLE_RECIPES,null,null);
        db.close();
    }
    public void deleteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_RECIPE_ID + " = ?",
                new String[] { String.valueOf(recipeId) });
        db.close();
    }



}
