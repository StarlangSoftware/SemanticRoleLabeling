package AutoProcessor.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import PropBank.FramesetList;

import java.util.ArrayList;

public class TurkishSentenceAutoPredicate extends SentenceAutoPredicate {
    private final FramesetList framesetList;

    /**
     * Constructor for {@link TurkishSentenceAutoPredicate}. Gets the FrameSets as input from the user, and sets
     * the corresponding attribute.
     * @param framesetList FramesetList containing the Turkish propbank frames.
     */
    public TurkishSentenceAutoPredicate(FramesetList framesetList){
        this.framesetList = framesetList;
    }

    /**
     * The method uses predicateCandidates method to predict possible predicates. For each candidate, it sets for that
     * word PREDICATE tag.
     * @param sentence The sentence for which predicates will be determined automatically.
     * @return If at least one word has been tagged, true; false otherwise.
     */
    public boolean autoPredicate(AnnotatedSentence sentence){
        ArrayList<AnnotatedWord> candidateList = sentence.predicateCandidates(framesetList);
        for (AnnotatedWord word : candidateList){
            word.setArgument("PREDICATE$" + word.getSemantic());
        }
        return !candidateList.isEmpty();
    }

}
