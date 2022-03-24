package Annotation.ParseTree.Propbank;

import DataCollector.ParseTree.TreeEditorFrame;
import DataCollector.ParseTree.TreeEditorPanel;

public class TreePropbankArgumentFrame extends TreeEditorFrame {

    public TreePropbankArgumentFrame(){
        this.setTitle("Propbank Argument Editor");
    }

    @Override
    protected TreeEditorPanel generatePanel(String currentPath, String rawFileName) {
        return new TreePropbankArgumentPanel(currentPath, rawFileName);
    }
}
