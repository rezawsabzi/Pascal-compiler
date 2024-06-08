package finalcompiler;

import java.util.ArrayList;
import java.util.Arrays;

import static finalcompiler.Compiler.findIndex;

public class IfStatement extends Statement{


    IfStatement(String[] statement){
        super(statement);
        this.start = "if";
        this.nextStep();
    }

    public void nextStep(){
        ArrayList<String> segment = new ArrayList<>(Arrays.asList(this.statement));
//        System.out.println(segment);
        String condition = "";
        String action = "";
        ArrayList<String> actionlist = new ArrayList<>();
        int startInxOfCondition = findIndex(this.statement, "(");
//        int endInxOfCondition = findIndex(this.statement, ")");
        int inxOfThen = findIndex(this.statement, "then");
        for (int i = startInxOfCondition ; i < inxOfThen; i++) {
            condition += this.statement[i] + " ";
        }
        String res = String.valueOf(new HandleExp(condition).result);
        if (Boolean.parseBoolean(res)) {
            for (int i = inxOfThen + 1; i < this.statement.length; i++) {
                if (i != this.statement.length - 1)
                    action += this.statement[i] + " ";
                else
                    action += this.statement[i];

                actionlist.add(this.statement[i]);
            }

            if(actionlist.get(0).equals("begin")){
                if (actionlist.get(actionlist.size() - 1).equals(";")){
                    actionlist.remove(actionlist.size() - 1);
                    actionlist.set(actionlist.size() - 1, "end.");
                    Compiler.runBlock(actionlist);
                }

//                System.out.println(actionlist);
            }else if(action.split(";").length == 1){
//                System.out.println(actionlist);
                Compiler.runSegment(actionlist);
            }

        }
    }
}
