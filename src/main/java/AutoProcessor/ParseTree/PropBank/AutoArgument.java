package AutoProcessor.ParseTree.PropBank;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTransferable;
import AnnotatedTree.Processor.NodeDrawableCollector;
import PropBank.ArgumentType;
import PropBank.Frameset;

import java.util.ArrayList;

public abstract class AutoArgument {
    protected ViewLayerType secondLanguage;
    protected abstract boolean autoDetectArgument(ParseNodeDrawable parseNode, ArgumentType argumentType);

    protected AutoArgument(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    /**
     * Given the parse tree and the frame net, the method collects all leaf nodes and tries to set a propbank argument
     * label to them. Specifically it tries all possible argument types one by one ARG0 first, then ARG1, then ARG2 etc.
     * Each argument type has a special function to accept. The special function checks basically if there is a specific
     * type of ancestor (specific to the argument, for example SUBJ for ARG0), or not.
     * @param parseTree Parse tree for semantic role labeling
     * @param frameset Frame net used in labeling.
     */
    public void autoArgument(ParseTreeDrawable parseTree, Frameset frameset){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTransferable(secondLanguage));
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(ViewLayerType.PROPBANK) == null){
                for (ArgumentType argumentType : ArgumentType.values()){
                    if (frameset.containsArgument(argumentType) && autoDetectArgument(parseNode, argumentType)){
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, ArgumentType.getPropbankType(argumentType));
                    }
                }
                if (Word.isPunctuation(parseNode.getLayerData(secondLanguage))){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, "NONE");
                }
            }
        }
        parseTree.save();
    }

}
