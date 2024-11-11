package com.example.project1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.project1.dataBase.Ingredient;
import com.example.project1.dataBase.IngredientAdapter;
import com.example.project1.dataBase.Recipe;
import com.example.project1.dataBase.RecipeDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class addRecipeActivity extends AppCompatActivity {
    private ImageView imageView ;
    private Button addIngredientButton ,saveButton;
    private RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this);

    private List<Ingredient> recipeIngredientsList= new ArrayList<>() ;
    private List<Ingredient> ingredientsList= new ArrayList<>() ;
    private RecyclerView recyclerView;
    private EditText recipeNameText , cuisineText , categoryText,stepsText,servingText,ratingText;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);


        imageView = findViewById(R.id.addImage);
        imageView.setImageResource(R.drawable.recipe_image);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        saveButton= findViewById(R.id.saveButton);
        recipeNameText = findViewById(R.id.editRecipeName);
        cuisineText = findViewById(R.id.editCuisine);
        categoryText= findViewById(R.id.editCategory);
        stepsText= findViewById(R.id.editSteps);
        servingText= findViewById(R.id.editServing);
        ratingText= findViewById(R.id.editTextRating);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Recipe recipe = new Recipe(
                        1,
                        String.valueOf(recipeNameText.getText()),
                        Integer.parseInt(String.valueOf(ratingText.getText())),
                        String.valueOf(categoryText.getText()),
                        String.valueOf(cuisineText.getText()),
                        Integer.parseInt(String.valueOf(servingText.getText())),
                        "itttt",
                        String.valueOf(stepsText.getText())
                );
                dbHelper.addRecipe(recipe , ingredientAdapter.getIngredientlist() );
                Toast.makeText(
                        addRecipeActivity.this,
                        recipe.getRecipeName() + " added",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }

        });
        try {
            ingredientsList = dbHelper.getIngredientsList();}catch (Exception e){
        }
        recyclerView = findViewById(R.id.ViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new IngredientAdapter(this, recipeIngredientsList);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(addRecipeActivity.this, addIngredientButton);
                //Inflating the Popup using xml file

                for(int i = 0 ; i< ingredientsList.size();i++ ){


                    popup.getMenu().add(0,i,i ,ingredientsList.get(i).getName());
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(recipeIngredientsList.contains(ingredientsList.get(item.getOrder()))){
                            Toast.makeText(
                                    addRecipeActivity.this,
                                    item.getTitle() + " is already added",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else{
                            Toast.makeText(
                                    addRecipeActivity.this,
                                    "You added : " + item.getTitle(),
                                    Toast.LENGTH_SHORT
                            ).show();
                            recipeIngredientsList.add(ingredientsList.get(item.getOrder()));
                            ingredientAdapter.setIngredientList(recipeIngredientsList);
                            recyclerView.setAdapter( ingredientAdapter);
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
    }
}