package Annotation.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import AutoProcessor.Sentence.Propbank.TurkishSentenceAutoPredicate;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.Argument;
import PropBank.FramesetList;

import java.awt.*;
import java.util.ArrayList;

public class SentencePropbankPredicatePanel extends SentenceAnnotatorPanel {
    private final TurkishSentenceAutoPredicate turkishSentenceAutoPredicate;

    public SentencePropbankPredicatePanel(String currentPath, String fileName, FramesetList xmlParser){
        super(currentPath, fileName, ViewLayerType.PROPBANK);
        setLayout(new BorderLayout());
        turkishSentenceAutoPredicate = new TurkishSentenceAutoPredicate(xmlParser);
    }

    @Override
    protected void setWordLayer() {
        clickedWord.setArgument(list.getSelectedValue().toString());
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getArgument() != null){
            String correct = word.getArgument().getArgumentType();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getArgument() != null){
            int size = g.getFontMetrics().stringWidth(word.getArgument().getArgumentType());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    public void autoDetect(){
        if (turkishSentenceAutoPredicate.autoPredicate(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        listModel.addElement(new Argument("NONE", null));
        if (word.getSemantic() != null){
            listModel.addElement(new Argument("PREDICATE", word.getSemantic()));
        }
        if (word.getArgument() != null && word.getArgument().getArgumentType().equals("NONE")){
            return 0;
        }
        if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE") && word.getArgument().getId().equals(word.getSemantic())){
            return 1;
        }
        return -1;
    }

}
