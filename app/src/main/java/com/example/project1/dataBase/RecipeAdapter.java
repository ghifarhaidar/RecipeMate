package com.example.project1.dataBase;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ViewRecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context context;
    private RecipeDatabaseHelper dbHelper;



    private List<Recipe> recipeList = new ArrayList<>();


    public RecipeAdapter(Context context,List<Recipe> recipeList ) {
        this.context = context;
        dbHelper = new RecipeDatabaseHelper(context);

        this.recipeList = recipeList;

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecipeList(List<Recipe> filteredList){
        this.recipeList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_layout ,  parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getRecipeName());
        holder.recipeCategory.setText(recipe.getCategory());
        holder.recipeCuisine.setText(recipe.getCuisine());
        holder.recipeRate.setText(String.valueOf(recipe.getRating()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRecipeActivity.class);
                intent.putExtra("recipeId", recipe.getId());
                context.startActivity(intent);;
            }
        });
        holder.addToShoppingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        context,
                        recipe.getRecipeName() + " added to shopping cart",
                        Toast.LENGTH_SHORT
                ).show();
                List<Ingredient> ingredientslist = dbHelper.getIngredientsList(recipe.getId());
                dbHelper.addRecipeIngredientsToShoppingCart(ingredientslist);

            }
        });


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName , recipeCategory , recipeCuisine , recipeRate;
        private FloatingActionButton addToShoppingCartButton ;
        private ImageView imageView ;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCategory= itemView.findViewById(R.id.category);
            recipeCuisine = itemView.findViewById(R.id.cuisine);
            recipeRate = itemView.findViewById(R.id.Rating);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.recipe_image);
            addToShoppingCartButton = itemView.findViewById(R.id.addToShoppingCart);

        }
    }
}

