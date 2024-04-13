package Annotation.Sentence.PropBank;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import javax.swing.*;

public class SentencePropBankPredicateFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoPredicateDetectionOption;
    private final FramesetList xmlParser;

    /**
     * Constructor for {@link SentencePropBankPredicateFrame}. Adds automatic predicate detection
     * button. Loads the Turkish PropBank.
     */
    public SentencePropBankPredicateFrame() {
        super();
        autoPredicateDetectionOption = new JCheckBox("Auto Predicate Detection", false);
        toolBar.add(autoPredicateDetectionOption);
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Predicate Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    protected SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropBankPredicatePanel(currentPath, rawFileName, xmlParser);
    }

    /**
     * The next method takes an int count as input and moves forward along the SentencePropbankPredicatePanels as much
     * as the count. If the autoPredicateDetectionOption is selected, it automatically assigns predicate tags to
     * some words.
     * @param count Integer count is used to move forward.
     */
    public void next(int count){
        super.next(count);
        SentencePropBankPredicatePanel current;
        current = (SentencePropBankPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

    /**
     * The previous method takes an int count as input and moves backward along the SentencePropbankPredicatePanels as
     * much  as the count. If the autoPredicateDetectionOption is selected, it automatically assigns predicate tags to
     * some words.
     * @param count Integer count is used to move backward.
     */
    public void previous(int count){
        super.previous(count);
        SentencePropBankPredicatePanel current;
        current = (SentencePropBankPredicatePanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoPredicateDetectionOption.isSelected() && !current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

}
