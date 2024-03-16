package Annotation.Sentence.Propbank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import AutoProcessor.Sentence.Propbank.TurkishSentenceAutoArgument;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.Frameset;
import PropBank.FramesetArgument;
import PropBank.FramesetList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class SentencePropbankArgumentPanel extends SentenceAnnotatorPanel {
    private final FramesetList framesetList;
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private boolean selfSelected = false;
    private final TurkishSentenceAutoArgument turkishSentenceAutoArgument;

    public SentencePropbankArgumentPanel(String currentPath, String fileName, FramesetList framesetList){
        super(currentPath, fileName, ViewLayerType.PROPBANK);
        setLayout(new BorderLayout());
        this.framesetList = framesetList;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("FrameSets");
        turkishSentenceAutoArgument = new TurkishSentenceAutoArgument();
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setVisible(false);
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null && clickedWord != null && !selfSelected) {
                if (node.getLevel() == 2){
                    String synSet = (String) ((DefaultMutableTreeNode)node.getParent()).getUserObject();
                    FramesetArgument argument = (FramesetArgument) node.getUserObject();
                    clickedWord.setArgument(argument.getArgumentType() + "$" + synSet);
                    sentence.writeToFile(new File(fileDescription.getFileName()));
                } else {
                    if (node.getLevel() == 0){
                        clickedWord.setArgument("NONE");
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
        clickedWord.setArgument(list.getSelectedValue().toString());
    }

    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getArgument() != null){
            String correct = word.getArgument().getArgumentType();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
        }
    }

    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getArgument() != null){
            int size = g.getFontMetrics().stringWidth(word.getArgument().getArgumentType());
            if (size > maxSize){
                maxSize = size;
            }
        }
        return maxSize;
    }

    public void autoDetect(){
        if (turkishSentenceAutoArgument.autoArgument(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        boolean argTmp, argLoc, argDis, argMnr;
        DefaultMutableTreeNode selectedNode = null;
        HashSet<Frameset> currentFrameSets = sentence.getPredicateSynSets(framesetList);
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        for (Frameset frameset : currentFrameSets){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(frameset.getId());
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(frameNode);
            argTmp = false;
            argDis = false;
            argLoc = false;
            argMnr = false;
            for (FramesetArgument argument : frameset.getFramesetArguments()){
                DefaultMutableTreeNode argumentNode = new DefaultMutableTreeNode(argument);
                frameNode.add(argumentNode);
                if (word.getArgument() != null && word.getArgument().getId() != null && word.getArgument().getId().equals(frameset.getId()) && word.getArgument().getArgumentType() != null && word.getArgument().getArgumentType().equals(argument.getArgumentType())){
                    selectedNode = argumentNode;
                }
                if (argument.getArgumentType().equals("ARGMTMP")){
                    argTmp = true;
                }
                if (argument.getArgumentType().equals("ARGMLOC")){
                    argLoc = true;
                }
                if (argument.getArgumentType().equals("ARGMDIC")){
                    argDis = true;
                }
                if (argument.getArgumentType().equals("ARGMMNR")){
                    argMnr = true;
                }
            }
            if (!argTmp){
                frameNode.add(new DefaultMutableTreeNode(new FramesetArgument("ARGMTMP", "", "")));
            }
            if (!argLoc){
                frameNode.add(new DefaultMutableTreeNode(new FramesetArgument("ARGMLOC", "", "")));
            }
            if (!argDis){
                frameNode.add(new DefaultMutableTreeNode(new FramesetArgument("ARGMDIS", "", "")));
            }
            if (!argMnr){
                frameNode.add(new DefaultMutableTreeNode(new FramesetArgument("ARGMMNR", "", "")));
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
            pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, 90);
            clickedWord = ((AnnotatedWord)sentence.getWord(selectedWordIndex));
            selectedWordIndex = -1;
            selfSelected = false;
            this.repaint();
        }
    }

    public void previous(int count) {
        while (fileDescription.previousFileExists(count)){
            fileDescription.addToIndex(-count);
            sentence = new AnnotatedSentence(new File(fileDescription.getFileName()));
            if (sentence.containsPredicate()){
                break;
            }
        }
        pane.setVisible(false);
        repaint();
    }

    public void next(int count) {
        while (fileDescription.nextFileExists(count)){
            fileDescription.addToIndex(count);
            sentence = new AnnotatedSentence(new File(fileDescription.getFileName()));
            if (sentence.containsPredicate()) {
                break;
            }
        }
        pane.setVisible(false);
        repaint();
    }



}
