package Annotation.Sentence.FrameNet;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import FrameNet.FrameNet;

import javax.swing.*;

public class SentenceFrameNetPredicateFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoPredicateDetectionOption;
    private final FrameNet frameNet;

    /**
     * Constructor for {@link SentenceFrameNetPredicateFrame}. Adds automatic predicate detection
     * button. Loads the Turkish FrameNet.
     */
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

    /**
     * The next method takes an int count as input and moves forward along the SentenceFrameNetPredicatePanels as much
     * as the count. If the autoPredicateDetectionOption is selected, it automatically assigns predicate tags to
     * some words.
     * @param count Integer count is used to move forward.
     */
    public void next(int count){
        super.next(count);
        SentenceFrameNetPredicatePanel current;
        current = (SentenceFrameNetPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsFramePredicate()){
            current.autoDetect();
        }
    }

    /**
     * The previous method takes an int count as input and moves forward along the SentenceFrameNetPredicatePanels as
     * much  as the count. If the autoPredicateDetectionOption is selected, it automatically assigns predicate tags to
     * some words.
     * @param count Integer count is used to move backward.
     */
    public void previous(int count){
        super.previous(count);
        SentenceFrameNetPredicatePanel current;
        current = (SentenceFrameNetPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsFramePredicate()){
            current.autoDetect();
        }
    }


}
