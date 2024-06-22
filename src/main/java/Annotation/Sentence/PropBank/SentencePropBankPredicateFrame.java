package Annotation.Sentence.PropBank;

import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.Sentence.SentenceAnnotatorFrame;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class SentencePropBankPredicateFrame extends SentenceAnnotatorFrame {
    private final JCheckBox autoPredicateDetectionOption;
    private final FramesetList xmlParser;

    /**
     * Constructor for {@link SentencePropBankPredicateFrame}. Adds automatic predicate detection
     * button. Loads the Turkish PropBank.
     */
    public SentencePropBankPredicateFrame() {
        super();
        AnnotatedCorpus annotatedCorpus;
        String subFolder = "false";
        Properties properties1 = new Properties();
        try {
            properties1.load(Files.newInputStream(new File("config.properties").toPath()));
            if (properties1.containsKey("subFolder")){
                subFolder = properties1.getProperty("subFolder");
            }
        } catch (IOException ignored) {
        }
        annotatedCorpus = readCorpus(subFolder);
        autoPredicateDetectionOption = new JCheckBox("Auto Predicate Detection", false);
        toolBar.add(autoPredicateDetectionOption);
        xmlParser = new FramesetList();
        JMenuItem itemViewAnnotated = addMenuItem(projectMenu, "View Annotations", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        itemViewAnnotated.addActionListener(e -> new ViewSentencePropBankPredicateAnnotationFrame(annotatedCorpus, xmlParser, this));
        JOptionPane.showMessageDialog(this, "PropBank is loaded!", "PropBank Predicate Annotation", JOptionPane.INFORMATION_MESSAGE);
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
