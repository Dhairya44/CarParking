package com.andreasmarsh.SpringTest;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    @Column(name = "category", length = 30, unique = true)
    private String category;

    public Category() {

    }

    public Category(Long categoryID, String category) {
        this.categoryID = categoryID;
        this.category = category;
    }

    public Category(String category) {
        this.category = category;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){//overriding the toString() method
        return categoryID + " " + category;
    }
}
