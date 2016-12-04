/**
 * @(#)CommentAdderPlusPlusPlus.java
 *
 * offers the user the choice to add comments to their code themselves
 * in an efficent and neat way, or the choice to have the program autocomment
 * their code, using a MVP method that adds basic comments to each line
 * determined by key words found in the code
 *
 * @author Christy Morin and Jonathan Kim
 * @version 1.00 2016/12/3
 */




import java.util.*;
import javax.swing.*;
import java.io.*;


public class CommentAdderPlusPlus  extends JPanel {
	
	private static File file, data;
	private static Scanner scanner, scanner_data;
	private static int numberOfVariables, numberOfMethods;
	
	public static void main(String args[]) throws IOException {
			
			JFileChooser jfc = new JFileChooser();			//creates new file chooser object and database list to choose comments from
			data = new File("C:/Users/nicole/Documents/comments.txt");
			
			do{						//runs through loop until user hits cancel, exit, or enters a valid file name
				int returnVal = jfc.showDialog(jfc, "Open Code to Comment");
				if(returnVal == 1)
					return;
				try {
					file = jfc.getSelectedFile();
					if(!file.exists())
						JOptionPane.showMessageDialog(null, "Error: invalid file choice.");
				}
				catch (NullPointerException npe){return;}
			}while(!file.exists());
			
			Object[] options = {"Auto-comment", "Comment manually", "Quit CommentAdder++"};
	
			int n = JOptionPane.showOptionDialog(null,"Choose an action.", "CommentAdder++", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		    	
			if(n == 0)
				selfComment(file);
			else if	(n == 1)
				userComment(file);
			else
				JOptionPane.showMessageDialog(null, "Thanks for using CommentAdder++!", "CommentAdder++", JOptionPane.PLAIN_MESSAGE);
	}
	public static void userComment(File code) throws IOException
	{
		scanner = new Scanner(code);
		String output = "", line;
			while(scanner.hasNextLine()) { //loop that continues until end of file
				line = scanner.nextLine(); //sets line of code to string line
				String jtext = (String)JOptionPane.showInputDialog(null, line, "Click OK to continue or CANCEL to exit program.", JOptionPane.PLAIN_MESSAGE); //displays GUI
				try { //tries its best
					if(jtext.equals("") == false) //if user inputs nothing
						line += " //" + jtext; //adds comment to line
					output += line + "\n"; //adds line to output with return
				}
				catch(NoSuchElementException n){ //when the file ends, break
					break; 
				}
				catch(NullPointerException e){return;} //null pointers
			}
			PrintWriter writer = new PrintWriter(code); //prints onto file
			System.out.println(output); //prints new file into command line
			writer.print(output); //writes onto file
			writer.close(); //closes printer
	}
	public static void selfComment(File code) throws IOException
	{
		numberOfVariables = 0; //get rekt
		numberOfMethods = 0;
		scanner = new Scanner(code);
		scanner_data = new Scanner(data);
		String line = "", line2 = "", word = "", variable_name = "", var_two = "", after_the_equals = "", comment ="", output = "";
		
		while(scanner.hasNextLine()) {	//gets total number of methods in code to be commented
			line = scanner.nextLine();
			System.out.println(line);
			while(scanner_data.hasNextLine()) {
				line2 = scanner_data.nextLine();
				try {
					if(line.contains(line2.substring(0, line2.indexOf(" :"))))
						numberOfMethods++;
				}
				catch(StringIndexOutOfBoundsException s){}
				
			}
			scanner_data = new Scanner(data);
		}
		scanner = new Scanner(code);
		while(scanner.hasNextLine()) {	//gets total number of variables in code to be commented
			line = scanner.nextLine();
			while(scanner_data.hasNextLine()) {
				line2 = scanner_data.nextLine();
				if(line.contains(line2))
					numberOfVariables++;
			}
			scanner_data = new Scanner(data);
		}
		System.out.println(numberOfMethods);
		System.out.println(numberOfVariables);
		/* following code iterates through the entire code,
		 * adding relevant comments to each line
		 */
		scanner = new Scanner(code);
		for(int i = 0; i < numberOfVariables; i++) {	
			while(scanner.hasNextLine()) {
				variable_name = "";
				after_the_equals = "";
				var_two = "";
				line = scanner.nextLine();
				word = getWord(line);
				String meth = getMethod(line);
				if(meth != "")
					comment = forMethods(meth);
				if(word != ""){
					variable_name = line.substring(line.indexOf(word) + word.length());//variable name till end of line
					System.out.println(variable_name); //debug
					if(line.contains("="))
						after_the_equals = " is set to " + getStupidValue(variable_name); //get value that variable is set to, add it to comment
					variable_name = " //" + variable_name.substring(0, variable_name.indexOf(" "));//only variable name, nothing else		
				}	
				comment = variable_name + after_the_equals; 
				System.out.println(comment); //debug
				output += line + comment + "\n";	//creates full comment at the end of the line of code and adds it to output
				line = line.substring(line.indexOf(word)+word.length());
		}
			PrintWriter writer = new PrintWriter(code);	//rewrites the full code with comments
			writer.print(output);
			writer.close();
		}
	}
	public static String getWord(String line) throws IOException	//returns key word that determines comment
	{
		scanner_data = new Scanner(data);
		String word;
		while(scanner_data.hasNextLine()){
			word = scanner_data.nextLine();
			if(line.contains(word))
			{
				System.out.println(word + " found getWord");
				return word;
			}
		}
		return "";
	}
	public static String getStupidValue(String var_line)	//returns value that the variable in the code is set to
	{
		String after_equals = var_line.substring(var_line.indexOf("=")+1);
		after_equals = after_equals.replace(" ", "");
		return after_equals.substring(0, after_equals.indexOf(";"));
	}
	public static String forMethods(String meth) throws IOException {		//add comments to methods explaining them
		scanner_data = new Scanner(data);
		String line;
		System.out.println(meth);
		while(scanner_data.hasNextLine()) {
			line = scanner_data.nextLine();
			if(line.contains(meth))
			{
				String output = " //" +  line.substring(line.indexOf(":")).replace("x", line.substring(line.indexOf("(") + 1, line.indexOf(")")));
				System.out.println(output);
				return output;
			}
		}
		return "";
	}
	public static String getMethod(String line) throws IOException
	{
		scanner_data = new Scanner(data);
		String word;
		while(scanner_data.hasNextLine()){
			word = scanner_data.nextLine();
			if(word.contains(":"))
			{
				if(line.contains(word.substring(0, word.indexOf(" :"))))
				System.out.println(word + " found getMethod");
				return word;
			}
		}
		return "";
	}


	
}








