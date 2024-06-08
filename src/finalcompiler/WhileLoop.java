package finalcompiler;

import java.util.ArrayList;

import static finalcompiler.Compiler.findIndex;

public class WhileLoop extends Statement {
    WhileLoop(String[] statement) {
        super(statement);
        this.start = "while";
        this.nextStep();
    }

    public void nextStep() {
        ArrayList<String> actionlist = new ArrayList<>();
        ArrayList<String> condition = new ArrayList<>();
        int inxOfDo = findIndex(this.statement, "do");
        int inxOfStart = findIndex(this.statement, "(");
        int inxOfEnd = findIndex(this.statement, ")");

        String str = "";
        String conStr = "";

        for (int i = inxOfDo + 1; i < this.statement.length; i++) {
            str += this.statement[i];
            actionlist.add(this.statement[i]);
        }

        for (int i = inxOfStart + 1; i < inxOfEnd; i++) {
            conStr += this.statement[i];
            condition.add(this.statement[i]);
        }

        if (actionlist.get(actionlist.size() - 1).equals(";")) {
            actionlist.remove(actionlist.size() - 1);
            actionlist.set(actionlist.size() - 1, "end.");
        }

        boolean flag = Boolean.parseBoolean(String.valueOf(new HandleExp(conStr).result));
        while (flag) {
            flag = Boolean.parseBoolean(String.valueOf(new HandleExp(conStr).result));
            Compiler.runBlock(actionlist);
            actionlist.add(0, "begin");
            actionlist.add(actionlist.size(), "end.");
        }


    }
}
