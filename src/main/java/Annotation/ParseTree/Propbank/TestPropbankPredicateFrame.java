package Annotation.ParseTree.Propbank;

import WordNet.WordNet;

public class TestPropbankPredicateFrame {

    public static void main(String[] args){
        WordNet turkish = new WordNet();
        new TreePropbankPredicateFrame(turkish);
    }

}
