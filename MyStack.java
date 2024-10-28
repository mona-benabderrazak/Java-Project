/**
 *Project 4
 *My Stack
 *
 *This class detects whether a html file is tag balanced
 *_______________________________________________________
 *Mona Benabderrazak
 *3/29/24
 *CMSC256 Section 001
 */
package cmsc256;

import java.util.*;
import java.io.*;

public class MyStack<E> implements StackInterface<E>{

    public static void main(String[] args){
        File file = new File("9malformedtagpage.html");
        try {
            System.out.println(isBalanced(file));
        } catch (FileNotFoundException ex){
            System.out.println("file not found");
        }
    }
    public static boolean isBalanced(File webpage) throws FileNotFoundException {
        Scanner input = new Scanner(webpage);
        MyStack<String> tagStack = new MyStack<>();
        boolean isBalanced = true;

        while (input.hasNext() && isBalanced == true){

            String tag = null;

            while(tag == null){
                tag = input.findInLine("<\\w+>");
                if (tag == null){
                    tag = input.findInLine("</\\w+>");
                }
                if (tag == null){
                    input.nextLine();
                }
            }

            String openingTag = "";
            String delimiter = "";

            for (int i = 0; i < tag.length(); i++){
                if (!Character.isLetter(tag.charAt(i))){
                    delimiter += tag.charAt(i);
                }
            }

            switch(delimiter){
                case "<>":
                    tagStack.push(tag);
                    break;
                case "</>":
                    if (tagStack.isEmpty()){
                        isBalanced = false;
                    } else {
                        openingTag = tagStack.pop();
                        String s = tag.replace("/", "");
                        if (openingTag.equals(s)){
                            isBalanced = true;
                        } else {
                            isBalanced = false;
                        }
                    }
                    break;
            }
        }
        if (!tagStack.isEmpty()){
            isBalanced = false;
        }

        input.close();

        return isBalanced;
    }

    /**
     * Declare instance variables top, and size
     */
    private Node top;
    private int size;

    /**
     * This method constructs a new object MyStack and sets top
     * to null and size to 0
     */
    public MyStack(){
        top = null;
        size = 0;
    }

    /**
     * This method constructs a new object MyStack with the parameter size
     * @param size
     */
    public MyStack(int size){
        top = null;
        size = 0;
    }

    /**
     * This method adds a new object to the top of the stack.
     * @param newEntry
     */
    @Override
    public void push(E newEntry) {
        if (newEntry == null){
            throw new IllegalArgumentException();
        }
        top = new Node(newEntry, top);
        size++;
    }

    /**
     * This method removes an object from the top of the stack.
     * @return temp
     */
    @Override
    public E pop() {
        if (top == null){
            throw new EmptyStackException();
        }
        E temp = top.data;
        top = top.next;
        size--;
        return temp;
    }

    /**
     * This method returns the object at the top of the stack.
     * @return top.data
     */
    @Override
    public E peek() {
        if (top == null){
            throw new EmptyStackException();
        }
        return top.data;
    }

    /**
     * This method checks if the stack is empty
     * @return true or false
     */
    @Override
    public boolean isEmpty() {
        if (size == 0){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method clears the stack.
     */
    @Override
    public void clear() {
        top = null;
        size = 0;
    }

    private class Node {
        /**
         * Declare instance variables data and next
         */
        private E data;
        private Node next;

        /**
         * This method constructs a new object Node with the parameter dataPortion
         * @param dataPortion
         */
        private Node(E dataPortion)
        {
            this(dataPortion, null);
        }

        /**
         * This method constructs a new object Node with the parameters dataPortion and linkPortion
         * @param dataPortion
         * @param linkPortion
         */
        private Node(E dataPortion, Node linkPortion)
        {
            data = dataPortion;
            next = linkPortion;
        }

        /**
         * This method returns data
         * @return data
         */
        private E getData()
        {
            return data;
        }

        /**
         * This method sets data to newData
         * @param newData
         */
        private void setData(E newData)
        {
            data = newData;
        }

        /**
         * This method returns next
         * @return next
         */
        private Node getNextNode()
        {
            return next;
        }

        /**
         * This method sets next to nextNode
         * @param nextNode
         */
        private void setNextNode(Node nextNode)
        {
            next = nextNode;
        }
    }

}
