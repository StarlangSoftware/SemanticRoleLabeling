package Annotation.Sentence.FrameNet;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import FrameNet.FrameNet;

import javax.swing.*;

public class SentenceFrameNetPredicateFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoPredicateDetectionOption;
    private final FrameNet frameNet;

    public SentenceFrameNetPredicateFrame() {
        super();
        autoPredicateDetectionOption = new JCheckBox("Auto Predicate Detection", false);
        toolBar.add(autoPredicateDetectionOption);
        frameNet = new FrameNet();
        JOptionPane.showMessageDialog(this, "WordNet and frameNet are loaded!", "Predicate Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceFrameNetPredicatePanel(currentPath, rawFileName, frameNet);
    }

    public void next(int count){
        super.next(count);
        SentenceFrameNetPredicatePanel current;
        current = (SentenceFrameNetPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsFramePredicate()){
            current.autoDetect();
        }
    }

    public void previous(int count){
        super.previous(count);
        SentenceFrameNetPredicatePanel current;
        current = (SentenceFrameNetPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsFramePredicate()){
            current.autoDetect();
        }
    }


}
