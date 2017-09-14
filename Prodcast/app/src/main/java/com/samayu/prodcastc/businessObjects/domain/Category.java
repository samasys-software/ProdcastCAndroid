package com.samayu.prodcastc.businessObjects.domain;

/**
 * Created by nandhini on 8/24/17.
 */

public class Category {

        public long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(long categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        private long categoryId;
        private String categoryName;


}
