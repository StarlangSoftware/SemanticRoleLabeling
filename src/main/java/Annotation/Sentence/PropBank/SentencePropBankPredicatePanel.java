package Annotation.Sentence.PropBank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AutoProcessor.Sentence.Propbank.TurkishSentenceAutoPredicate;
import PropBank.Argument;
import PropBank.FramesetList;

import java.awt.*;

public class SentencePropBankPredicatePanel extends SentencePropBankPanel {
    private final TurkishSentenceAutoPredicate turkishSentenceAutoPredicate;

    /**
     * Constructor for the PropBank predicate panel for annotated sentence.
     * @param currentPath The absolute path of the annotated sentence.
     * @param fileName The raw file name of the annotated sentence.
     * @param framesetList Turkish PropBank
     */
    public SentencePropBankPredicatePanel(String currentPath, String fileName, FramesetList framesetList){
        super(currentPath, fileName, framesetList);
        setLayout(new BorderLayout());
        turkishSentenceAutoPredicate = new TurkishSentenceAutoPredicate(framesetList);
    }

    /**
     * Automatically detects the Frame predicate  in the sentence using turkishSentenceAutoPredicate.
     */
    public void autoDetect(){
        if (turkishSentenceAutoPredicate.autoPredicate(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    @Override
    protected void setWordLayer() {
        if (list.getSelectedIndex() == 0 && clickedWord.getArgumentList().containsPredicate()){
            clickedWord.getArgumentList().removePredicate();
        } else {
            if (list.getSelectedIndex() == 1){
                if (clickedWord.getArgumentList() == null){
                    clickedWord.setArgumentList("PREDICATE$" + ((Argument)list.getSelectedValue()).getId());
                } else {
                    if (!clickedWord.getArgumentList().containsPredicate()){
                        clickedWord.getArgumentList().addPredicate(((Argument)list.getSelectedValue()).getId());
                    }
                }
            }
        }
    }

    /**
     * Fills the JList that contains PREDICATE tag with semantic id and NONE.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return Index of the selection.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        listModel.clear();
        listModel.addElement(new Argument("NONE", null));
        if (word.getSemantic() != null && framesetList.frameExists(word.getSemantic())){
            listModel.addElement(new Argument("PREDICATE", word.getSemantic()));
        }
        if (word.getArgumentList() != null){
            if (word.getArgumentList().containsPredicateWithId(word.getSemantic())){
                return 1;
            }
            if (word.getArgumentList().getArguments().contains("NONE")){
                return 0;
            }
        }
        return -1;
    }
}
