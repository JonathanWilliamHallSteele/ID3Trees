import java.util.Iterator;

/**
 * Node is used to create, link and print data in a tree structure.
 * @author Jon Steele jonwhsteele@gmail.com
 */
public class Node {

    String name;
    Node[] children;
    boolean noChildren = true;

    /**
     * Unparameterized constructor
     */
    public Node(){
    }

    /**
     * Parameterized constructor
     * @param name is a string value that this node holds, ie "A+," or "English"
     */
    public Node(String name){
        this.name = name;
    }

    /**
     * numberOfChildren declares the size of the children array
     * @param number the number of children
     */
    public void numberOfChildren(int number){
        children = new Node[number];
        noChildren = false;
    }

    /**
     * This method inserts a child node into this node's child array
     * @param child the child node to be inserted
     * @param place the space in the child array for the child node to be inserted
     */
    public void setChild(Node child, int place){
        children[place] = child;
        noChildren = false;
    }

    /**
     * This method returns the string of this nodes subtree
     * @return A string of the subtree
     */
    public String toString() {

        //One marks for writing the code that prints out the solution correctly.

        StringBuilder buffer = new StringBuilder(100);
        print(buffer, "", "");
        return buffer.toString();
    }

    /**
     * This method is used to print a diagram of the subtree starting at this node. It is recursively called
     * until the exit conditions are met.
     * @param buffer is used to easily append information to the resulting string
     * @param prefix is used to append the prefix to the resulting string
     * @param childrenPrefix is used to append the children prefix to the resulting string
     */
    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {

        //One marks for writing the code that prints out the solution correctly.

        int childrenSize;

        if (children == null)
            childrenSize = 0;
        else
            childrenSize = children.length;

        buffer.append(prefix);
        buffer.append(name);
        buffer.append('\n');

        for (int i = 0; i < childrenSize; i++) {
            Node current = children[i];
            if (i < children.length - 1) {
                current.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                current.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }

}
