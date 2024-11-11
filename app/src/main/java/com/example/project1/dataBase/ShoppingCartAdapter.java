package com.example.project1.dataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ingredientViewHolder>{
    private Context context ;


    private List<Ingredient> ingredientlist = new ArrayList<>();
    private Set<Long> deleting = new HashSet<>();

    public ShoppingCartAdapter(Context context, List<Ingredient> ingredientlist ) {
        this.context = context;
        this.ingredientlist = ingredientlist;

    }
    @SuppressLint("NotifyDataSetChanged")
    public void setShoppingCartList(List<Ingredient> ingredientlist){
        this.ingredientlist = ingredientlist;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ingredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.shopping_cart_ingrediant, parent, false);
        return new ShoppingCartAdapter.ingredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ingredientViewHolder holder, int position) {
        Ingredient ingredient  = ingredientlist.get(position);
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientAmount.setText(ingredient.getAmount()+ "(" +String.valueOf(ingredient.getUnit())+")");
        holder.ingredientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleting.contains(ingredient.getIngredientId())){
                    deleting.remove(ingredient.getIngredientId());
                }
                else {
                    deleting.add(ingredient.getIngredientId());
                }
            }
        });

    }
    public Set<Long> getDeleting(){
        return deleting ;
    }

    @Override
    public int getItemCount() {
        return ingredientlist.size();
    }

    public static class ingredientViewHolder extends RecyclerView.ViewHolder{
        TextView  ingredientAmount;
        CheckBox ingredientName ;
        public ingredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientNameCheckBox);
            ingredientAmount = itemView.findViewById(R.id.ingredientAmount2);

        }
    }
}
