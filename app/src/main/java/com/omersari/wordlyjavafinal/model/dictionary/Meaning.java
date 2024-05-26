package com.omersari.wordlyjavafinal.model.dictionary;

import java.util.List;

public class Meaning {
    private String partOfSpeech;
    private List<Definition> definitions;

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    private List<String> synonyms;
    private List<String> antonyms;

    public String getPartOfSpeech() { return partOfSpeech; }
    public void setPartOfSpeech(String value) { this.partOfSpeech = value; }

    public List<Definition> getDefinitions() { return definitions; }
    public void setDefinitions(List<Definition> value) { this.definitions = value; }
}
