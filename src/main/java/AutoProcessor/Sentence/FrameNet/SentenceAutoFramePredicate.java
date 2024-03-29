package AutoProcessor.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import FrameNet.FrameNet;

public abstract class SentenceAutoFramePredicate {
    protected FrameNet frameNet;

    /**
     * The method should set determine all frame predicates in the sentence.
     * @param sentence The sentence for which frame predicates will be determined automatically.
     * @return True if the auto frame predicate is successful.
     */
    public abstract  boolean autoPredicate(AnnotatedSentence sentence);
}
