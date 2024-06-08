package finalcompiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Write extends Statement{
    Write(String[] segment, String start){
        super(segment, start);
        this.nextStep();
    }
    Write(String segment, String start) {
        this(Compiler.extractWords(segment), start);
    }


    public void nextStep(){
        ArrayList<String> segment = new ArrayList<>(Arrays.asList(this.statement)) ;

        int firstIndex = segment.indexOf("(");
        int lastIndex = segment.lastIndexOf(")");
        StringBuilder printExp = new StringBuilder();

        for (int i = firstIndex + 1; i < lastIndex; i++) {
            if (!segment.get(i).equals("\"")){
                printExp.append(segment.get(i) + " ");
            }else if (segment.get(i).equals("\"")){
                for (int j = i; j < lastIndex; j++) {
                    if(j + 1 < lastIndex){
                        if (!segment.get(j + 1).equals("\"")){
                            printExp.append(segment.get(j));
                        }else if (segment.get(j + 1).equals("\"")){
                            printExp.append(segment.get(j));
                            printExp.append(segment.get(j + 1) + " ");
                            i = j + 2;
                            break;
                        }
                    }

                }


            }

        }
        String result = "";
        if(printExp.toString().contains("\"")){


            ArrayList<ArrayList<String>> segs = parseString(printExp.toString());

            ArrayList<String> segs2 = new ArrayList<>();
            for (int i = 0; i < segs.size(); i++) {
                segs2.add(String.join(" ", segs.get(i)));
            }

            for (int i = 0; i < segs2.size(); i++) {
                if (segs2.get(i).charAt(0) == '\"'){
                    result += segs2.get(i);
                }else {

                    result += String.valueOf(new HandleExp(segs2.get(i)).result) ;
                }
            }
            result = result.replaceAll("\"", "");
        }else {
            result = String.valueOf(new HandleExp(printExp.toString()).result) ;
        }


        if (start.equals("write")){
            System.out.print(result);
        }else if (start.equals("writeln")){
            System.out.println(result);
        }



    }

    public static ArrayList<ArrayList<String>> parseString(String input) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        StringBuilder expressionBuilder = new StringBuilder();
        boolean insideString = false;

        Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^\"+]+)|([+])");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Found a quoted string
                if (expressionBuilder.length() > 0) {
                    result.add(new ArrayList<>(Arrays.asList(expressionBuilder.toString().trim())));
                    expressionBuilder.setLength(0);
                }
                result.add(new ArrayList<>(Arrays.asList("\"" + matcher.group(1) + "\"")));
            } else if (matcher.group(2) != null) {
                // Found an expression
                expressionBuilder.append(matcher.group(2).trim());
            } else if (matcher.group(3) != null) {
                // Found a + operator
                if (expressionBuilder.length() > 0) {
                    result.add(new ArrayList<>(Arrays.asList(expressionBuilder.toString().trim())));
                    expressionBuilder.setLength(0);
                }
            }
        }

        if (expressionBuilder.length() > 0) {
            result.add(new ArrayList<>(Arrays.asList(expressionBuilder.toString().trim())));
        }

        return result;
    }


}
