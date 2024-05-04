package Annotation.Sentence.FrameNet;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.ViewLayerType;
import DataCollector.Sentence.SentenceAnnotatorPanel;

import java.awt.*;
import java.util.ArrayList;

public class SentenceFrameNetPanel extends SentenceAnnotatorPanel {

    public SentenceFrameNetPanel(String currentPath, String rawFileName, ViewLayerType layerType) {
        super(currentPath, rawFileName, layerType);
    }

    /**
     * Updates the FrameNet layer of the annotated word.
     */
    @Override
    protected void setWordLayer() {
        clickedWord.setFrameElement(list.getSelectedValue().toString());
    }

    /**
     * Sets the width and height of the JList that displays the Frame element tags.
     */
    @Override
    protected void setBounds() {
        pane.setBounds(((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getX(), ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getY() + ((AnnotatedWord)sentence.getWord(selectedWordIndex)).getArea().getHeight(), 120, (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.4));
    }

    /**
     * Sets the space between displayed lines in the sentence.
     */
    @Override
    protected void setLineSpace() {
        lineSpace = 80;
    }

    /**
     * Draws the Frame element tag of the word.
     * @param word Annotated word itself.
     * @param g Graphics on which NER tag is drawn.
     * @param currentLeft Current position on the x-axis, where the Frame element tag will be aligned.
     * @param lineIndex Current line of the word, if the sentence resides in multiple lines on the screen.
     * @param wordIndex Index of the word in the annotated sentence.
     * @param maxSize Maximum size in pixels of anything drawn in the screen.
     * @param wordSize Array storing the sizes of all words in pixels in the annotated sentence.
     * @param wordTotal Array storing the total size until that word of all words in the annotated sentence.
     */
    @Override
    protected void drawLayer(AnnotatedWord word, Graphics g, int currentLeft, int lineIndex, int wordIndex, int maxSize, ArrayList<Integer> wordSize, ArrayList<Integer> wordTotal) {
        if (word.getFrameElement() != null){
            String correct = word.getFrameElement().getFrameElementType();
            g.drawString(correct, currentLeft, (lineIndex + 1) * lineSpace + 30);
            String id = word.getFrameElement().getId();
            g.drawString(id, currentLeft, (lineIndex + 1) * lineSpace + 50);
        }
    }

    /**
     * Compares the size of the word and the size of the frame element tag in pixels and returns the maximum of them.
     * @param word Word annotated.
     * @param g Graphics on which Frame element is drawn.
     * @return Maximum of the graphic sizes of word and its Frame element tag.
     */
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

}
