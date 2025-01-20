package AutoProcessor.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import MorphologicalAnalysis.MorphologicalTag;
import PropBank.ArgumentType;
import PropBank.FramesetList;

public class TurkishSentenceAutoArgumentWithShallowParse extends SentenceAutoArgument {

    /**
     * Constructor for TurkishSentenceAutoArgumentWithShallowParse. Sets the Turkish propbank.
     * @param framesetList Turkish propbank.
     */
    public TurkishSentenceAutoArgumentWithShallowParse(FramesetList framesetList) {
        this.framesetList = framesetList;
    }

    /**
     * Given the sentence for which the predicate(s) were determined before, this method automatically assigns
     * semantic role labels to some/all words in the sentence. The method first finds the first predicate, then assuming
     * that the shallow parse tags were preassigned, assigns ÖZNE tagged words ARG0; NESNE tagged words ARG1. If the
     * verb is in passive form, ÖZNE tagged words are assigned as ARG1.
     * @param sentence The sentence for which semantic roles will be determined automatically.
     * @return If the method assigned at least one word a semantic role label, the method returns true; false otherwise.
     */
    public boolean autoArgument(AnnotatedSentence sentence) {
        boolean modified = false;
        boolean onlyArg1 = false;
        String predicateId = null;
        for (int i = 0; i < sentence.wordCount(); i++){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getArgumentList() != null && word.getArgumentList().containsPredicate()){
                if (word.getParse() != null && word.getParse().containsTag(MorphologicalTag.PASSIVE)){
                    onlyArg1 = true;
                }
                predicateId = word.getSemantic();
                break;
            }
        }
        if (framesetList.frameExists(predicateId) && !framesetList.getFrameSet(predicateId).containsArgument(ArgumentType.ARG0)){
            onlyArg1 = true;
        }
        if (predicateId != null){
            for (int i = 0; i < sentence.wordCount(); i++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
                if (word.getArgumentList() == null){
                    if (word.getShallowParse() != null && word.getShallowParse().equalsIgnoreCase("ÖZNE")){
                        if (word.getParse() != null && onlyArg1){
                            word.setArgumentList("ARG1$" + predicateId);
                        } else {
                            word.setArgumentList("ARG0$" + predicateId);
                        }
                        modified = true;
                    } else {
                        if (word.getShallowParse() != null && word.getShallowParse().equalsIgnoreCase("NESNE")){
                            word.setArgumentList("ARG1$" + predicateId);
                            modified = true;
                        }
                    }
                }
            }
        }
        return modified;
    }
}
