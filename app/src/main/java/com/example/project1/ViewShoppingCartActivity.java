package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project1.dataBase.Ingredient;
import com.example.project1.dataBase.RecipeDatabaseHelper;
import com.example.project1.dataBase.ShoppingCartAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ViewShoppingCartActivity extends AppCompatActivity {
    private List<Ingredient> shoppingCartIngredient = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShoppingCartAdapter shoppingCartAdapter ;
    private RecipeDatabaseHelper dbHelper ;
    private Button done ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_cart);
        dbHelper = new RecipeDatabaseHelper(this);
        try {  shoppingCartIngredient = dbHelper.getShoppingCartIngredients();
        }catch ( Exception e){
        Toast.makeText(this, "there is no shopping list", Toast.LENGTH_SHORT).show();
        }
        recyclerView = findViewById(R.id.shoppingCartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingCartAdapter = new ShoppingCartAdapter(this, shoppingCartIngredient);
        recyclerView.setAdapter( shoppingCartAdapter);

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Long> delete = shoppingCartAdapter.getDeleting();

                dbHelper.deleteBoughtIngredients(delete);
                Intent intent = new Intent( ViewShoppingCartActivity.this , MainActivity.class ) ;
                startActivity(intent);

            }
        });

    }


}