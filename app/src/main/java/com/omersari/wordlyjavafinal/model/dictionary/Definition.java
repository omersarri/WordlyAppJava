package com.omersari.wordlyjavafinal.model.dictionary;

import java.util.List;

public class Definition {
    private List<Object> synonyms;
    private List<Object> antonyms;
    private String definition;
    private String example;

    public List<Object> getSynonyms() { return synonyms; }
    public void setSynonyms(List<Object> value) { this.synonyms = value; }

    public List<Object> getAntonyms() { return antonyms; }
    public void setAntonyms(List<Object> value) { this.antonyms = value; }

    public String getDefinition() { return definition; }
    public void setDefinition(String value) { this.definition = value; }

    public String getExample() { return example; }
    public void setExample(String value) { this.example = value; }
}
