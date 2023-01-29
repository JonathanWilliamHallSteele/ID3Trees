import java.util.Stack;

/**
 * Question 4 Programming question [10 Marks]
 * Write a Java program to create a decision tree using ID3 algorithm. Use entropy and information gain calculation to
 * create subtrees. To test your program, use the following dataset table. The dataset is used to predict a student’s
 * grade in first year computer science programming course based on their records in math, statistics, science and
 * English in high school.
 */

/**
 * Main is where the data is interpreted, where the id3 algorithm is called, and where the nodes are printed.
 * @author Jon Steele jonwhsteele@gmail.com
 */
public class main {

    public static void main (String[] args){

        String[][] data = csvReader.getData(); //One mark for accepting CSV file and reading the training set correctly.
        Stack<String> headers = csvReader.getHeaders(); //One mark for accepting CSV file and reading the training set correctly.
        int classifierColumn = data[0].length - 1;

        Node root = new Node();

        root = id3(data, classifierColumn, headers, root);

        System.out.println(root.toString()); //One marks for writing the code that prints out the solution correctly.
    }

    /**
     * id3() is where the bulk of the algorithm is executed. id3() is used in a recursive manner, finding the column
     * with the highest information gain of each node, then creating a hierarchy of nodes to represent the most
     * informative splits. It is helpful when looking to make predictions about new data, based on a set of similar data.
     * @param data is the entire set of data to be analyzed
     * @param informationColumn is the column containing the data we want predictions of. In this case, "A+ in programming"
     * @param headers is the set of all names of columns
     * @param root is the root node. id3 builds the subtree off of this node.
     * @return A node, which links to all other nodes, so the decision tree can be printed, or recursively called
     */
    public static Node id3(String[][] data, int informationColumn, Stack<String> headers, Node root){

        //Six marks for writing the core of the ID3 search,
        // including recursive functions to build the subtrees and exclude visited nodes.

        if (data.length == 1) { //exit condition
            Node node = new Node(dominantLabel(data, informationColumn));
            return node;
        }

        if (singleClassification(data, informationColumn) == true) { //exit condition
            return new Node(data[0][informationColumn]);
        }

        //Finding the next best split (using information gain), assigning it to a node
        //One mark for writing the code that calculates the entropy and information gain correctly.
        Stack<String> newHeaders = (Stack<String>) headers.clone();
        int col = nextBestSplit(data, informationColumn); //Here we use nextBestSplit to find the highest information gain

        Stack<classifier> splitClassifiers = new Stack<classifier>();
        splitClassifiers = getClassifiers(data, col, informationColumn); //here we gather each different options of the given column

        Node parentNode = new Node(headers.elementAt(col)); //node now contains the column name of highest IG
        newHeaders.removeElementAt(col); //removing the header at the column we are splitting

        int numberOfChildren = splitClassifiers.size();
        parentNode.numberOfChildren(numberOfChildren); //declaring the number of different elements in this column

        //This for loop is used to recursively call id3() on each child node of this decision, as well as
        //link the nodes together into a tree.

        for (int i = 0; i < numberOfChildren; i++){

            Node childNode = new Node(splitClassifiers.elementAt(i).getName()); //Instantiating each child node
            parentNode.setChild(childNode, i); //linking each child node to the parent node

            String[][] newData = splitClassifiers.elementAt(i).getData();
            int newInformationColumn = informationColumn - 1;
            childNode.numberOfChildren(1);

            //Here I recursively call id3. It will call the id3 algorithm again on the child node, and
            //the child node's data set. Then, it will return the next best split node
            childNode.setChild(id3(newData, newInformationColumn, newHeaders, childNode), 0);
        }

        return parentNode;
    }

    /**
     * dominantLabel is used to find if A+ or no A+ occurs most
     * @param data the dataset
     * @param classifierCol the column that contains the "A+ in programming" variable
     * @return the dominant one
     */
    private static String dominantLabel(String[][] data, int classifierCol) {

        int yes = 0;
        int no = 0;

        for (int i = 0; i < data.length; i++){
            if (data[i][classifierCol].compareTo("Yes") == 0)
                yes++;
            else
                no++;
        }

        if (yes > no)
            return "Yes";
        else if (yes == no)
            return "Yes";
        else
            return "No";
    }

    /**
     * This simple method will iterate through the classifications of a data array, and will return true if there is
     * only one kind of answer, ie only "Yes's." Otherwise, it returns false. This is used when computing exit
     * conditions in the recursive ID3 algorithm.
     * @param data the data to be analyzed, containing the classifications
     * @param informationColumn the column that contains the classifications
     * @return true, one classification, false, two classifications
     */
    private static boolean singleClassification(String[][] data, int informationColumn) {

        String compare = data[0][informationColumn];

        for (int i = 1; i < data.length; i++){
            if (compare.compareTo(data[i][informationColumn]) != 0)
                return false;
        }

        return true;
    }

    /**
     * nextBestSplit() calculates information gain for each of the columns inputted, and will return the column that
     * contains the highest information gain
     * @param data is the table containing all the data
     * @param informationColumn is the column of data[][] that holds "A plus in programming?"
     * @return the location of the column that has the highest information gain
     */
    private static int nextBestSplit(String[][] data, int informationColumn){

        double overallEntropy = getOverallEntropy(data, informationColumn);
        Stack<classifier> classifiers = new Stack<classifier>();

        double max = 0;
        int maxCol = 0;
        double ig;

        //for every column in data, except the last one, we find the information gain.
        //This is used to determine which decision tree split is best
        for (int i = 0; i < informationColumn; i++){

            classifiers.clear();
            classifiers = getClassifiers(data, i, informationColumn);

            //One mark for writing the code that calculates the entropy and information gain correctly.
            ig = informationGain(classifiers, overallEntropy, data.length);

            if (ig > max) {
                max = ig;
                maxCol = i;
            }
        }

        return maxCol;
    }

    /**
     * informationGain returns the information gain of a column. The column is represented as a stack of classifier objects
     * @param classifiers holds the information of a single column. It is in the form of a stack of a classes called
     *                    classifier. Each classifier holds information needed to calculate entropy and weight.
     * @param overallEntropy holds the overall entropy of the parent
     * @param length tells informationGain() how many data points there are, so it can create ratios (see "weight")
     * @return the information gain, in the form of a double, of the given column of classifiers
     */
    private static double informationGain(Stack<classifier> classifiers, double overallEntropy, int length) { //need to test this

        Stack<classifier> myClasses = (Stack<classifier>) classifiers.clone();
        int size = classifiers.size();
        double averageChildEntropy = 0;
        classifier curr = new classifier();
        double weight = 0;
        double weightedEntropy = 0;

        //for each different value inside this column (classifier),
        //the product of its weight and entropy are summed with every other classifier's weight and entropy.
        for (int i = 0; i < size; i++){

            curr = myClasses.pop();

            weight = (curr.getOccurances() / (double)length);
            weightedEntropy = weight * (double)getEntropy(curr);//One mark for writing the code that calculates the entropy and information gain correctly.

            if (Double.isNaN(weightedEntropy) == false)
                averageChildEntropy = averageChildEntropy + weightedEntropy;
        }

        //One mark for writing the code that calculates the entropy and information gain correctly.
        double totalInformationGain = overallEntropy - averageChildEntropy;

        return totalInformationGain;
    }

    /**
     * Used to iterate a classifier of the classifier stack
     * @param classes is a stack of classifiers. We search for the class with the same name as s, and iterate it.
     * @param s is the name of the class we want to iterate
     */
    private static void iterateClass(Stack<classifier> classes, String s, String aPlus, String[] insertData, int col) {

        for (int i = 0; i < classes.size(); i++){
            if (classes.elementAt(i).getName().compareTo(s) == 0){
                classes.elementAt(i).iterate(aPlus, insertData, col);
                return;
            }
        }

        System.out.println("Class not found. Unable to iterate");
        return;
    }

    /**
     * This method is used to check if a classifier exists.
     * @param classes is a stack of a class called classifier. Classifier holds a name, which is used for comparison
     * @param newClass is the name of the class we want to check if exists
     * @return a boolean statement saying if the class exists or not
     */
    private static boolean classExists(Stack<classifier> classes, String newClass) { //need to test this

        for (int i = 0; i < classes.size(); i++){
            if(classes.elementAt(i).getName().compareTo(newClass) == 0){ return true; }
        }

        return false;
    }

    /**
     * getClassifiers creates a Stack<classifier> object, fills it with all the classes and how many instances exits,
     * then returns this stack
     * @param data holds all the data. Each column holds a different kind of data.
     * @param col is the column of data we want to break apart into classifiers
     * @param classifierColumn is the column containing "A+ in programming"
     * @return
     */
    private static Stack<classifier> getClassifiers(String[][] data, int col, int classifierColumn){

        Stack<classifier> classes = new Stack<classifier>();

        //for each row of data, we need to create a new class, or iterate a new class
        //this loop is
        for (int row = 0; row < data.length; row++){

            if (classExists(classes, data[row][col]) == false){ //if the class doesn't exist, a new one is created
                classes.push(new classifier(data[row][col], data[row][classifierColumn], data[row], col));
            }

            else{  //if the class exists, we iterate that class.
                iterateClass(classes, data[row][col], data[row][classifierColumn], data[row], col);
            }
        }

        return classes;
    }

    /**
     * Returns the entropy of a given classifier.
     * @param classifier is obtained by removing each different kind of class in a given column
     * @return the entropy of the classifier
     */
    public static double getEntropy(classifier classifier){ //One mark for writing the code that calculates the entropy and information gain correctly.

        classifier curr = classifier;

        double ratioAPlus = ((double)curr.getAPlus()/(curr.getAPlus()+curr.getNoAPlus()));
        double ratioNoAPlus = ((double)curr.getNoAPlus()/(curr.getAPlus()+curr.getNoAPlus()));

        return (-ratioAPlus * log2(ratioAPlus)) + (-ratioNoAPlus * log2(ratioNoAPlus));
    }

    private static double getOverallEntropy(String[][] data, int col) {

        int aPlusCount = 0;
        int noAPlusCount = 0;

        for (int i = 0; i < data.length; i++){

            if (data[i][col].compareTo("Yes") == 0)
                aPlusCount++;
            else
                noAPlusCount++;
        }

        double ratioAPlus = (double)aPlusCount / data.length;
        double ratioNoAPlus = (double)noAPlusCount / data.length;

        return (-ratioAPlus * log2(ratioAPlus)) + (-ratioNoAPlus * log2(ratioNoAPlus));
    }

    /**
     * This is a log base 2 function
     * @param number
     * @return
     */
    public static double log2(double number){
        return (double)(Math.log(number) / Math.log(2));
    }
}
