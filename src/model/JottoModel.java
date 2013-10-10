package model;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

/**
 *  The Jotto model class creates the Jotto Model. This class connect to the web server that test the user's
 *  guesses with the toURL method that will return a string from the server.
 *  The Jotto model allows the JottoGui to connect to the server and test a user guess with the 
 *  MakeGuess method that will return either an error (if something with the guess was invalid) or
 *  the number of correct characters' location and the number of common characters.
 */
public class JottoModel {
	private String serverOutput="";
	private final String error2="Invalid guess.";
	private final String error1="Invalid puzzle ID.";
	private final String error0="Ill-formatted URL.";
	private final String URLpath="http://courses.csail.mit.edu/6.005/jotto.py?puzzle=";

	/**
	 * The makeGuess method will take a puzzle number and a string from the user and will send them to
	 * the server using the toURL method. The answer will be either an error message that will be presented
	 * to the user
	 * 1. error 0 - Ill formatted URL
     * 2. error 1 - Invalid puzzle ID
     * 3. error 2 - Invalid guess.
     * Or guess answer that includes the number of common characters, and the number of characters in the right location.
     * guess [0-5] [0-5]= guess [number of letters in common] [number of letters in correct positions].  
	 * @param puzzleNumber- the puzzleNumber for the game
	 * @param guess - a 5 letters word to guess 
	 * @return a string either guess result or an error message.
	 */
	/*
	 * Rep invariant: since we are covering invalid inputs with error messages that will be present to the user
	 * We don't have any restriction on the input to this method.
	 * Guess should be a valid 5 letters word/ or 5 characters that some of them are *- in order to get a 
	 * valid answer from the server.
	 * Any other type of guess will result in either error2 or error0
	 * 
	 * puzzleActualNumber is a valid >0 BigInteger
	 * puzzle number that is equal to 0 will result in error1, and puzzle numbers that are smaller than zero
	 * will be ignored and will lead to the creation of a new valid random puzzle (as specified in the specs)
	 */
	public String makeGuess(BigInteger puzzleActualNumber,String guess) {
	    try {
            serverOutput=toURL(puzzleActualNumber,guess);
        }
	    //This covers connection issues.
	    catch (Exception e) {
            e. printStackTrace();
        }
	    if(serverOutput.contains("error 2:")){
	        return error2;
	    }
	    else if(serverOutput.contains("error 1:")){
	            return error1;
	        }
	    else if(serverOutput.contains("error 0:")){
               return error0;
           }  
        return serverOutput;
	}
	
	/**
	 * The toURL method will send a guess request to the guess server. 
	 * This method is being called from the makeGuess method , that will give it the puzzle number and the guess
	 * The method will return a string from the server that will either be an error message or a guess message.
	 * The method that called it will evaluate this string 
	 * @param puzzleActualNumber- puzzle number to create a URL with
	 * @param guess - string to create a URL with
	 * @return a string with the output from the server.
	 * @throws Exception- throw an exception if there is a problem with the connection.
	 */
	/*
	 * rep invariant- all of the variables values are being forwarded from the makeGuess method
	 * the makeGuess'rep invariant covers all of the possible inputs.
	 */
	private String toURL(BigInteger puzzleActualNumber, String guess) throws Exception{
        URL guessServer = new URL(URLpath+String.valueOf(puzzleActualNumber)+"&guess="+guess.replace(" ", "%20"));
        //added the %20 to deal with the space bug in the server, also addressing a code review comment
        BufferedReader in = new BufferedReader(
        new InputStreamReader(guessServer.openStream()));

        String inputLine;
        String answer="";
        while ((inputLine = in.readLine()) != null)
            answer+=inputLine;
        in.close();
        return answer;
	}
	}
