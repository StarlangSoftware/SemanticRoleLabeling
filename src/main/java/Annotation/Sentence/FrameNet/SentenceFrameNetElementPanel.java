package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import DataCollector.Sentence.SentenceAnnotatorPanel;
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

public class SentenceFrameNetElementPanel extends SentenceAnnotatorPanel {

    private final FrameNet frameNet;
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private boolean selfSelected = false;

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
                    clickedWord.setFrameElement(frameElement + "$" + displayedFrame.getFrame().getName() + "$" + displayedFrame.getLexicalUnit());
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

    @Override
    protected void setWordLayer() {
        clickedWord.setFrameElement(list.getSelectedValue().toString());
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getHeight(), 120, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getFrameElement() != null){
            String correct = word.getFrameElement().getFrameElementType();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getFrameElement() != null){
            int size = g.getFontMetrics().stringWidth(word.getFrameElement().getFrameElementType());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

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
