package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class AllSavedDNA {
    List<SavedDNA> savedDNAs;

    public AllSavedDNA() {
        savedDNAs = new ArrayList<>();
    }

    public List<SavedDNA> getSavedDNAs() {
        return savedDNAs;
    }

    public void setSavedDNAs(List<SavedDNA> savedDNAs) {
        this.savedDNAs = savedDNAs;
    }
}

