package ui;

import java.awt.event.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import model.JottoModel;

/**
 * Jotto GUI will create a GUI for the player. This will include a table that will keep track of the
 * user's guesses. The GUI will allow the user to generate new puzzles (either randomly or by preference).
 * Helper functions: 
 * generatePuzzleRandom- generate a random puzzle
 * generatePuzzleNumber- create a new game when the JTextField is filled.
 * puzzleNumberGeneratorButton- create a new game when a button is pressed
 * addNewElementToTable- create an entry of the user's guess in the table.
 * UpdateTable- update the table with the response from the server.
 * cancelSwingWorkers- cancel current swing workers if the user started a new game.
 */
public class JottoGUI extends JFrame {
    private JButton newPuzzleButton;
    private JTextField newPuzzleNumber;
    private JLabel puzzleNumber, guessLabel;
    private JTextField guess;
    private JTable guessTable;
    private Random random = new Random();
    private BigInteger puzzleActualNumber = generatePuzzleRandom();// generate a random puzzle
    private JottoModel JM = new JottoModel();
    private String serverRespond,CommonToTable, rightPlaceToTable;
    private int counter = 0;
    private final DefaultTableModel model;
    boolean puzzleWasChanged = false;
    private Vector<SwingWorker> workersVector = new Vector<SwingWorker>();
    private final int maxRandomPuzzleNumber=10001;

    
    /*
     * Helper Functions:
     */
    /**
     * The method will generate a random puzzle number for the user.
     * This method is being call at the beginning of every new game,
     *  or when the user press the new puzzle button. The method return one of 10,000 puzzles
     * @return a puzzle number between 1 and 10001
     */
    private BigInteger generatePuzzleRandom() {
        puzzleActualNumber = BigInteger.valueOf(Math.max(1,random.nextInt(maxRandomPuzzleNumber)));
        return puzzleActualNumber;
    }

    /**
     * The method will generate a puzzle number: 
     * Case 1: if a value was entered to the newPuzzleNumber Jtext field- 
     * the method will generate this puzzle number if this puzzle number is >=0.
     * we generate an invalid puzzle with the ID 0 for purpose of generating an invalid URL (based on the specs.)
     * The error will be added to the table once the user will try to guess
     * Case 2: a negative value will generate a new random puzzle (base on the specs)
     */
    //Changed method name- based on a code review comment
    private void generatePuzzleNumber() {
        String textValue = newPuzzleNumber.getText();
        if (textValue.matches("\\-?[0-9]+")) { //ignore any non numeric value
            if (Long.parseLong(textValue) < 0) { //case2
                puzzleActualNumber = generatePuzzleRandom();
            } else {//case 1
                puzzleActualNumber = BigInteger.valueOf(Long.parseLong(textValue));
            }
            //Clear the table when we start a new game
            model.setRowCount(0);
            counter = 0;
            cancelSwingWorkers();
        }
        puzzleNumber.setText("Puzzle #"+ String.valueOf(puzzleActualNumber));
        newPuzzleNumber.setText("");
    }

    /**
     * Generate a new puzzle when the user clicks the button.
     * Case 1: if the value in the text field is invalid ignore it and generate
     * a new random puzzle 
     * Case 2: if the value in the text field is valid use it to generate a new puzzle.
     */
    private void puzzleNumberGeneratorButton() {
        String textValue = newPuzzleNumber.getText();
        if (textValue == null || textValue.equals("")|| !textValue.matches("[0-9]+")
                || Long.parseLong(textValue) < 0) { //Case 1
            puzzleActualNumber = generatePuzzleRandom();
        } else {// Case 2
            puzzleActualNumber = BigInteger.valueOf(Long.parseLong(textValue));
        }
        newPuzzleNumber.setText("");
        puzzleNumber.setText("Puzzle #" + String.valueOf(puzzleActualNumber));
    }

    /**
     * This method will allow the user to see his/her guess as soon as it was submitted to the server.
     * This will free the GUI for other guess in case of a delay.
     * The method will add a row to the table once the user submitted a guess
     * @param gussedWord - user's guess
     * @param commonLetters - place holder for the answer from the server.
     * @param rightplace - place holder for the answer from the server.
     */
    private void addNewElementToTable(String gussedWord, String commonLetters,
            String rightplace) {
        model.addRow(new Object[] { "Column 1", "Column 2", "Column 3" });
        model.setValueAt(gussedWord, counter, 0);
        model.setValueAt(commonLetters, counter, 1);
        model.setValueAt(rightplace, counter, 2);
        counter++;
        guessTable.repaint();
    }

    /**
     * The method will update the values in a row after a response from the server.
     * @param gussedWord - the word the user guessed
     * @param commonLetters - one out of three options:
     * Case 1: number of common Letters
     * Case 2: a win message- if the guess was the right word 
     * Case 3: an error message- either an invalid guess, ill format URL, or a invalid puzzle ID
     * @param Only in case 1 the number of letters in the right places. In other cases- blank value
     * @param rownum - the row we need to update
     */
    private void UpdateTable(String gussedWord, String commonLetters,
            String rightplace, int rownum) {
        model.setValueAt(gussedWord, rownum, 0);
        model.setValueAt(commonLetters, rownum, 1);
        model.setValueAt(rightplace, rownum, 2);
        guessTable.repaint();
    }

    /**
     * This method will cancel the workers that are running,if the user starts a new game.
     */
    private void cancelSwingWorkers() {
        for (SwingWorker elem : workersVector) {
            elem.cancel(true);
        }
        workersVector.clear(); //clean the vector from the old cancelled workers.
    }
/*
 * Construction of the GUI
 */
    public JottoGUI() {
        super("Jotto Game");
        newPuzzleButton = new JButton("New Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        puzzleNumber = new JLabel("Puzzle #"+ String.valueOf(puzzleActualNumber));
        puzzleNumber.setName("puzzleNumber");
        guess = new JTextField();
        guess.setName("guess");
        guessLabel = new JLabel("Type a guess here:");
        guessLabel.setName("guessLabel");
        guessTable = new JTable(new DefaultTableModel(new Object[] { "Column1", "Column2", "column3" }, 0));
        guessTable.setName("guessTable");
        model = (DefaultTableModel) guessTable.getModel();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(puzzleNumber)
                                        .addComponent(newPuzzleButton)
                                        .addComponent(newPuzzleNumber))
                        .addGroup(layout.createParallelGroup(
                                        GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                        .addComponent(guessLabel)
                                        .addComponent(guess)))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup().addComponent(guessTable)))));
        layout.setVerticalGroup(layout
                .createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                                .addComponent(puzzleNumber)
                                .addComponent(newPuzzleButton)
                                .addComponent(newPuzzleNumber))
                .addGroup(layout.createParallelGroup(
                                GroupLayout.Alignment.BASELINE)
                        .addComponent(guessLabel).addComponent(guess))
                .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(guessTable)));
/*
 * Action listeners
 */
        /*
         * A button listener: It will generate a new puzzle number, 
         * clean the table and cancel all old threads
         */
        newPuzzleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                puzzleNumberGeneratorButton();
                model.setRowCount(0);
                counter = 0;
                cancelSwingWorkers();
            }
        });

        /*
         * A JtextField listener It will generate a new puzzle number, 
         * clean the table and cancel all old threads
         */ 
        newPuzzleNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                generatePuzzleNumber();
            }
        });
/*
 * when the user makes a guess the listener will create a SwingWorker thread.
 * This will allow the user to keep playing even if the server did not return with an answer 
 * doInBackground- will send the user's guess to the table
 * done- will evaluate the server's response and update the table.
 */
        guess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userGuess = guess.getText();
                final int currentRow = model.getRowCount();  // thread safety- confinement of the current row counter
                if (userGuess == null) {}// ignore null guesses 
                else {
                    addNewElementToTable(userGuess, "", "");
                    SwingWorker worker = new SwingWorker<String, Void>() {
                        String userGuess = guess.getText();

                        @Override
                        public String doInBackground() {
                            /*
                             * Case 1: Enter was pressed but no text was entered
                             * in the guess JTextField
                             */
                            if (userGuess == null) {
                                // do nothing probably just enter by mistake
                            }
                            /*
                             * Case 2: A text (valid or invalid) was entered in
                             * the guess JTextField
                             */
                            else { // submit to the server
                                serverRespond = JM.makeGuess(puzzleActualNumber, userGuess);
                            }
                            return serverRespond;
                        }

                        @Override
                        public void done() {
                            try {
                                if (!isCancelled()) { // make sure that we only update the table from 
                                    //Swing Workers that are not cancelled
                                    String serverAnswer = get();
                                    if (serverAnswer.matches("(guess[\\s][0-5][\\s][0-5])")) {
                                        if (serverAnswer.equals("guess 5 5")) {
                                            // the guess was the right guess return a win message
                                            String winText = "You win! The secret word was " + userGuess + "!";
                                            UpdateTable(userGuess, winText, "", currentRow);
                                            /*
                                             * if we did not win return numeric values for common letters
                                             * and right place letters
                                             */
                                        } else {
                                            CommonToTable = serverAnswer.split(" ")[1];
                                            rightPlaceToTable = serverAnswer.split(" ")[2];
                                            UpdateTable(userGuess,CommonToTable,rightPlaceToTable,currentRow);
                                        }
                                    }
                                     //Invalid guess- present an error message
                                    else {
                                        CommonToTable = serverAnswer; // CommonToTable saves the error message
                                        UpdateTable(userGuess, CommonToTable,"", currentRow);
                                    }

                                }
                            } catch (Exception e) {
                                // Handle the Exception
                                e.printStackTrace();
                            }
                        }

                    };
                    guess.setText(""); // clear guess JTextField
                    workersVector.add(worker);// keep track of active workers in vector (thread safe date type) 
                    worker.execute();
                }
            }
        });
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JottoGUI main = new JottoGUI();
                main.setDefaultCloseOperation(main.EXIT_ON_CLOSE); // will close swing by clicking X
                main.pack();
                main.setSize(500, 500);
                main.setVisible(true);

            }
        });

    }
}
