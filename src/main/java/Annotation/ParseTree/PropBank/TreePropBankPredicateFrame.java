package Annotation.ParseTree.PropBank;

import DataCollector.ParseTree.TreeEditorFrame;
import DataCollector.ParseTree.TreeEditorPanel;

public class TreePropBankPredicateFrame extends TreeEditorFrame {

    /**
     * Constructor for {@link TreePropBankPredicateFrame}. Sets the title.
     */
    public TreePropBankPredicateFrame(){
        this.setTitle("Propbank Predicate Editor");
    }

    @Override
    protected TreeEditorPanel generatePanel(String currentPath, String rawFileName) {
        return new TreePropBankPredicatePanel(currentPath, rawFileName);
    }
}
