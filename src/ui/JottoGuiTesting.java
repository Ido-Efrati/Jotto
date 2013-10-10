package ui;

import org.junit.Test;

public class JottoGuiTesting {
/*
 * This file will document all of the manual testing for the GUI part of the pset:
 */
    
    //Puzzle Number testing:
    /*
     * Valid Cases- all tests will update the JLabel of Puzzle Number:
     * Test that every game start with a random puzzle number
     * Test that a click on New Puzzle button will create a new random puzzle 
     * Test that an enter on the empty JTextField will not create a new random puzzle
     * Test that an enter on a JTextField with a value will create the right puzzle and update the JLabel.
     * Test that a click on a New Puzzle button with a value in the JTextField will create the right puzzle and update the Jlabel.
     * 
     * Invalid cases:
     * Try to put invalid null, empty string, char, string, should do nothing
     * Test for puzzle 0, should create the puzzle and will fail later in the table
     * Test that negative numbers generate a random puzzle
     */
    
    //Table testing:
    /*
     * Valid Cases:
     * guess with 0 0
     * guess with some right letters and some common for example 1 1
     * guess with a win guess 5 5 , will present "win message" in the table
     * clear table after generating a puzzle both random and non-random
     * 
     * 
     * Invalid Cases:
     * Invalid guess:
     * word (length 5) that does not exist- will present an error in the table
     * word too short (length< 5)-  will present an error in the table
     * word too long (length >5)- will present an error in the table
     * guess with a space in it "bir ds"
     * guess with upper case word
     * guess of a valid word and a space after it "cargo birds"
     * 
     * Invalid puzzle ID
     * puzzle 0 try to guess will put an invalid puzzle ID error in table
     * 
     * Ill format URL
     * Try to guess a space " "- will put an ill format URL error in table
     *Try to guess an empty guess ""- will put an ill format URL error in table
     */
    
    //Delay testing
    /*
     * submit a guess with an * for example carg* or *bean. Make sure you can add a new guess.
     * add guess without an * and with * and again without * , make sure all will be updated.
     * guess with *, start a new game using a button. add a new guess make sure it won't get override with the old guess
     * guess with *, start a new game using a text field. add a new guess make sure it won't get override with the old guess
     * guess with * and also bunch of invalid guesses just to make sure that the order is saved for invalid values as well
     */

    @Test
    public void dummyTest() {
    }

}
