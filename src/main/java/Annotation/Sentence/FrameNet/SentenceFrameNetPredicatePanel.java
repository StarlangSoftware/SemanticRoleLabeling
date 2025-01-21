package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import AutoProcessor.Sentence.FrameNet.TurkishSentenceAutoFramePredicate;
import FrameNet.FrameElement;
import FrameNet.FrameNet;
import PropBank.Argument;

import java.awt.*;

public class SentenceFrameNetPredicatePanel extends SentenceFrameNetPanel {
    private final TurkishSentenceAutoFramePredicate turkishSentenceAutoFramePredicate;

    /**
     * Constructor for the FrameNet predicate panel for an annotated sentence. Sets the attributes.
     * @param currentPath The absolute path of the annotated file.
     * @param fileName The raw file name of the annotated file.
     * @param frameNet Turkish FrameNet
     */
    public SentenceFrameNetPredicatePanel(String currentPath, String fileName, FrameNet frameNet){
        super(currentPath, fileName, ViewLayerType.FRAMENET);
        setLayout(new BorderLayout());
        turkishSentenceAutoFramePredicate = new TurkishSentenceAutoFramePredicate(frameNet);
    }

    /**
     * Automatically detects the Frame predicate tag of words in the sentence using turkishSentenceAutoNER.
     */
    public void autoDetect(){
        if (turkishSentenceAutoFramePredicate.autoPredicate(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    @Override
    protected void setWordLayer() {
        if (list.getSelectedIndex() == 0 && clickedWord.getFrameElementList().containsPredicate()){
            clickedWord.getFrameElementList().removePredicate();
        } else {
            if (list.getSelectedIndex() == 1){
                if (clickedWord.getFrameElementList() == null){
                    clickedWord.setFrameElementList("PREDICATE$NONE$" + ((Argument)list.getSelectedValue()).getId());
                } else {
                    if (!clickedWord.getFrameElementList().containsPredicate()){
                        clickedWord.getFrameElementList().addPredicate(((Argument)list.getSelectedValue()).getId());
                    }
                }
            }
        }
    }

    /**
     * Fills the JList that contains NONE and PREDICATE tag with the words semantic id.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        listModel.addElement(new FrameElement("NONE", null, null));
        if (word.getSemantic() != null){
            listModel.addElement(new FrameElement("PREDICATE", "NONE", word.getSemantic()));
        }
        if (word.getFrameElementList() != null){
            if (word.getFrameElementList().containsPredicateWithId(word.getSemantic())){
                return 1;
            }
            if (word.getFrameElementList().getFrameElements().contains("NONE")){
                return 0;
            }
        }
        return -1;
    }

}
