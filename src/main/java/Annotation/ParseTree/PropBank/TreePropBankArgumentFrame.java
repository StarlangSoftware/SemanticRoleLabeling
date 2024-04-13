package Annotation.ParseTree.PropBank;

import DataCollector.ParseTree.TreeEditorFrame;
import DataCollector.ParseTree.TreeEditorPanel;

public class TreePropBankArgumentFrame extends TreeEditorFrame {

    /**
     * Constructor for {@link TreePropBankArgumentFrame}. Sets the title.
     */
    public TreePropBankArgumentFrame(){
        this.setTitle("Propbank Argument Editor");
    }

    @Override
    protected TreeEditorPanel generatePanel(String currentPath, String rawFileName) {
        return new TreePropBankArgumentPanel(currentPath, rawFileName);
    }
}
