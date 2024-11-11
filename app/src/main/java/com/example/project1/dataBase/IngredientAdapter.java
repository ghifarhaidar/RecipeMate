package com.example.project1.dataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ingredientViewHolder>{

    private List<Ingredient> ingredientlist = new ArrayList<>();
    private Context context ;
    boolean editable = true;

    public List<Ingredient> getIngredientlist() {
        return ingredientlist;
    }

    public IngredientAdapter(Context context, List<Ingredient> ingredientlist) {
        this.ingredientlist = ingredientlist;
        this.context = context;
        notifyDataSetChanged();
    }
    public IngredientAdapter(Context context, List<Ingredient> ingredientlist,boolean editable) {
        this.ingredientlist = ingredientlist;
        this.context = context;
        this.editable = editable;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setIngredientList(List<Ingredient> ingredientlist){
        this.ingredientlist = ingredientlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ingredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.ingredient_layout, parent, false);
        return new IngredientAdapter.ingredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ingredientViewHolder holder, int position) {

        Ingredient ingredient  = ingredientlist.get(position);

        holder.ingredientAmount.setText(String.valueOf(ingredient.getAmount()) +"("+ingredient.getUnit()+")");
        if(!editable){
            holder.ingredientAmount.setEnabled(false);
        }
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    String[] newAmount  = String.valueOf(holder.ingredientAmount.getText()).split(Pattern.quote("("));
                    String s = newAmount[0];
                    ingredient.setAmount( Float.valueOf(s));
                    return true;
                }
                holder.ingredientAmount.setText(String.valueOf(ingredient.getAmount()) +"("+ingredient.getUnit()+")");
                holder.ingredientName.setText(ingredient.getName());

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return ingredientlist.size();
    }

    public static class ingredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientName ;
        EditText ingredientAmount;
        public ingredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient);
            ingredientAmount = itemView.findViewById(R.id.ingredientAmount);

        }
    }
}
