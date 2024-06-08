package finalcompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compiler {
    static String[] words;
    static String word;
    static ArrayList<DataTypes> dataTypes = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        String[] programLines = readFile("program/pas/10.pas");
        ArrayList<String> program = new ArrayList<>();
        for (int i = 0; i < programLines.length; i++) {
            String[] line = extractWords(programLines[i]);
            for (int j = 0; j < line.length; j++) {
                program.add(line[j]);
            }

        }
        compile(program);

    }
    public static String[] readFile(String filePath) throws FileNotFoundException {
        List<String> linesList = new ArrayList<>();
        File programFile = new File(filePath);
        Scanner reader = new Scanner(programFile);
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            linesList.add(line);
        }
        reader.close();
        return linesList.toArray(new String[0]);
    }

    public static String[] extractWords(String text) {
        words = new String[]{};
        word = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c != ' ') {
                switch (c) {
                    case '?':
                        checkChar(text, i, c);
                        break;

                    case '=':
                    case '+':
                    case '-':
                    case '>':
                    case '<':
                    case '*':
                    case '/':
                    case '%':
                        checkWordIsEmpty();
                        word = Character.toString(c);
                        if (i + 1 < text.length()) {
                            char next = text.charAt(i + 1);
                            if (next != ' ') {
                                if (next == '=') {
                                    word = c + "=";
                                    i++;
                                }else if (c == '<' && next == '>'){
                                    word = "<>";
                                    i++;
                                }
                                words = appendToArray(words, word);
                                word = "";
                            }
                        }
                        break;
                    case ')':
                    case '(':
                    case ':':
                    case ';':
                    case ',':
                        checkChar(text, i, c);
                        break;

                    case '"':
                        checkWordIsEmpty();
                        words = appendToArray(words, "\"");
                        word = "";
                        int index = i + 1;
                        while (text.charAt(index) != '"') {
                            word += Character.toString(text.charAt(index));
                            index++;
                        }
                        if (text.charAt(index) == '"') {
                            words = appendToArray(words, word);
                            words = appendToArray(words, "\"");
                            word = "";
                        }
                        i = index;
                        break;
                    default:
                        word += c;
                }
            } else {
                if (!word.equals("")) {
                    words = appendToArray(words, word);
                    word = "";
                }
            }
        }

        if (word.equals("?") || !word.equals("")) {
            words = appendToArray(words, word);
        }

        return words;
    }
    public static String[] appendToArray(String[] arr, String element) {
        if(arr == null){
            arr =  new String[1];
            arr[0] = element;
            return arr;
        }
        String[] newArr = new String[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[arr.length] = element;
        return newArr;
    }

    public static void checkWordIsEmpty() {
        if (!word.equals("")) {
            words = appendToArray(words, word);
        }
    }

    public static void checkChar(String text, int i, char c) {
        checkWordIsEmpty();
        word = Character.toString(c);
        if (i + 1 < text.length()) {
            char next = text.charAt(i + 1);
            if (next != ' ') {
                words = appendToArray(words, word);
                word = "";
            }
        }
    }

    public static void compile(ArrayList<String> program) {

        ArrayList<String> variablesString = new ArrayList<>();
        ArrayList<String> mainBlock = new ArrayList<>();
        for (int i = 0; i < program.size(); i++) {
            if (!program.get(i).equals("begin")) {
                variablesString.add(program.get(i));
            } else {
                for (int j = i; j < program.size(); j++) {
                    mainBlock.add(program.get(i));
                    i++;
                }
                break;
            }
        }
        runBeforeBegin(variablesString);
        runBlock(mainBlock);
    }

    public static void runBeforeBegin(ArrayList<String> variablesString){
        ArrayList<ArrayList<String>> varSegments = splitByDelimiter(variablesString, ";");
        for (ArrayList<String> s : varSegments){
            if (varSegments.indexOf(s) != 0){
                if (!s.get(0).equals("var")){
                    s.add(0, "var");
                }
            }
            new VarDefinition(s.toArray(new String[s.size()]));
        }

//        System.out.println(dataTypes);
    }

    public static void runBlock(ArrayList<String> block){
        if(block.get(0).equals("begin") && block.get(block.size() - 1).equals("end.")){
            block.remove(0);
            block.remove(block.size()-1);
           runInside(block);

        }else {
            throw new RuntimeException("myCompiler error: Does not match begin and end ");
        }
    }

    public static void runInside(ArrayList<String> code){
        ArrayList<ArrayList<String>> segments = splitByDelimiter(code, ";");

        for (ArrayList<String> seg : segments){
            runSegment(seg);
        }

    }

    public static ArrayList<ArrayList<String>> splitByDelimiter(ArrayList<String> list, String delimiter) {
        ArrayList<ArrayList<String>> segments = new ArrayList<>();
        ArrayList<String> currentSegment = new ArrayList<>();
        int nestedIfLevel = 0;

        for (String element : list) {
            currentSegment.add(element);
            if (element.equals("begin")) {
                nestedIfLevel++;
            } else if (element.equals("end")) {
                nestedIfLevel--;
            } else if (element.equals(delimiter) && nestedIfLevel == 0) {
                segments.add(new ArrayList<>(currentSegment)); // Add a copy of currentSegment
                currentSegment.clear(); // Clear current segment for the next set of elements
            }
        }

        // Add the last segment if it wasn't followed by a delimiter
        if (!currentSegment.isEmpty()) {
            segments.add(currentSegment);
        }
        

        return segments;
    }



    public static void runSegment(ArrayList<String> segment){
        switch (segment.get(0)){
            case "writeln":
            case "write":
                new Write(segment.toArray(new String[segment.size()]), segment.get(0));
                break;
            case "if":
                new IfStatement(segment.toArray(new String[segment.size()]));
                break;
            case "for":
                new ForLoop(segment.toArray(new String[segment.size()]));
                break;
            case "while":
                new WhileLoop(segment.toArray(new String[segment.size()]));
                break;
            default:
                for (int i = 0; i < dataTypes.size(); i++) {
                    if (segment.get(0).equals(dataTypes.get(i).getName())){
                        runCheckForAssignment(segment);
                    }
                }
        }

    }



    public static void runCheckForAssignment(ArrayList<String> seg) {
        for (int i = 0; i < dataTypes.size(); i++) {
            if (seg.get(0).equals(dataTypes.get(i).getName()) &&
                    (seg.get(1).equals(":")) &&  (seg.get(2).equals("=")) &&  (seg.get(seg.size() - 1)).equals(";")){
                ArrayList<String> exp = new ArrayList<>();
                for (int j = 3; j < seg.size() - 1; j++) {
                    exp.add(seg.get(j));
                }
                String res = String.valueOf (new HandleExp(exp).result);
                if (getTypeOfDataType(res).equals(dataTypes.get(i).getType())){
                    dataTypes.get(i).setValue(res);
                }

            }
        }
//        System.out.println(dataTypes);
    }

    public static String getTypeOfDataType(String data) {
        // Check if the data is a defined variable
        for (DataTypes dataType : dataTypes) {
            if (dataType.getName().equals(data)) {
                return dataType.getType();
            }
        }

        // If not a variable, determine if it is a literal
        if (data.equals("true") || data.equals("false")) {
            return "boolean";
        } else if (isStringOnlyNumeric(data)) {
            return "integer";
        } else if (isStringOnlyDouble(data)) {
            return "real";
        } else if (data.startsWith("\"") && data.endsWith("\"")) {
            return "string";
        }else if (data.length() == 1){
            return "char";
        }
        else {
            throw new RuntimeException("myCompiler error: Undefined variable or invalid literal: " + data);
        }
    }

    public static String getValue(String data) {
        // Check if the data is a defined variable
        for (DataTypes dataType : dataTypes) {
            if (dataType.getName().equals(data)) {
                return dataType.getValue();
            }
        }

        // If not a variable, treat it as a literal
        if (data.equals("true") || data.equals("false")) {
            return data;
        } else if (isStringOnlyNumeric(data) || isStringOnlyDouble(data)) {
            return data;
        } else if (data.startsWith("\"") && data.endsWith("\"")) {
            return data.substring(1, data.length() - 1); // Remove surrounding quotes
        } else {
            throw new RuntimeException("myCompiler error: Undefined variable or invalid literal: " + data);
        }
    }

    public static int findIndex(String[] line, String el) {
        for (int i = 0; i < line.length; i++) {
            if (line[i].equals(el)) return i;
        }
        return -1;
    }



    public static boolean isStringOnlyNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    public static boolean isStringOnlyDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
