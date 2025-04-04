package Annotation.Sentence.PropBank;

import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.AnnotatedCorpus;
import DataCollector.Sentence.ViewSentenceAnnotationFrame;
import PropBank.FramesetList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewSentencePropBankPredicateAnnotationFrame extends ViewSentenceAnnotationFrame implements ActionListener {

    private FramesetList framesetList;

    /**
     * Updates the predicate tag for the selected sentences.
     * @param e Action event to be processed.
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (PASTE.equals(e.getActionCommand())) {
            if (selectedRow != -1) {
                for (int rowNo : dataTable.getSelectedRows()) {
                    updatePropBankPredicate(rowNo, data.get(selectedRow).get(TAG_INDEX));
                }
            }
        }
        dataTable.invalidate();
    }

    public class PropBankPredicateTableDataModel extends TableDataModel {

        /**
         * Returns the name of the given column.
         * @param col  the column being queried
         * @return Name of the given column
         */
        public String getColumnName(int col) {
            switch (col) {
                case FILENAME_INDEX:
                    return "FileName";
                case WORD_POS_INDEX:
                    return "Index";
                case WORD_INDEX:
                    return "Word";
                case 3:
                    return "Predicate";
                case 4:
                    return "Sentence";
                default:
                    return "";
            }
        }

        /**
         * Updates the predicate tag for the sentence in the given cell.
         * @param value   value to assign to cell
         * @param row   row of cell
         * @param col  column of cell
         */
        public void setValueAt(Object value, int row, int col) {
            if (col == TAG_INDEX && !data.get(row).get(TAG_INDEX).equals(value)) {
                updatePropBankPredicate(row, (String) value);
            }
        }
    }

    /**
     * Sets the value in the data table. After finding the corresponding sentence in that row, updates the propbank
     * layer of that word associated with that row.
     * @param row Index of the row
     * @param newValue Predicate tag to be assigned.
     */
    private void updatePropBankPredicate(int row, String newValue){
        data.get(row).set(TAG_INDEX, newValue);
        AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(Integer.parseInt(data.get(row).get(COLOR_COLUMN_INDEX - 1)));
        AnnotatedWord word = (AnnotatedWord) sentence.getWord(Integer.parseInt(data.get(row).get(WORD_POS_INDEX)) - 1);
        word.setArgumentList(newValue);
        sentence.save();
    }

    /**
     * Constructs the data table. For every sentence, the columns are:
     * <ol>
     *     <li>Annotated sentence file name</li>
     *     <li>Index of the word</li>
     *     <li>Word itself</li>
     *     <li>Predicate tag of the word if it exists, NONE otherwise</li>
     *     <li>Annotated sentence itself</li>
     *     <li>Sentence index</li>
     * </ol>
     * @param corpus Annotated PropBank corpus
     */
    protected void prepareData(AnnotatedCorpus corpus){
        data = new ArrayList<>();
        for (int i = 0; i < corpus.sentenceCount(); i++){
            AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
            for (int j = 0; j < corpus.getSentence(i).wordCount(); j++){
                AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                ArrayList<String> row = new ArrayList<>();
                row.add(sentence.getFileName());
                row.add("" + (j + 1));
                row.add(word.getName());
                if (word.getArgumentList() != null && word.getArgumentList().containsPredicate()){
                    row.add(word.getArgumentList().toString());
                } else {
                    if (word.getSemantic() != null && framesetList.frameExists(word.getSemantic())){
                        row.add("NONE");
                    } else {
                        row.add("");
                    }
                }
                row.add(sentence.toWords());
                row.add("" + i);
                row.add("0");
                data.add(row);
            }
        }
    }

    /**
     * Constructs Propbank annotation frame viewer. Arranges the minimum width, maximum width or with of every column. If the
     * user double-clicks any row, the method automatically creates a new panel showing associated annotated sentence.
     * @param corpus Annotated PropBank corpus
     * @param sentencePropBankPredicateFrame Frame in which new panels will be created, when the user double-clicks a row.
     */
    public ViewSentencePropBankPredicateAnnotationFrame(AnnotatedCorpus corpus, FramesetList framesetList, SentencePropBankPredicateFrame sentencePropBankPredicateFrame){
        super(corpus);
        this.framesetList = framesetList;
        COLOR_COLUMN_INDEX = 6;
        TAG_INDEX = 3;
        prepareData(corpus);
        dataTable = new JTable(new PropBankPredicateTableDataModel());
        dataTable.getColumnModel().getColumn(FILENAME_INDEX).setMinWidth(150);
        dataTable.getColumnModel().getColumn(FILENAME_INDEX).setMaxWidth(150);
        dataTable.getColumnModel().getColumn(WORD_POS_INDEX).setMinWidth(60);
        dataTable.getColumnModel().getColumn(WORD_POS_INDEX).setMaxWidth(60);
        dataTable.getColumnModel().getColumn(WORD_INDEX).setWidth(200);
        dataTable.getColumnModel().getColumn(TAG_INDEX).setWidth(200);
        dataTable.setDefaultRenderer(Object.class, new CellRenderer());
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2){
                    int row = dataTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String fileName = data.get(row).get(0);
                        AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(Integer.parseInt(data.get(row).get(COLOR_COLUMN_INDEX - 1)));
                        sentencePropBankPredicateFrame.addPanelToFrame(sentencePropBankPredicateFrame.generatePanel(sentence.getFile().getParent(), fileName), fileName);
                    }
                }
            }
        });
        JScrollPane tablePane = new JScrollPane(dataTable);
        add(tablePane, BorderLayout.CENTER);
    }

}
