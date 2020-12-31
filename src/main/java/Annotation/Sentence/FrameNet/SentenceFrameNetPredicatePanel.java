package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.AutoProcessor.AutoPredicate.TurkishSentenceAutoFramePredicate;
import AnnotatedSentence.ViewLayerType;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import Dictionary.Pos;
import FrameNet.FrameElement;
import FrameNet.FrameNet;
import WordNet.WordNet;

import java.awt.*;

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
