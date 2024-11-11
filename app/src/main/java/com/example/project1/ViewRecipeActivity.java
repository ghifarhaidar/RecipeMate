package com.example.project1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.dataBase.Ingredient;
import com.example.project1.dataBase.IngredientAdapter;
import com.example.project1.dataBase.Recipe;
import com.example.project1.dataBase.RecipeDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {
    RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this);
    private ImageView imageView ;
    private IngredientAdapter ingredientAdapter ;
    private Button deleteButton , addtoShoppingCart ;

    private RecyclerView recyclerView;
    private List<Ingredient> recipeIngredients= new ArrayList<>();
    long recipeId;
    Recipe recipe;
    TextView recipeNameEditText,cuisineEditText,categoryEditText,stepsEditText,ratingEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        imageView = findViewById(R.id.addImage);
        imageView.setImageResource(R.drawable.recipe_image);

        recipeId = getIntent().getLongExtra("recipeId", 0L);

        recipe = dbHelper.getRecipe(recipeId);

        initRecipeView();
        initIngredients();
        deleteButton = findViewById(R.id.deleteRecipeButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlert(v);
            }



//            @Override
//            public void onClick(View v) {
//                Toast.makeText(
//                        ViewRecipeActivity.this,
//                        recipe.getRecipeName() + " deleted",
//                        Toast.LENGTH_SHORT
//                ).show();
//                dbHelper.deleteRecipe(recipeId);
//                finish();
//            }
        });
        addtoShoppingCart= findViewById(R.id.addtoShoppingCart);
        addtoShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        ViewRecipeActivity.this,
                        recipe.getRecipeName() + " added to shopping cart",
                        Toast.LENGTH_SHORT
                ).show();
                List<Ingredient> ingredientslist = dbHelper.getIngredientsList(recipeId);
                dbHelper.addRecipeIngredientsToShoppingCart(ingredientslist);
            }
        });


    }
    public  void deleteAlert(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure You want to delete thia recipe");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ViewRecipeActivity.this,recipe.getRecipeName() + " deleted",Toast.LENGTH_LONG).show();

                        dbHelper.deleteRecipe(recipeId);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void initRecipeView() {
        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        cuisineEditText = findViewById(R.id.cuisineEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        stepsEditText = findViewById(R.id.stepsEditText);
        ratingEditText = findViewById(R.id.ratingEditText);

        recipeNameEditText.setText(recipe.getRecipeName());
        cuisineEditText.setText(recipe.getCuisine());
        categoryEditText.setText(recipe.getCategory());
        stepsEditText.setText(recipe.getSteps());
        ratingEditText.setText(String.valueOf(recipe.getRating()));

    }

    private void initIngredients(){
        recipeIngredients = dbHelper.getIngredientsList(recipeId);
        recyclerView = findViewById(R.id.ViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new IngredientAdapter(this,recipeIngredients,false );
        recyclerView.setAdapter( ingredientAdapter);
        ingredientAdapter.setIngredientList(recipeIngredients);
    }
}