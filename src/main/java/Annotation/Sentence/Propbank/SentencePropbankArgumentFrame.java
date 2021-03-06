package Annotation.Sentence.Propbank;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;
import WordNet.WordNet;

import javax.swing.*;

public class SentencePropbankArgumentFrame extends SentenceAnnotatorFrame {
    private JCheckBox autoArgumentDetectionOption;
    private FramesetList xmlParser;
    private WordNet wordNet;

    public SentencePropbankArgumentFrame(final WordNet wordNet) {
        super();
        autoArgumentDetectionOption = new JCheckBox("Auto Argument Detection", false);
        toolBar.add(autoArgumentDetectionOption);
        this.wordNet = wordNet;
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Argument Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropbankArgumentPanel(currentPath, rawFileName, wordNet, xmlParser);
    }

    public void next(int count){
        super.next(count);
        SentencePropbankArgumentPanel current;
        current = (SentencePropbankArgumentPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoArgumentDetectionOption.isSelected() && current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

    public void previous(int count){
        super.previous(count);
        SentencePropbankArgumentPanel current;
        current = (SentencePropbankArgumentPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoArgumentDetectionOption.isSelected() && current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

}
