package Annotation.Sentence.FrameNet;

import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import FrameNet.FrameNet;

import javax.swing.*;

public class SentenceFrameNetElementFrame extends SentenceAnnotatorFrame {

    private final FrameNet frameNet;

    /**
     * Constructor for {@link SentenceFrameNetElementFrame}. Loads the Turkish FrameNet and WordNet.
     */
    public SentenceFrameNetElementFrame() {
        super();
        frameNet = new FrameNet();
        JOptionPane.showMessageDialog(this, "WordNet and frameNet are loaded!", "Frame Element Selection", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public SentenceAnnotatorPanel generatePanel(String currentPath, String rawFileName) {
        return new SentenceFrameNetElementPanel(currentPath, rawFileName, frameNet);
    }

}
