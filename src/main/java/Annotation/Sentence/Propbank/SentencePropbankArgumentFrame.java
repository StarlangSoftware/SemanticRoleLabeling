package Annotation.Sentence.Propbank;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import javax.swing.*;

public class SentencePropbankArgumentFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoArgumentDetectionOption;
    private final FramesetList xmlParser;

    public SentencePropbankArgumentFrame() {
        super();
        autoArgumentDetectionOption = new JCheckBox("Auto Argument Detection", false);
        toolBar.add(autoArgumentDetectionOption);
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Argument Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropbankArgumentPanel(currentPath, rawFileName, xmlParser);
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
