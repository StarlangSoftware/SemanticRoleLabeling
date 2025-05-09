package Annotation.Sentence.PropBank;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import DataCollector.Sentence.SentenceAnnotatorPanel;
import PropBank.FramesetList;

import java.awt.*;
import java.util.ArrayList;

public class SentencePropBankPanel extends SentenceAnnotatorPanel {
    protected final FramesetList framesetList;

    public SentencePropBankPanel(String currentPath, String rawFileName, FramesetList framesetList) {
        super(currentPath, rawFileName, ViewLayerType.PROPBANK);
        this.framesetList = framesetList;
    }

    @Override
    protected void setWordLayer() {
    }

    /**
     * Sets the width and height of the JList that displays the Frame element tags.
     */
    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + 20, 240, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    /**
     * Sets the space between displayed lines in the sentence.
     */
    @Override
    protected void setLineSpace() {
        lineSpace = 100;
    }

    /**
     * Draws the Frame element tag of the word.
     * @param word Annotated word itself.
     * @param g Graphics on which Frame element tag is drawn.
     * @param currentLeft Current position on the x-axis, where the Frame element tag will be aligned.
     * @param lineIndex Current line of the word, if the sentence resides in multiple lines on the screen.
     * @param wordIndex Index of the word in the annotated sentence.
     * @param maxSize Maximum size in pixels of anything drawn in the screen.
     * @param wordSize Array storing the sizes of all words in pixels in the annotated sentence.
     * @param wordTotal Array storing the total size until that word of all words in the annotated sentence.
     */
    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getArgumentList() != null){
            ArrayList<String> arguments = word.getArgumentList().getArguments();
            int i = 0;
            for (String argument : arguments) {
                g.drawString(argument, currentLeft, (lineIndex + 1) * lineSpace + 25 * (i + 1));
                i++;
            }
        }
    }

    /**
     * Compares the size of the word and the size of the Frame element tag in pixels and returns the maximum of them.
     * @param word Word annotated.
     * @param g Graphics on which Frame element is drawn.
     * @return Maximum of the graphic sizes of word and its Frame element tag.
     */
    @Override
    protected int getMaxLayerLength(AnnotatedWord word, Graphics g) {
        int maxSize = g.getFontMetrics().stringWidth(word.getName());
        if (word.getArgumentList() != null){
            ArrayList<String> arguments = word.getArgumentList().getArguments();
            for (String argument : arguments) {
                int size = g.getFontMetrics().stringWidth(argument);
                if (size > maxSize){
                    maxSize = size;
                }
            }
        }
        return maxSize;
    }

}
