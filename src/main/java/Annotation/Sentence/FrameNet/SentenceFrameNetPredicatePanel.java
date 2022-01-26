package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import AutoProcessor.Sentence.FrameNet.TurkishSentenceAutoFramePredicate;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import Dictionary.Pos;
import FrameNet.FrameElement;
import FrameNet.FrameNet;
import WordNet.WordNet;

import java.awt.*;
import java.util.ArrayList;

public class SentenceFrameNetPredicatePanel extends SentenceAnnotatorPanel {
    private TurkishSentenceAutoFramePredicate turkishSentenceAutoFramePredicate;
    private WordNet wordNet;
    private FrameNet frameNet;

    public SentenceFrameNetPredicatePanel(String currentPath, String fileName, FrameNet frameNet, WordNet wordNet){
        super(currentPath, fileName, ViewLayerType.FRAMENET);
        setLayout(new BorderLayout());
        this.wordNet = wordNet;
        this.frameNet = frameNet;
        turkishSentenceAutoFramePredicate = new TurkishSentenceAutoFramePredicate(frameNet);
    }

    @Override
    protected void setWordLayer() {
        clickedWord.setFrameElement(list.getSelectedValue().toString());
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().x, ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().y + ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().height, 120, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getFrameElement() != null){
            String correct = word.getFrameElement().getFrameElementType();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getFrameElement() != null){
            int size = g.getFontMetrics().stringWidth(word.getFrameElement().getFrameElementType());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    public void autoDetect(){
        if (turkishSentenceAutoFramePredicate.autoPredicate(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        listModel.addElement(new FrameElement("NONE", null, null));
        if (word.getSemantic() != null && wordNet.getSynSetWithId(word.getSemantic()) != null && wordNet.getSynSetWithId(word.getSemantic()).getPos().equals(Pos.VERB)){
            listModel.addElement(new FrameElement("PREDICATE", "NONE", word.getSemantic()));
        }
        if (word.getFrameElement() != null && word.getFrameElement().getFrameElementType().equals("NONE")){
            return 0;
        }
        if (word.getFrameElement() != null && word.getFrameElement().getFrameElementType().equals("PREDICATE") && word.getFrameElement().getId().equals(word.getSemantic())){
            return 1;
        }
        return -1;
    }

}
