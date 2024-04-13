package Annotation.ParseTree.PropBank;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import DataCollector.ParseTree.TreeLeafEditorPanel;

import java.awt.*;

public class TreePropBankPanel extends TreeLeafEditorPanel {

    public TreePropBankPanel(String path, String fileName, ViewLayerType viewLayer, boolean defaultFillEnabled) {
        super(path, fileName, viewLayer, defaultFillEnabled);
    }

    /**
     * The size of the string displayed. If it is a leaf node, it returns the size of the propbank argument. Otherwise,
     * it returns  the size of the symbol in the node.
     * @param parseNode Parse node
     * @param g Graphics on which tree will be drawn.
     * @return Size of the string displayed.
     */
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

    /**
     * If the node is a leaf node, it draws the word and the propbank argument tag of the word. Otherwise, it draws the
     * node symbol.
     * @param parseNode Parse Node
     * @param g Graphics on which symbol is drawn.
     * @param x x coordinate
     * @param y y coordinate
     */
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

    /**
     * Sets the size of the enclosing area of the parse node (for selecting, editing etc.).
     * @param parseNode Parse Node
     * @param x x coordinate of the center of the node.
     * @param y y coordinate of the center of the node.
     * @param stringSize Size of the string in terms of pixels.
     */
    protected void setArea(ParseNodeDrawable parseNode, int x, int y, int stringSize){
        parseNode.setArea(x - 5, y - 15, stringSize + 10, 20);
    }

}
