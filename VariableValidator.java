package cmsc256;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map;

public class VariableValidator implements ProgramParserInterface{

    public static void main (String[] args){
        VariableValidator test = new VariableValidator("JavaKeywordList.txt", "MathExpressions2.java");
        try {
            AVLTree<String> tree = test.createKeywordTree();
            System.out.println(test.inorderTraversal(tree.getRoot()));
            System.out.println();
            System.out.println(test.preorderTraversal(tree.getRoot()));
            System.out.println();
            System.out.println(test.postorderTraversal(tree.getRoot()));
            Map<String, Integer> newMap = test.getValidJavaIdentifiers();
            for (Map.Entry<String, Integer> entry : newMap.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            System.out.println();
            Map<String, List<Integer>> secondMap = test.getInvalidJavaIdentifiers();
            for (Map.Entry<String, List<Integer>> entry : secondMap.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
            System.out.println();
            for (String key: secondMap.keySet()){
                System.out.println(key);
            }
            System.out.println();
            for (List<Integer> list: secondMap.values()){
                System.out.println(list);
            }
        } catch (FileNotFoundException ex){
            System.out.println("file not found");
        } catch (IllegalArgumentException ex){
            System.out.println("invalid file");
        }


    }
    private File keywordFile;
    private File javaFile;
    private AVLTree<String> reservedWords;

    public VariableValidator(String keywordFileName){
        setKeywordFile(keywordFileName);
    }

    public VariableValidator(String keywordFileName, String javaFileName){
        setKeywordFile(keywordFileName);
        setJavaFile(javaFileName);
    }
    @Override
    public String getJavaFileName() {
        return javaFile.getName();
    }

    @Override
    public void setJavaFile(String javaFileName) {
        if (javaFileName == null){
            throw new IllegalArgumentException();
        }
        javaFile = new File(javaFileName);
        if (!javaFile.exists()){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String getKeywordFileName() {
        return keywordFile.getName();
    }

    @Override
    public void setKeywordFile(String keywordFileName) {
        if (keywordFileName == null){
            throw new IllegalArgumentException();
        }
        keywordFile = new File(keywordFileName);
        if (!keywordFile.exists()){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public AVLTree<String> createKeywordTree() throws FileNotFoundException {
        Scanner input = new Scanner(keywordFile);
        reservedWords = new AVLTree<>();

        while (input.hasNext()){
            String s = input.nextLine();
            reservedWords.insert(s.trim());
        }
        input.close();
        return reservedWords;
    }

    public String inorderTraversal(AVLTree.AVLNode node){
        String s = "";
        if (node != null){
            s += inorderTraversal(node.getLeft());
            s += node.getElement() + " ";
            s += inorderTraversal(node.getRight());
        }
        return s;
    }
    @Override
    public String getInorderTraversal() {
        String s = "";
        try{
            AVLTree<String> words = createKeywordTree();
            s = inorderTraversal(words.getRoot());
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }
        return s;
    }

    public String preorderTraversal(AVLTree.AVLNode node){
        String s = "";
        if (node != null){
            s += node.getElement() + " ";
            s += preorderTraversal(node.getLeft());
            s += preorderTraversal(node.getRight());
        }
        return s;
    }
    @Override
    public String getPreorderTraversal() {
        String s = "";
        try{
            AVLTree<String> words = createKeywordTree();
            s = preorderTraversal(words.getRoot());
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }
        return s;
    }

    public String postorderTraversal(AVLTree.AVLNode node){
        String s = "";
        if (node != null){
            s += postorderTraversal(node.getLeft());
            s += postorderTraversal(node.getRight());
            s += node.getElement() + " ";
        }
        return s;
    }
    @Override
    public String getPostorderTraversal() {
        String s = "";
        try{
            AVLTree<String> words = createKeywordTree();
            s = postorderTraversal(words.getRoot());
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }
        return s;
    }

    @Override
    public Map<String, Integer> getValidJavaIdentifiers() throws FileNotFoundException {
        AVLTree<String> words = createKeywordTree();
        String wordsString = inorderTraversal(words.getRoot());
        String[] reservedWordsList = wordsString.split(" ");

        Map<String, Integer> validJavaIndentifiers = new HashMap<>();

        Scanner input = new Scanner(javaFile);

        while(input.hasNext()){
            String s = input.nextLine();

            while (s.contains("\"")){
                int begin = s.indexOf("\"");
                int end = s.indexOf("\"", begin + 1);
                s = s.substring(0, begin) + s.substring(end + 1);
            }

            while (s.contains("'")){
                int begin = s.indexOf("'");
                int end = s.indexOf("'", begin + 1);
                s = s.substring(0, begin) + s.substring(end + 1);
            }

            if (s.contains("/*")){
                s = input.nextLine();
                do {
                    s = input.nextLine();
                } while (!s.contains("*/"));
            }

            if (s.contains("//")){
                int j = s.indexOf("//");
                if (j == 0){
                    s = "";
                } else {
                    s = s.substring(0, j);
                }
            }

            for (int i = 0; i < s.length(); i++){
                if (!Character.isLetter(s.charAt(i)) && !Character.isDigit(s.charAt(i))){
                    String temp = s.substring(i, i + 1);
                    if (!temp.equals("_") && !temp.equals("$")){
                        s = s.replace(temp, " ");
                    }
                }
            }

            String[] variables = s.split("\\s+");

            for (int i = 0; i < variables.length; i++){

                boolean isReserved = false;
                for (String str: reservedWordsList){
                    if (variables[i].equals(str)){
                        isReserved = true;
                        break;
                    }
                }

                if (variables[i].length() == 1){  //Check if it is only one letter
                    if (Character.isLetter(variables[i].charAt(0))){
                        if (validJavaIndentifiers.containsKey(variables[i])){
                            validJavaIndentifiers.put(variables[i], validJavaIndentifiers.get(variables[i]) + 1);
                        } else {
                            validJavaIndentifiers.put(variables[i], 1);
                        }
                    }
                } else if (!isReserved && !variables[i].isEmpty()){
                    String[] validVariables = {"Scanner", "System", "String", "Integer", "Character", "File", "Double", "Int", "Char"};
                    char firstLetter = variables[i].charAt(0);
                    if (Character.isUpperCase(firstLetter)){   //Check if the first letter is upperCase
                        if (i >= 1){
                            String variable = variables[i - 1];
                            if (variable.equals("class") || variable.equals("implements") || variable.equals("extends")){
                                if (validJavaIndentifiers.containsKey(variables[i])){
                                    validJavaIndentifiers.put(variables[i], validJavaIndentifiers.get(variables[i]) + 1);
                                } else {
                                    validJavaIndentifiers.put(variables[i], 1);
                                }
                            } else {
                                boolean add = false;
                                for (int k = 0; k < validVariables.length; k++){
                                    if (validVariables[k].equals(variables[i])){
                                        add = true;
                                        break;
                                    }
                                }
                                if (add){
                                    if (validJavaIndentifiers.containsKey(variables[i])){
                                        validJavaIndentifiers.put(variables[i], validJavaIndentifiers.get(variables[i]) + 1);
                                    } else {
                                        validJavaIndentifiers.put(variables[i], 1);
                                    }
                                }
                            }
                        }
                    }
                    if (i >= 2 && (variables[i - 2].equals("final"))){
                        boolean validFinal = true;
                        for (int y = 0; y < variables[i].length(); y++){
                            char character = variables[i].charAt(y);
                            if (!Character.isLetter(character) || !Character.isUpperCase(character)){
                                if (character != '_'){
                                    validFinal = false;
                                } else {
                                    if (y == 0 || y == variables[i].length() - 1){
                                        validFinal = false;
                                    }
                                }
                            }
                        }
                        if (validFinal){
                            if (validJavaIndentifiers.containsKey(variables[i])){
                                validJavaIndentifiers.put(variables[i], validJavaIndentifiers.get(variables[i]) + 1);
                            } else {
                                validJavaIndentifiers.put(variables[i], 1);
                            }
                        }
                    }
                    boolean unwantedFinal = true;
                    for (int y = 0; y < variables[i].length(); y++){
                        char character = variables[i].charAt(y);
                        if (!(Character.isLetter(character) && Character.isUpperCase(character)) && character != '_'){
                            unwantedFinal = false;
                            break;
                        }
                    }
                    char c = variables[i].charAt(0);
                    if ((Character.isLetter(c) && Character.isLowerCase(c)) || c == '$' || c == '_') {
                        if (!unwantedFinal){
                            if (validJavaIndentifiers.containsKey(variables[i])){
                                validJavaIndentifiers.put(variables[i], validJavaIndentifiers.get(variables[i]) + 1);
                            } else {
                                validJavaIndentifiers.put(variables[i], 1);
                            }
                        }
                    }
                }
            }

        }

        return validJavaIndentifiers;
    }

    @Override
    public Map<String, List<Integer>> getInvalidJavaIdentifiers() {
        String wordsString = "";
        try{
            AVLTree<String> words = createKeywordTree();
            wordsString = postorderTraversal(words.getRoot());
        } catch (FileNotFoundException ex){
            System.out.println("File not found");
        }
        String[] reservedWordsList = wordsString.split(" ");

        Map<String, List<Integer>> invalidJavaIndentifiers = new HashMap<>();

        Scanner input = null;
        try {
            input = new Scanner(javaFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        int count = 0;

        while(input.hasNext()) {
            String s = input.nextLine();
            count++;

            while (s.contains("\"")) {
                int begin = s.indexOf("\"");
                int end = s.indexOf("\"", begin + 1);
                s = s.substring(0, begin) + s.substring(end + 1);
            }

            while (s.contains("'")) {
                int begin = s.indexOf("'");
                int end = s.indexOf("'", begin + 1);
                s = s.substring(0, begin) + s.substring(end + 1);
            }

            if (s.contains("/*")) {
                //s = input.nextLine();
                do {
                    s = input.nextLine();
                    count++;
                } while (!s.contains("*/"));
            }

            if (s.contains("//")) {
                int j = s.indexOf("//");
                if (j == 0) {
                    s = "";
                } else {
                    s = s.substring(0, j);
                }
            }

            /*for (int i = 0; i < s.length(); i++) {
                if (!Character.isLetter(s.charAt(i)) && !Character.isDigit(s.charAt(i))) {
                    String temp = s.substring(i, i + 1);
                    if (!temp.equals("_") && !temp.equals("$")) {
                        s = s.replace(temp, " ");
                    }
                }
            }*/
            s = s.replace(".", " ");
            s = s.replace("(", " ");
            s = s.replace(")", " ");
            s = s.replace("[", " ");
            s = s.replace("]", " ");
            s = s.replace("{", " ");
            s = s.replace("}", " ");
            s = s.replace(";", " ");

            s = s.replace("*", " ");
            s = s.replace("-", " ");
            s = s.replace("+", " ");
            s = s.replace("%", " ");
            s = s.replace("/", " ");
            s = s.replace("=", " ");
            s = s.replace("<", " ");
            s = s.replace(">", " ");
            s = s.replace(",", " ");
            s = s.trim();

            String[] variables = s.split("\\s+");

            for (int i = 0; i < variables.length; i++) {

                boolean isReserved = false;
                for (String str : reservedWordsList) {
                    if (variables[i].equals(str)) {
                        isReserved = true;
                        break;
                    }
                }

                if (!isReserved && !variables[i].isEmpty()) {
                    boolean isNum = true;
                    try {
                        int n = Integer.parseInt(variables[i]);
                    } catch (NumberFormatException ex) {
                        isNum = false;
                    }
                    if (!isNum && variables[i].length() > 1){
                        char c = variables[i].charAt(0);
                        if (!Character.isLetter(c)){
                            if (c != '&' && c != '_'){
                                if (invalidJavaIndentifiers.containsKey(variables[i])) {
                                    List<Integer> lines = invalidJavaIndentifiers.get(variables[i]);
                                    if (!lines.contains(count)){
                                        lines.add(count);
                                        invalidJavaIndentifiers.replace(variables[i], lines);
                                    }
                                } else {
                                    List<Integer> lines = new ArrayList<>();
                                    lines.add(count);
                                    invalidJavaIndentifiers.put(variables[i], lines);
                                }
                            }
                        } else {
                            if (Character.isUpperCase(c)){
                                String[] validVariables = {"Scanner", "System", "String", "Integer", "Character", "File", "Double", "Int", "Char"};
                                boolean isInList = false;
                                for (int k = 0; k < validVariables.length; k++){
                                    if (validVariables[k].equals(variables[i])){
                                        isInList = true;
                                    }
                                }
                                if (!isInList){
                                    boolean validFinal = true;
                                    for (int y = 0; y < variables[i].length(); y++){
                                        char character = variables[i].charAt(y);
                                        if (!Character.isLetter(character) || !Character.isUpperCase(character)){
                                            if (character != '_'){
                                                validFinal = false;
                                            } else {
                                                if (y == 0 || y == variables[i].length() - 1){
                                                    validFinal = false;
                                                }
                                            }
                                        }
                                    }
                                    if (validFinal){
                                        if (i < 2 || !variables[i - 2].equals("final")){
                                            if (invalidJavaIndentifiers.containsKey(variables[i])) {
                                                List<Integer> lines = invalidJavaIndentifiers.get(variables[i]);
                                                if (!lines.contains(count)){
                                                    lines.add(count);
                                                    invalidJavaIndentifiers.replace(variables[i], lines);
                                                }
                                            } else {
                                                List<Integer> lines = new ArrayList<>();
                                                lines.add(count);
                                                invalidJavaIndentifiers.put(variables[i], lines);
                                            }
                                        }
                                    } else {
                                        if (i >= 1){
                                            String variable = variables[i - 1];
                                            if (!variable.equals("class") && !variable.equals("implements") && !variable.equals("extends")){
                                                if (invalidJavaIndentifiers.containsKey(variables[i])) {
                                                    List<Integer> lines = invalidJavaIndentifiers.get(variables[i]);
                                                    if (!lines.contains(count)){
                                                        lines.add(count);
                                                        invalidJavaIndentifiers.replace(variables[i], lines);
                                                    }
                                                } else {
                                                    List<Integer> lines = new ArrayList<>();
                                                    lines.add(count);
                                                    invalidJavaIndentifiers.put(variables[i], lines);
                                                }
                                            }
                                        } else {
                                            if (invalidJavaIndentifiers.containsKey(variables[i])) {
                                                List<Integer> lines = invalidJavaIndentifiers.get(variables[i]);
                                                if (!lines.contains(count)){
                                                    lines.add(count);
                                                    invalidJavaIndentifiers.replace(variables[i], lines);
                                                }
                                            } else {
                                                List<Integer> lines = new ArrayList<>();
                                                lines.add(count);
                                                invalidJavaIndentifiers.put(variables[i], lines);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return invalidJavaIndentifiers;
    }
}
