// Dictionary.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.omersari.wordlyjavafinal.model.dictionary;
import java.util.List;

public class DictionaryItem {
    private String phonetic;
    private String origin;
    private List<Phonetic> phonetics;
    private String word;
    private List<Meaning> meanings;

    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String value) { this.phonetic = value; }

    public String getOrigin() { return origin; }
    public void setOrigin(String value) { this.origin = value; }

    public List<Phonetic> getPhonetics() { return phonetics; }
    public void setPhonetics(List<Phonetic> value) { this.phonetics = value; }

    public String getWord() { return word; }
    public void setWord(String value) { this.word = value; }

    public List<Meaning> getMeanings() { return meanings; }
    public void setMeanings(List<Meaning> value) { this.meanings = value; }
}

class Phonetic {
    private String text;
    private String audio;

    public String getText() { return text; }
    public void setText(String value) { this.text = value; }

    public String getAudio() { return audio; }
    public void setAudio(String value) { this.audio = value; }
}



// Meaning.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

// Definition.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

// Phonetic.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

