package finalcompiler;

import java.util.ArrayList;

import static finalcompiler.Compiler.*;

public class ForLoop extends Statement{

    ForLoop(String[] statement){
        super(statement);
        this.start = "for";
        this.nextStep();
    }

    public void nextStep(){
        ArrayList<String> assignment = new ArrayList<>();
        ArrayList<String> actionlist = new ArrayList<>();
        int inxOfDo = findIndex(this.statement, "do");
        int inxOdDown = -1;

        int inxOfTo = findIndex(this.statement, "to");
        if(inxOfTo < 0){
            inxOdDown = findIndex(this.statement, "downto");
            if (inxOdDown > 0){
                for (int i = 1 ; i < inxOdDown; i++) {
                    assignment.add(this.statement[i]);
                }
            }
        }
        if (inxOfTo > 0){
            for (int i = 1 ; i < inxOfTo; i++) {
                assignment.add(this.statement[i]);
            }
        }

        assignment.add(";");
        Compiler.runCheckForAssignment(assignment);
        String x = this.statement[1];
        int xIndex = -1;
        for (DataTypes d: Compiler.dataTypes){
            if (x.equals(d.getName())){
                xIndex = Compiler.dataTypes.indexOf(d);
            }
        }

        int counter = Integer.parseInt(getValue(this.statement[1]));
        String str = "";
        if (inxOfTo > 0){
            for (int i = inxOfTo + 1; i < inxOfDo; i++) {
                str += this.statement[i] + " ";
            }
            for (int i = inxOfDo + 1; i < this.statement.length; i++) {
                actionlist.add(this.statement[i]);
            }
        }else if (inxOdDown > 0){
            for (int i = inxOdDown + 1; i < inxOfDo; i++) {
                str += this.statement[i] + " ";
            }
            for (int i = inxOfDo + 1; i < this.statement.length; i++) {
                actionlist.add(this.statement[i]);
            }
        }
        String res = String.valueOf(new HandleExp(str).result);
//        System.out.println(new HandleExp(str).result);
        int to = Integer.parseInt(String.valueOf(new HandleExp(str).result) );

        if(actionlist.get(actionlist.size() - 1).equals(";")){
            actionlist.remove(actionlist.size()-1);
            actionlist.set(actionlist.size() - 1 , "end.");
        }
        if (inxOfTo > 0){
            for (int i = counter; i <= to; i++) {
//                System.out.println(actionlist);
                if (i == counter){
                    Compiler.runBlock(actionlist);
                    dataTypes.get(xIndex).setValue(String.valueOf(i + 1));



                }else{
                    actionlist.add(0, "begin");
                    actionlist.add(actionlist.size() , "end.");
                    Compiler.runBlock(actionlist);
                    dataTypes.get(xIndex).setValue(String.valueOf(i + 1));

                }
            }
        } else if (inxOdDown > 0) {
            for (int i = counter; i >= to; i--) {
                if (i == counter){
                    Compiler.runBlock(actionlist);
                    dataTypes.get(xIndex).setValue(String.valueOf(i - 1));

                }else{
                    actionlist.add(0, "begin");
                    actionlist.add(actionlist.size() , "end.");
                    Compiler.runBlock(actionlist);
                    dataTypes.get(xIndex).setValue(String.valueOf(i - 1));
                }
            }
        }

    }
}
