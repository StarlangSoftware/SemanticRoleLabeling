package Annotation.ParseTree.Propbank;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.*;
import DataCollector.ParseTree.TreeAction.LayerAction;
import DataCollector.ParseTree.TreeLeafEditorPanel;
import PropBank.Argument;
import PropBank.Frameset;
import PropBank.FramesetArgument;
import PropBank.FramesetList;
import WordNet.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.HashSet;

public class TreePropbankArgumentPanel extends TreeLeafEditorPanel {

    private FramesetList framesetList;
    private WordNet wordNet;
    private JTree tree;
    private DefaultTreeModel treeModel;

    public TreePropbankArgumentPanel(String path, String fileName, WordNet wordNet) {
        super(path, fileName, ViewLayerType.PROPBANK, false);
        this.wordNet = wordNet;
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
                    SynSet predicateSynSet = (SynSet)((DefaultMutableTreeNode)treeNode.getParent()).getUserObject();
                    FramesetArgument argument = (FramesetArgument) treeNode.getUserObject();
                    action = new LayerAction(((TreePropbankArgumentPanel)((JTree) e.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), argument.getArgumentType() + "$" + predicateSynSet.getId(), ViewLayerType.PROPBANK);
                } else {
                    action = new LayerAction(((TreePropbankArgumentPanel)((JTree) e.getSource()).getParent().getParent().getParent()), previousNode.getLayerInfo(), "NONE", ViewLayerType.PROPBANK);
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

    public void populateLeaf(ParseNodeDrawable node){
        DefaultMutableTreeNode selectedNode = null;
        if (previousNode != null){
            previousNode.setSelected(false);
        }
        previousNode = node;
        HashSet<Frameset> frameSets = currentTree.getPredicateSynSets(wordNet, framesetList);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        for (Frameset frameset : frameSets){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(wordNet.getSynSetWithId(frameset.getId()));
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

    protected int getStringSize(ParseNodeDrawable parseNode, Graphics g) {
        if (parseNode.numberOfChildren() == 0) {
            if (parseNode.getLayerInfo().getArgument() != null){
                return g.getFontMetrics().stringWidth(parseNode.getLayerInfo().getArgument().getArgumentType());
            } else {
                return g.getFontMetrics().stringWidth(parseNode.getData().getName());
            }
        } else {
            return g.getFontMetrics().stringWidth(parseNode.getData().getName());
        }
    }

    protected void drawString(ParseNodeDrawable parseNode, Graphics g, int x, int y){
        if (parseNode.numberOfChildren() == 0){
            g.drawString(parseNode.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
            g.setColor(Color.RED);
            y += 25;
            if (parseNode.getLayerInfo().getArgument() != null){
                g.drawString(parseNode.getLayerInfo().getArgument().getArgumentType(), x, y);
                if (parseNode.getLayerInfo().getArgument().getId() != null){
                    Font previousFont = g.getFont();
                    g.setFont(new Font("Serif", Font.PLAIN, 10));
                    g.drawString(parseNode.getLayerInfo().getArgument().getId(), x - 15, y + 10);
                    g.setFont(previousFont);
                }
            }
        } else {
            g.drawString(parseNode.getData().getName(), x, y);
        }
    }

    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

}
