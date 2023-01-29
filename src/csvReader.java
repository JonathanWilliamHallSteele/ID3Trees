/**
 * @author Jon Steele jonwhsteele@gmail.com
 */
import java.util.Scanner;
import java.io.*;
import java.util.Stack;

/**
 * csvReader is used to extracting data from a .csv file. This .csv file is pulled from the path under csvFileName,
 * on line 18. Data is extracted with the getData() method call, and returns a String[][] holding all the data, except
 * for the headers. Headers can be extracted using the getHeaders() method call.
 *
 * @author Jon Steele jonwhsteele@gmail.com
 */
public class csvReader {

    //This is where csvReader will read data from. Change path here
    static String csvFileName = "src/Grades.csv";

    /**
     * getData goes into the .csv file under the path csvFileName, and extracts the data, saving it to a String[][]
     * @return a String[][] containing the .csv information under the path csvFileName
     */
    public static String[][] getData() { //One mark for accepting CSV file and reading the training set correctly.

        Stack<String> data = new Stack();
        String tmp = "";
        String line = "";
        Scanner scan;

        {
            try {
                scan = new Scanner(new File(csvFileName));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        while(scan.hasNextLine()) {
            line = scan.nextLine();
            data.push(line);
        }

        int numberOfDataPoints = data.size() - 1;
        int dataPointLength = 5; //I am assuming that the number of variables in each data point will remain 4.
        int col = 0;

        String[][] dataArray = new String[numberOfDataPoints][dataPointLength];

        for (int row = 0; row < numberOfDataPoints; row++){

            line = data.pop();
            tmp = "";
            col = 0;

            for (int character = 0; character < line.length(); character++){

                if (line.charAt(character) == ',' || character == line.length() - 1) {

                    if (character == line.length() - 1) { //if it's the end of the string, we concatenate
                        dataArray[row][col] = tmp + line.charAt(character);
                    }

                    else{ //if it is a comma, we don't want to concatenate the comma to the entry
                        dataArray[row][col] = tmp;
                    }
                    col++;
                    tmp = "";
                    continue;
                }

                tmp = tmp + line.charAt(character);

            }
        }
        return dataArray;
    }

    /**
     * getHeaders goes into the .csv file under the path csvFileName, and extracts the header data, saving it to a
     * Stack<String>
     * @return a Stack<String> containing the .csv information under the path csvFileName
     */
    public static Stack<String> getHeaders(){ //One mark for accepting CSV file and reading the training set correctly.

        Stack<String> header = new Stack<>();
        Scanner scan;
        String tmp = "";
        String line = "";

        {
            try {
                scan = new Scanner(new File(csvFileName));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        line = scan.nextLine();

        int col = 0;
        for (int i = 0; i < line.length(); i++) {

            if (line.charAt(i) == ',' || i == line.length() - 1) {

                if (i == line.length() - 1) { //if it's the end of the string, we concatenate
                    tmp = tmp + line.charAt(i);
                    header.push(tmp);
                }
                else { //if its a comma, we dont want to concatenate
                    header.push(tmp);
                }

                col++;
                tmp = "";
                continue;
            }

            tmp = tmp + line.charAt(i);
        }

        return header;
    }
}
