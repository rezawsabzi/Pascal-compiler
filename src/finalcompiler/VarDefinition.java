package finalcompiler;

import java.util.ArrayList;
import java.util.Arrays;

public class VarDefinition extends Statement{
    String[] allDataTypes = {"integer", "boolean", "real", "string", "char"};
    VarDefinition(String[] statement){
        super(statement);
        this.start = "var";
        this.nextStep();
    }

    public void nextStep(){
        ArrayList<String> varNames = new ArrayList<>();
        if (this.statement[2].equals(",")) {
            for (int i = 0; i < this.statement.length; i++) {
                if (this.statement[i+1].equals(",") || this.statement[i+1].equals(":")) {
                    varNames.add(this.statement[i]);
                }
                if (this.statement[i + 1].equals(":")) break;
            }
            int indxOdType = 0;
            for (int i = 0; i < this.statement.length; i++) {
                if (this.statement[i].equals(":")) indxOdType = ++i;
            }
            for (String v : varNames) {
                if(indxOdType != 0 && this.statement[0].equals(start) && this.statement[indxOdType - 1].equals(":")
                        && Arrays.stream(allDataTypes).anyMatch(this.statement[indxOdType]::equals)
                        && this.statement[indxOdType + 1].equals(";")){

                        if (isStringOnlyAlphabet(v.charAt(0))){
                            Compiler.dataTypes.add(new DataTypes(this.statement[indxOdType],v, ""));
                        }

                }

            }
        }

        if(this.statement[0].equals(start) && isStringOnlyAlphabet(this.statement[1].charAt(0))
        && this.statement[2].equals(":") && Arrays.stream(allDataTypes).anyMatch(this.statement[3]::equals)){

            if(this.statement[4].equals("=")){
                switch (this.statement[3]){
                    case "integer": // var x:integer = 4;
                        if(isStringOnlyNumeric(this.statement[5]) && this.statement[6].equals(";")){
                            Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], this.statement[5]));
                        }
                        break;
                    case "real":  // var x:real = 4.5;
                        if(isStringOnlyDouble(this.statement[5]) && this.statement[6].equals(";")){
                            Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], this.statement[5]));
                        }
                        break;
                    case "boolean":  // var x:boolean = true;
                        if(this.statement[5].equals("true") || this.statement[5].equals("false")  && this.statement[6].equals(";")){
                            Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], this.statement[5]));
                        }
                        break;
                    case "char": // var x:char = "c";
                        if(this.statement[6].length() == 1 && this.statement[5].equals("\"")  && this.statement[7].equals("\"")  && this.statement[8].equals(";")){
                            Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], this.statement[6]));
                        }
                        break;
                    case "string":  // var x:string = "c";
                        if(this.statement[5].equals("\"") && this.statement[7].equals("\"") && this.statement[8].equals(";")){
                            Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], this.statement[7]));
                        }
                        break;
                }

            }else if(this.statement[4].equals(";")){
                 Compiler.dataTypes.add(new DataTypes(this.statement[3],this.statement[1], "")) ;
            }else{
                throw new RuntimeException("myCompiler error:  Variable declarations is not correct. ");
            }
        }

    }

    public  boolean isStringOnlyNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }

    public  boolean isStringOnlyDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public  boolean isStringOnlyAlphabet(char str) {
        return Character.isLetter(str);
    }
}
