package com.example.project1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.widget.SearchView;

import com.example.project1.dataBase.Ingredient;
import com.example.project1.dataBase.Recipe;
import com.example.project1.dataBase.RecipeAdapter;
import com.example.project1.dataBase.RecipeDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecipeDatabaseHelper dbHelper = new RecipeDatabaseHelper(this);

    private RecyclerView recyclerView;
    private SearchView searchView;
    private RecipeAdapter recipeAdapter ;
    private FloatingActionButton addRecipeButton , viewShoppingCartButton  ;

    private String searchText;
    private List<Recipe> filteredRecipes= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSplashScreen().setOnExitAnimationListener(splashScreenView -> {
            final ObjectAnimator slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.getHeight()
            );
            slideUp.setInterpolator(new AnticipateInterpolator());
            slideUp.setDuration(200L);

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    splashScreenView.remove();
                }
            });

            // Run your animation.
            slideUp.start();
        });

//        initDataBase();
        initSearchWidget();
        initButtons();

    }
    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerView();
    }
    private void refreshRecyclerView(){
        filteredRecipes = dbHelper.getFilteredRecipes("",null , null , true);
        recipeAdapter.setRecipeList(filteredRecipes);
    }

    private void initSearchWidget(){
        searchView = (SearchView)findViewById(R.id.search_view);
        searchView.clearFocus();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredRecipes = dbHelper.getFilteredRecipes("",null , null , true);
        recipeAdapter = new RecipeAdapter(this,filteredRecipes);
        recyclerView.setAdapter( recipeAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                filteredRecipes = dbHelper.getFilteredRecipes(searchText,null , null , true);
                recipeAdapter.setRecipeList(filteredRecipes);

                return true;
            }
        });
    }
    private void initButtons(){
        addRecipeButton=findViewById(R.id.addRecipe);
        viewShoppingCartButton=findViewById(R.id.viewShoppingCart);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MainActivity.this , addRecipeActivity.class ) ;
                startActivity(intent);
            }
        });
        viewShoppingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MainActivity.this , ViewShoppingCartActivity.class ) ;
                startActivity(intent);
            }
        });
    }
    private void deleteDatabase() {
        dbHelper.deleteCourse();
    }

    private void initDataBase(){
        deleteDatabase();
        dbHelper.addIngredient(new Ingredient("Salt" , "tbsp"));
        dbHelper.addIngredient(new Ingredient("Pepper" , "tbsp"));
        dbHelper.addIngredient(new Ingredient("Olive oil" , "tbsp"));
        dbHelper.addIngredient(new Ingredient("Garlic" , "Cloves"));
        dbHelper.addIngredient(new Ingredient("Onions" , "gram"));
        dbHelper.addIngredient(new Ingredient("Butter" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Flour" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Sugar" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Milk" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Lemon juice" , "Tablespoon"));
        dbHelper.addIngredient(new Ingredient("Herbs" , "Teaspoon"));
        dbHelper.addIngredient(new Ingredient("Spices" , "Teaspoon"));
        dbHelper.addIngredient(new Ingredient("Vinegar" , "Tablespoon"));
        dbHelper.addIngredient(new Ingredient("Soy sauce" , "tbsp"));
        dbHelper.addIngredient(new Ingredient("Worcestershire sauce" , "tbsp"));
        dbHelper.addIngredient(new Ingredient("Stock" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Rice" , "Cup"));
        dbHelper.addIngredient(new Ingredient("Pasta" , "Ounces"));

    }
}