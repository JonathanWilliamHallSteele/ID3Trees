import java.util.Stack;

/**
 * Classifier can be thought of as a unique grade for a column of data.
 * Classifier is used in to store and access grade data inside of the data[][] variable.
 * Classifier represents each "class" of data inside of a given column. For example, under the "Math" column, "A+," "B,"
 * and "A" are classifiers. Each classifier holds the name of the grade, the number of occurances, as well as how many
 * of the grade received an A+ in programming.
 *
 * Classifier is most useful when calculating the entropy of a given column, as all the needed information is stored here
 *
 * @author Jon Steele jonwhsteele@gmail.com
 */
public class classifier {

    int occurances = 0;
    String name = ""; //The name refers to the grade
    int aPlus = 0; //This represents how many occurrances of this grade also scored an A+ in programming
    int noAPlus = 0; //This represents how many occurrances of this grade did not score an A+ in programming
    Stack<String[]> data;

    public classifier() {
        data = new Stack<String[]>();
        occurances = 0;
    }

    /**
     * Parameterized constructor for a new classifier object
     * @param newName becomes the String name of this classifier (ie the grade)
     * @param a is the data containing the "A+ in programming" variable (which iterates either aPlus or noAPlus)
     * @param currData is the first set of data for this classifier (and instantly gets pushed onto the data stack)
     * @param col is the column in which this classifier exists (So we know which grades we are looking at)
     */
    public classifier(String newName, String a, String[] currData, int col) {

        data = new Stack<String[]>();

        data.push(foldData(currData.clone(), col)); //saving the data from this first instance
        occurances = 1;
        name = newName;
        if (a.compareTo("Yes") == 0) //iterating aPlus or noAPlus
            aPlus++;
        else {
            noAPlus++;
        }
    }

    /**
     * foldData creates a subset of data[], by removing the inputted column, col.
     * foldData() takes an input String[], and returns a String[] that is smaller by 1.
     * It removes the item in the String[col]. This is because we know the classifier, and don't need the information.
     * @param data the String[] of data we want to be "folded"
     * @param col the column of data we would like removed from data[]
     * @return the inputted data string[], except missing the col'th column
     */
    private String[] foldData(String[] data, int col){

        String[] result = new String[data.length - 1];
        int iteration = 0;

        for (int i = 0; i < data.length; i++){
            if (i == col)
                continue;

            result[iteration] = data[i];
            iteration++;
        }

        return result;
    }

    /**
     * Iterate is used when we have found another instance of this grade (classifier). This means we can iterate
     * the occurance variable, iterate aPlus or noAPlus, as well as save the new data.
     * @param a is the "A+ in programming" data for this row
     * @param insertData is the entire row of data
     * @param col is the column that this grade, or classifier, rests inside.
     */
    public void iterate(String a, String[] insertData, int col) {
        data.push(foldData(insertData, col));
        occurances++;
        if (a.compareTo("Yes") == 0)
            aPlus++;
        else {
            noAPlus++;
        }
    }

    /**
     * Getter method of occurances
     * @return occurances
     */
    public int getOccurances() {
        return occurances;
    }

    /**
     * Getter method of aPlus
     * @return aPlus
     */
    public int getAPlus() {
        return aPlus;
    }

    /**
     * Getter method of noAPlus
     * @return noAPlus
     */
    public int getNoAPlus() {
        return noAPlus;
    }

    /**
     * This method converts all the data in the String[] stack into a String[][], then returns it
     * This method is only used when recursively calling id3(), as the data needs to be in a String[][] format
     * @return A String[][] containing all the data needed for this classifier
     */
    public String[][] getData(){

        String[][] result = new String[data.size()][data.elementAt(0).length];

        for (int row = 0; row < data.size(); row++){
            for (int col = 0; col < data.elementAt(0).length; col++){
                result[row][col] = data.elementAt(row)[col];
            }
        }

        return result;
    }

    public String getName() {
        return name;
    }
}

