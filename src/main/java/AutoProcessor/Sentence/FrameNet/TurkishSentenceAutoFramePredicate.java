package AutoProcessor.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import FrameNet.FrameNet;

import java.util.ArrayList;

public class TurkishSentenceAutoFramePredicate extends SentenceAutoFramePredicate {

    /**
     * Constructor for {@link TurkishSentenceAutoFramePredicate}. Gets the FrameSets as input from the user, and sets
     * the corresponding attribute.
     * @param frameNet FrameNet containing the Turkish frames.
     */
    public TurkishSentenceAutoFramePredicate(FrameNet frameNet){
        this.frameNet = frameNet;
    }

    /**
     * Checks all possible frame predicates and annotate them.
     * @param sentence The sentence for which frame predicates will be determined automatically.
     * @return True, if at least one frame predicate is annotated, false otherwise.
     */
    @Override
    public boolean autoPredicate(AnnotatedSentence sentence) {
        ArrayList<AnnotatedWord> candidateList = sentence.predicateFrameCandidates(frameNet);
        for (AnnotatedWord word : candidateList){
            word.setFrameElement("PREDICATE$NONE$" + word.getSemantic());
        }
        return !candidateList.isEmpty();
    }
}
