package Annotation.Sentence.PropBank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AutoProcessor.Sentence.Propbank.SentenceAutoArgument;
import AutoProcessor.Sentence.Propbank.TurkishSentenceAutoArgumentWithDependency;
import PropBank.ArgumentList;
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

public class SentencePropBankArgumentPanel extends SentencePropBankPanel {
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private boolean selfSelected = false;
    private final SentenceAutoArgument turkishSentenceAutoArgument;

    /**
     * Constructor for the PropBank argument panel for annotated sentence.
     * @param currentPath The absolute path of the annotated sentence.
     * @param fileName The raw file name of the annotated sentence.
     * @param framesetList Turkish PropBank
     */
    public SentencePropBankArgumentPanel(String currentPath, String fileName, FramesetList framesetList){
        super(currentPath, fileName, framesetList);
        setLayout(new BorderLayout());
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("FrameSets");
        turkishSentenceAutoArgument = new TurkishSentenceAutoArgumentWithDependency(framesetList);
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setVisible(false);
        tree.addTreeSelectionListener(e -> {
            TreePath[] list = tree.getSelectionPaths();
            if (!selfSelected && clickedWord != null && list != null && list.length > 0) {
                if (list.length == 1 && ((DefaultMutableTreeNode)list[0].getLastPathComponent()).getLevel() == 0) {
                    clickedWord.setArgumentList("NONE");
                    sentence.writeToFile(new File(fileDescription.getFileName()));
                } else {
                    for (int i = 0; i < sentence.wordCount(); i++) {
                        AnnotatedWord word = (AnnotatedWord) sentence.getWord(i);
                        if (word.isSelected()){
                            String argumentList = "";
                            for (int j = 0; j < list.length; j++) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) list[j].getLastPathComponent();
                                if (node.getLevel() == 2){
                                    String synSet = (String) ((DefaultMutableTreeNode)node.getParent()).getUserObject();
                                    FramesetArgument argument = (FramesetArgument) node.getUserObject();
                                    if (argumentList.isEmpty()){
                                        argumentList = argument.getArgumentType() + "$" + synSet;
                                    } else {
                                        argumentList += "#" + argument.getArgumentType() + "$" + synSet;
                                    }
                                }
                            }
                            word.setArgumentList(argumentList);
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
     * Automatically detects the FrameNet element tags in the sentence using turkishSentenceAutoArgument.
     */
    public void autoDetect(){
        if (turkishSentenceAutoArgument.autoArgument(sentence)){
            sentence.save();
            this.repaint();
        }
    }

    /**
     * Fills the JList that contains all PropBank arguments for all the predicates in the parse tree.
     * @param sentence Sentence used to populate for the current word.
     * @param wordIndex Index of the selected word.
     * @return Index of the selected tag.
     */
    public int populateLeaf(AnnotatedSentence sentence, int wordIndex){
        boolean argTmp, argLoc, argDis, argMnr;
        ArrayList<DefaultMutableTreeNode> selectedNodes = new ArrayList<>();
        HashSet<Frameset> currentFrameSets = sentence.getPredicateSynSets(framesetList);
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(wordIndex);
        ((DefaultMutableTreeNode)treeModel.getRoot()).removeAllChildren();
        treeModel.reload();
        ArgumentList argumentList = word.getArgumentList();
        for (Frameset frameset : currentFrameSets){
            DefaultMutableTreeNode frameNode = new DefaultMutableTreeNode(frameset.getId());
            ((DefaultMutableTreeNode) treeModel.getRoot()).add(frameNode);
            DefaultMutableTreeNode predicateNode = new DefaultMutableTreeNode(new FramesetArgument("PREDICATE", "", ""));
            frameNode.add(predicateNode);
            if (argumentList != null && argumentList.containsPredicateWithId(frameset.getId())){
                selectedNodes.add(predicateNode);
                selfSelected = true;
            }
            argTmp = false;
            argDis = false;
            argLoc = false;
            argMnr = false;
            for (FramesetArgument argument : frameset.getFramesetArguments()){
                DefaultMutableTreeNode argumentNode = new DefaultMutableTreeNode(argument);
                frameNode.add(argumentNode);
                if (argumentList != null && argumentList.containsArgument(argument.getArgumentType(), frameset.getId())){
                    selectedNodes.add(argumentNode);
                    selfSelected = true;
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
        for (DefaultMutableTreeNode node : selectedNodes){
            tree.addSelectionPath(new TreePath(treeModel.getPathToRoot(node)));
        }
        tree.setVisible(true);
        pane.setVisible(true);
        return -1;
    }

    /**
     * Constructs the JList for options.
     * @param mouseEvent Mouse event to be processed.
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        if (selectedWordIndex != -1){
            populateLeaf(sentence, selectedWordIndex);
            pane.getVerticalScrollBar().setValue(0);
            pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, 300);
            clickedWord = ((AnnotatedWord)sentence.getWord(selectedWordIndex));
            selectedWordIndex = -1;
            selfSelected = false;
            this.repaint();
        }
    }

    /**
     * Displays the previous sentence according to the index of the sentence. For example, if the current
     * sentence fileName is 0123.train, after the call of previous(4), the panel will display 0119.train. If the
     * previous sentence does not exist or the previous sentence does not contain the predicate, nothing will happen.
     * @param count Number of sentences to go backward
     */
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

    /**
     * Displays the next sentence according to the index of the sentence. For example, if the current
     * sentence fileName is 0123.train, after the call of previous(4), the panel will display 0119.train. If the
     * previous sentence does not exist or the previous sentence does not contain the predicate, nothing will happen.
     * @param count Number of sentences to go backward
     */
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
