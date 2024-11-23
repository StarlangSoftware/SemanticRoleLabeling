package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import FrameNet.FrameNet;
import FrameNet.DisplayedFrame;

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
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null && clickedWord != null && !selfSelected) {
                if (node.getLevel() == 2){
                    DisplayedFrame displayedFrame = (DisplayedFrame) ((DefaultMutableTreeNode)node.getParent()).getUserObject();
                    String frameElement = (String) node.getUserObject();
                    for (int i = 0; i < sentence.wordCount(); i++){
                        AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
                        if (word.isSelected()){
                            word.setFrameElement(frameElement + "$" + displayedFrame.getFrame().getName() + "$" + displayedFrame.getLexicalUnit());
                        }
                    }
                    sentence.writeToFile(new File(fileDescription.getFileName()));
                } else {
                    if (node.getLevel() == 0){
                        clickedWord.setFrameElement("NONE");
                        sentence.writeToFile(new File(fileDescription.getFileName()));
                    }
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
     * Fills the JList that contains all FrameNet elements for all predicates in the sentence.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return The index of the selected tag, -1 if nothing selected.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        DefaultMutableTreeNode selectedNode = null;
        ArrayList<DisplayedFrame> currentFrames = sentence.getFrames(frameNet);
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        for (DisplayedFrame frame : currentFrames){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(frame);
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(frameNode);
            for (int i = 0; i < frame.getFrame().frameElementSize(); i++){
                String frameElement = frame.getFrame().getFrameElement(i);
                DefaultMutableTreeNode frameElementNode = new DefaultMutableTreeNode(frameElement);
                frameNode.add(frameElementNode);
                if (word.getFrameElement() != null && word.getFrameElement().getId() != null && word.getFrameElement().getFrameElementType() != null && word.getFrameElement().getFrameElementType().equals(frameElement) && word.getFrameElement().getFrame() != null && word.getFrameElement().getFrame().equals(frame.getFrame().getName())){
                    selectedNode = frameElementNode;
                }
            }
        }
        treeModel.reload();
        if (selectedNode != null){
            selfSelected = true;
            tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(selectedNode)));
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
