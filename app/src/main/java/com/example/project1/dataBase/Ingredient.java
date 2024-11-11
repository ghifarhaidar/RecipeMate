package com.example.project1.dataBase;

public class Ingredient {
        private long id;
        private String name;

        public void setAmount(float amount) {
                this.amount = amount;
        }

        private String unit;
        private float amount;
        public Ingredient(long id, String name, String unit, float amount) {
                this.id = id;
                this.name = name;
                this.unit = unit;
                this.amount = amount;
        }
        public Ingredient( String name, String unit) {
                this.name = name;
                this.unit = unit;
        }


        public String getName() {
                return name;
                }

        public String getUnit() {
                return unit;
                }

        public float getAmount() {
                return amount;
                }
        public long getIngredientId() {
                        return id;
                }
}
