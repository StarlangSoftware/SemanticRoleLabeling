package Annotation.Sentence.PropBank;

import Annotation.Sentence.FrameNet.SentenceFrameNetElementFrame;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import javax.swing.*;

public class SentencePropBankArgumentFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoArgumentDetectionOption;
    private final FramesetList xmlParser;

    /**
     * Constructor for {@link SentenceFrameNetElementFrame}. Loads the Turkish PropBank and WordNet.
     */
    public SentencePropBankArgumentFrame() {
        super();
        autoArgumentDetectionOption = new JCheckBox("Auto Argument Detection", false);
        toolBar.add(autoArgumentDetectionOption);
        xmlParser = new FramesetList();
        JOptionPane.showMessageDialog(this, "WordNet and PropBank are loaded!", "PropBank Argument Annotation", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentencePropBankArgumentPanel(currentPath, rawFileName, xmlParser);
    }

    /**
     * The next method takes an int count as input and moves forward along the SentencePropbankArgumentPanels as much
     * as the count. If the autoArgumentDetectionOption is selected, it automatically assigns propBank argument tags to
     * some words.
     * @param count Integer count is used to move forward.
     */
    public void next(int count){
        super.next(count);
        SentencePropBankArgumentPanel current;
        current = (SentencePropBankArgumentPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoArgumentDetectionOption.isSelected() && current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

    /**
     * The previous method takes an int count as input and moves backward along the SentencePropbankArgumentPanels as much
     * as the count. If the autoArgumentDetectionOption is selected, it automatically assigns propBank argument tags to
     * some words.
     * @param count Integer count is used to move backward.
     */
    public void previous(int count){
        super.previous(count);
        SentencePropBankArgumentPanel current;
        current = (SentencePropBankArgumentPanel) ((JScrollPane) projectPane.getSelectedComponent()).getViewport().getView();
        if (autoArgumentDetectionOption.isSelected() && current.sentence.containsPredicate()){
            current.autoDetect();
        }
    }

}
