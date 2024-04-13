package Annotation.ParseTree.PropBank;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.*;
import DataCollector.ParseTree.TreeAction.LayerAction;
import PropBank.Argument;
import PropBank.Frameset;
import PropBank.FramesetArgument;
import PropBank.FramesetList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.HashSet;

public class TreePropBankArgumentPanel extends TreePropBankPanel {

    private final FramesetList framesetList;
    private final JTree tree;
    private final DefaultTreeModel treeModel;

    /**
     * Constructor for the PropBank argument panel for a parse tree. Constructs the list used to annotated words. It
     * also adds the list selection listener which will update the parse tree according to the selection.
     * @param path The absolute path of the annotated parse tree.
     * @param fileName The raw file name of the annotated parse tree.
     */
    public TreePropBankArgumentPanel(String path, String fileName) {
        super(path, fileName, ViewLayerType.PROPBANK, false);
        framesetList = new FramesetList();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("FrameSets");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setVisible(false);
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (previousNode != null && treeNode != null) {
                LayerAction action;
                previousNode.setSelected(false);
                if (treeNode.getLevel() == 2){
                    String predicateSynSet = (String)((DefaultMutableTreeNode)treeNode.getParent()).getUserObject();
                    FramesetArgument argument = (FramesetArgument) treeNode.getUserObject();
                    action = new LayerAction(((TreePropBankArgumentPanel)((JTree) e.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), argument.getArgumentType() + "$" + predicateSynSet, ViewLayerType.PROPBANK);
                } else {
                    action = new LayerAction(((TreePropBankArgumentPanel)((JTree) e.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), "NONE", ViewLayerType.PROPBANK);
                }
                actionList.add(action);
                action.execute();
                isEditing = false;
                repaint();
            }
            pane.setVisible(false);
            tree.setVisible(false);
        });
        pane = new JScrollPane(tree);
        add(pane);
        pane.setFocusTraversalKeysEnabled(false);
        setFocusable(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Fills the JList that contains all PropBank arguments for all the predicates in the parse tree.
     * @param node Selected node for which options will be displayed.
     */
    public void populateLeaf(ParseNodeDrawable node){
        DefaultMutableTreeNode selectedNode = null;
        if (previousNode != null){
            previousNode.setSelected(false);
        }
        previousNode = node;
        HashSet<Frameset> frameSets = currentTree.getPredicateSynSets(framesetList);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        for (Frameset frameset : frameSets){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(frameset.getId());
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(frameNode);
            for (FramesetArgument framesetArgument : frameset.getFramesetArguments()){
                DefaultMutableTreeNode argumentNode = new DefaultMutableTreeNode(framesetArgument);
                frameNode.add(argumentNode);
                Argument argument = node.getLayerInfo().getArgument();
                if (argument != null && argument.getArgumentType().equals(framesetArgument.getArgumentType())){
                    selectedNode = argumentNode;
                }
            }
        }
        treeModel.reload();
        if (selectedNode != null){
            tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(selectedNode)));
        }
        tree.setVisible(true);
        pane.setVisible(true);
        pane.getVerticalScrollBar().setValue(0);
        pane.setBounds(node.getArea().getX() - 5, node.getArea().getY() + 30, 250, 200);
        this.repaint();
        isEditing = true;
    }

}
