package AutoProcessor.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import MorphologicalAnalysis.MorphologicalTag;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.AnnotatedPhrase;
import PropBank.ArgumentType;
import PropBank.FramesetList;

import java.util.ArrayList;


public class TurkishSentenceAutoArgumentWithDependency extends SentenceAutoArgument{

    /**
     * Constructor for TurkishSentenceAutoArgumentWithDependency. Sets the Turkish propbank.
     * @param framesetList Turkish propbank.
     */
    public TurkishSentenceAutoArgumentWithDependency(FramesetList framesetList) {
        this.framesetList = framesetList;
    }

    /**
     * Given the sentence for which the predicate(s) were determined before, this method automatically assigns
     * semantic role labels to some/all words in the sentence. The method first finds the first predicate, then assuming
     * that the dependency tags were preassigned, assigns NSUBJ dependency group as ARG0; OBJ dependency group as ARG1.
     * If the verb is in passive form, NSUBJ dependency group is assigned as ARG1.
     * @param sentence The sentence for which semantic roles will be determined automatically.
     * @return If the method assigned at least one word a semantic role label, the method returns true; false otherwise.
     */
    public boolean autoArgument(AnnotatedSentence sentence) {
        boolean modified = false;
        boolean onlyArg1 = false;
        int rootWordIndex = -1;
        String predicateId = null;
        for (int i = sentence.wordCount() - 1; i >= 0 ; i--){
            AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
            if (word.getArgument() != null && word.getArgument().getArgumentType().equals("PREDICATE")){
                if (word.getParse() != null && word.getParse().containsTag(MorphologicalTag.PASSIVE)){
                    onlyArg1 = true;
                }
                predicateId = word.getArgument().getId();
                rootWordIndex = i + 1;
                break;
            }
        }
        if (framesetList.frameExists(predicateId) && !framesetList.getFrameSet(predicateId).containsArgument(ArgumentType.ARG0)){
            onlyArg1 = true;
        }
        if (predicateId != null){
            ArrayList<AnnotatedPhrase> dependencyGroups = sentence.getDependencyGroups(rootWordIndex);
            for (AnnotatedPhrase dependencyGroup : dependencyGroups){
                if (dependencyGroup.getTag().equals("NSUBJ") || dependencyGroup.getTag().equals("OBJ")){
                    for (int i = 0; i < dependencyGroup.wordCount(); i++){
                        AnnotatedWord word = (AnnotatedWord) dependencyGroup.getWord(i);
                        if (word.getArgument() == null){
                            switch (dependencyGroup.getTag()){
                                case "NSUBJ":
                                    if (onlyArg1){
                                        word.setArgument("ARG1$" + predicateId);
                                    } else {
                                        word.setArgument("ARG0$" + predicateId);
                                    }
                                    modified = true;
                                    break;
                                case "OBJ":
                                    word.setArgument("ARG1$" + predicateId);
                                    modified = true;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return modified;
    }

}
