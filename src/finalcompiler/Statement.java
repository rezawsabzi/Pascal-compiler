package finalcompiler;

public class Statement {
    String start;
    String[] statement;

    Statement(String[] statement){
        this.statement = statement;
    }
    Statement(String[] statement,String start){
        this.statement = statement;
        this.start = start;
    }

}
