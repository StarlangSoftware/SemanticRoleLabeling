package AutoProcessor.ParseTree.PropBank;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import AnnotatedTree.ParseNodeDrawable;
import PropBank.ArgumentType;

public class TurkishAutoArgument extends AutoArgument {

    /**
     * Sets the language.
     */
    public TurkishAutoArgument() {
        super(ViewLayerType.TURKISH_WORD);
    }

    /**
     * Checks all ancestors of the current parse node, until an ancestor has a tag of given name, or the ancestor is
     * null. Returns the ancestor with the given tag, or null.
     * @param parseNode Parse node to start checking ancestors.
     * @param name Tag to check.
     * @return The ancestor of the given parse node with the given tag, if such ancestor does not exist, returns null.
     */
    private boolean checkAncestors(ParseNode parseNode, String name){
        while (parseNode != null){
            if (parseNode.getData().getName().equals(name)){
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    /**
     * Checks all ancestors of the current parse node, until an ancestor has a tag with the given, or the ancestor is
     * null. Returns the ancestor with the tag having the given suffix, or null.
     * @param parseNode Parse node to start checking ancestors.
     * @param suffix Suffix of the tag to check.
     * @return The ancestor of the given parse node with the tag having the given suffix, if such ancestor does not
     * exist, returns null.
     */
    private boolean checkAncestorsUntil(ParseNode parseNode, String suffix){
        while (parseNode != null){
            if (parseNode.getData().getName().contains("-" + suffix)){
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    /**
     * The method tries to set the argument of the given parse node to the given argument type automatically. If the
     * argument type condition matches the parse node, it returns true, otherwise it returns false.
     * @param parseNode Parse node to check for semantic role.
     * @param argumentType Semantic role to check.
     * @return True, if the argument type condition matches the parse node, false otherwise.
     */
    protected boolean autoDetectArgument(ParseNodeDrawable parseNode, ArgumentType argumentType) {
        ParseNode parent = parseNode.getParent();
        switch (argumentType){
            case ARG0:
                if (checkAncestorsUntil(parent, "SBJ")){
                    return true;
                }
                break;
            case ARG1:
                if (checkAncestorsUntil(parent, "OBJ")){
                    return true;
                }
                break;
            case ARGMADV:
                if (checkAncestorsUntil(parent, "ADV")){
                    return true;
                }
                break;
            case ARGMTMP:
                if (checkAncestorsUntil(parent, "TMP")){
                    return true;
                }
                break;
            case ARGMMNR:
                if (checkAncestorsUntil(parent, "MNR")){
                    return true;
                }
                break;
            case ARGMLOC:
                if (checkAncestorsUntil(parent, "LOC")){
                    return true;
                }
                break;
            case ARGMDIR:
                if (checkAncestorsUntil(parent, "DIR")){
                    return true;
                }
                break;
            case ARGMDIS:
                if (checkAncestors(parent, "CC")){
                    return true;
                }
                break;
            case ARGMEXT:
                if (checkAncestorsUntil(parent, "EXT")){
                    return true;
                }
                break;
        }
        return false;
    }
}
