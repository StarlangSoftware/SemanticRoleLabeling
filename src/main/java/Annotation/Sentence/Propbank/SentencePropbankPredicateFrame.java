package Annotation.Sentence.Propbank;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import javax.swing.*;

public class SentencePropbankPredicateFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoPredicateDetectionOption;
    private final FramesetList xmlParser;

    public SentencePropbankPredicateFrame() {
        super();
        autoPredicateDetectionOption = new JCheckBox("Auto Predicate Detection", false);
        toolBar.add(autoPredicateDetectionOption);
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Predicate Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropbankPredicatePanel(currentPath, rawFileName, xmlParser);
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
