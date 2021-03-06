package Annotation.Sentence.Propbank;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;
import WordNet.WordNet;

import javax.swing.*;

public class SentencePropbankPredicateFrame extends SentenceAnnotatorFrame {
    private JCheckBox autoPredicateDetectionOption;
    private FramesetList xmlParser;
    private WordNet wordNet;

    public SentencePropbankPredicateFrame() {
        super();
        autoPredicateDetectionOption = new JCheckBox("Auto Predicate Detection", false);
        wordNet = new WordNet();
        toolBar.add(autoPredicateDetectionOption);
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Predicate Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropbankPredicatePanel(currentPath, rawFileName, xmlParser, wordNet);
    }

    public void next(int count){
        super.next(count);
        SentencePropbankPredicatePanel current;
        current = (SentencePropbankPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

    public void previous(int count){
        super.previous(count);
        SentencePropbankPredicatePanel current;
        current = (SentencePropbankPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

}
