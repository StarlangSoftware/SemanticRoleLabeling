package AutoProcessor.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import PropBank.FramesetList;

public abstract class SentenceAutoArgument {

    protected FramesetList framesetList;
    /**
     * The method should set all the semantic role labels in the sentence. The method assumes that the predicates
     * of the sentences were determined previously.
     * @param sentence The sentence for which semantic roles will be determined automatically.
     * @return True if the auto argument is successful.
     */
    public abstract boolean autoArgument(AnnotatedSentence sentence);
}
