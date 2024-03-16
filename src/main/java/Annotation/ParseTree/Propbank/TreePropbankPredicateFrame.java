package Annotation.ParseTree.Propbank;

import DataCollector.ParseTree.TreeEditorFrame;
import DataCollector.ParseTree.TreeEditorPanel;

public class TreePropbankPredicateFrame extends TreeEditorFrame {

    public TreePropbankPredicateFrame(){
        this.setTitle("Propbank Predicate Editor");
    }

    @Override
    protected TreeEditorPanel generatePanel(String currentPath, String rawFileName) {
        return new TreePropbankPredicatePanel(currentPath, rawFileName);
    }
}
