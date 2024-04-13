package Annotation.ParseTree.PropBank;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import DataCollector.ParseTree.TreeAction.LayerAction;
import PropBank.Argument;

import javax.swing.*;

public class TreePropBankPredicatePanel extends TreePropBankPanel {

    private final JList list;
    private final DefaultListModel listModel;

    /**
     * Constructor for the PropBank predicate panel for a parse tree. Constructs the list used to annotated words. It
     * also adds the list selection listener which will update the parse tree according to the selection.
     * @param path The absolute path of the annotated parse tree.
     * @param fileName The raw file name of the annotated parse tree.
     */
    public TreePropBankPredicatePanel(String path, String fileName) {
        super(path, fileName, ViewLayerType.PROPBANK, false);
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setVisible(false);
        list.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                if (list.getSelectedIndex() != -1 && previousNode != null) {
                    previousNode.setSelected(false);
                    LayerAction action = new LayerAction(((TreePropBankPredicatePanel)((JList) listSelectionEvent.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), list.getSelectedValue().toString(), ViewLayerType.PROPBANK);
                    actionList.add(action);
                    action.execute();
                    list.setVisible(false);
                    pane.setVisible(false);
                    isEditing = false;
                    repaint();
                }
            }
        });
        list.setFocusTraversalKeysEnabled(false);
        pane = new JScrollPane(list);
        add(pane);
        pane.setFocusTraversalKeysEnabled(false);
        setFocusable(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Fills the JList that contains PREDICATE tag with semantic id and NONE.
     * @param node Selected node for which options will be displayed.
     */
    public void populateLeaf(ParseNodeDrawable node){
        int selectedIndex = -1;
        if (previousNode != null){
            previousNode.setSelected(false);
        }
        previousNode = node;
        listModel.clear();
        listModel.addElement(new Argument("NONE", null));
        if (node.getLayerData(ViewLayerType.SEMANTICS) != null){
            String semantics = node.getLayerData(ViewLayerType.SEMANTICS);
            if (node.getLayerInfo().getNumberOfMeanings() == 1){
                listModel.addElement(new Argument("PREDICATE", semantics));
                if (node.getLayerInfo().getArgument() != null){
                    if (node.getLayerInfo().getArgument().getArgumentType().equals("PREDICATE") && node.getLayerInfo().getArgument().getId().equals(semantics)){
                        selectedIndex = 1;
                    }
                    if (node.getLayerInfo().getArgument().getArgumentType().equals("NONE")){
                        selectedIndex = 0;
                    }
                }
            }
        }
        if (selectedIndex != -1){
            list.setValueIsAdjusting(true);
            list.setSelectedIndex(selectedIndex);
        }
        list.setVisible(true);
        pane.setVisible(true);
        pane.getVerticalScrollBar().setValue(0);
        pane.setBounds(node.getArea().getX() - 5, node.getArea().getY() + 30, 200, 90);
        this.repaint();
        isEditing = true;
    }

}
