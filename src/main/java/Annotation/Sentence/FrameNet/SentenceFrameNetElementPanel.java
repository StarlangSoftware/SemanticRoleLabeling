package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import FrameNet.FrameNet;
import FrameNet.DisplayedFrame;
import FrameNet.FrameElementList;
import PropBank.FramesetArgument;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class SentenceFrameNetElementPanel extends SentenceFrameNetPanel {

    private final FrameNet frameNet;
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private boolean selfSelected = false;

    /**
     * Constructor for the FrameNet element panel for an annotated sentence. Sets the attributes.
     * @param currentPath The absolute path of the annotated file.
     * @param fileName The raw file name of the annotated file.
     * @param frameNet Turkish FrameNet
     */
    public SentenceFrameNetElementPanel(String currentPath, String fileName, FrameNet frameNet){
        super(currentPath, fileName, ViewLayerType.FRAMENET);
        setLayout(new BorderLayout());
        this.frameNet = frameNet;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Frames");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setVisible(false);
        tree.addTreeSelectionListener(e -> {
            TreePath[] list = tree.getSelectionPaths();
            if (!selfSelected && clickedWord != null && list != null && list.length > 0) {
                if (list.length == 1 && ((DefaultMutableTreeNode)list[0].getLastPathComponent()).getLevel() == 0) {
                    clickedWord.setFrameElementList("NONE");
                    sentence.writeToFile(new File(fileDescription.getFileName()));
                } else {
                    for (int i = 0; i < sentence.wordCount(); i++) {
                        AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
                        if (word.isSelected()){
                            String frameElementList = getFrameElementList(list);
                            word.setFrameElementList(frameElementList);
                        }
                    }
                    sentence.writeToFile(new File(fileDescription.getFileName()));
                }
            }
            clickedWord = null;
            pane.setVisible(false);
            tree.setVisible(false);
        });
        pane = new JScrollPane(tree);
        add(pane);
        pane.setFocusTraversalKeysEnabled(false);
        pane.setVisible(false);
        setFocusable(false);
    }

    /**
     * Constructs a frame element list string from the selected frame elements for a word.
     * @param list Selected tree paths representing different frame structures for a word.
     * @return String form of the frame element list of a word.
     */
    private String getFrameElementList(TreePath[] list) {
        String frameElementList = "";
        for (TreePath treePath : list) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (node.getLevel() == 2) {
                DisplayedFrame displayedFrame = (DisplayedFrame) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
                String frameElement = (String) node.getUserObject();
                if (frameElementList.isEmpty()) {
                    frameElementList = frameElement + "$" + displayedFrame.getFrame().getName() + "$" + displayedFrame.getLexicalUnit();
                } else {
                    frameElementList += "#" + frameElement + "$" + displayedFrame.getFrame().getName() + "$" + displayedFrame.getLexicalUnit();
                }
            }
        }
        return frameElementList;
    }

    /**
     * Fills the JList that contains all FrameNet elements for all predicates in the sentence.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        ArrayList<DefaultMutableTreeNode> selectedNodes = new ArrayList<>();
        ArrayList<DisplayedFrame> currentFrames = sentence.getFrames(frameNet);
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        FrameElementList frameElementList = word.getFrameElementList();
        for (DisplayedFrame frame : currentFrames){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(frame);
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(frameNode);
            DefaultMutableTreeNode predicateNode = new DefaultMutableTreeNode(new FramesetArgument("PREDICATE", "NONE", ""));
            frameNode.add(predicateNode);
            if (frameElementList != null && frameElementList.containsPredicateWithId(frame.getLexicalUnit())){
                selectedNodes.add(predicateNode);
            }
            for (int i = 0; i < frame.getFrame().frameElementSize(); i++){
                String frameElement = frame.getFrame().getFrameElement(i);
                DefaultMutableTreeNode frameElementNode = new DefaultMutableTreeNode(frameElement);
                frameNode.add(frameElementNode);
                if (frameElementList != null && frameElementList.containsFrameElement(frameElement, frame.getFrame().getName(), frame.getLexicalUnit())){
                    selectedNodes.add(frameElementNode);
                    selfSelected = true;
                }
            }
        }
        treeModel.reload();
        for (DefaultMutableTreeNode node : selectedNodes){
            tree.addSelectionPath(new TreePath(treeModel.getPathToRoot(node)));
        }
        tree.setVisible(true);
        pane.setVisible(true);
        return -1;
    }

    /**
     * Constructs the JList below the clicked word.
     * @param mouseEvent Mouse event handled
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        if (selectedWordIndex != -1){
            populateLeaf(sentence, selectedWordIndex);
            pane.getVerticalScrollBar().setValue(0);
            pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, 200);
            clickedWord = ((AnnotatedWord)sentence.getWord(selectedWordIndex));
            selectedWordIndex = -1;
            selfSelected = false;
            this.repaint();
        }
    }

}
