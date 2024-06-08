package finalcompiler;

import java.util.ArrayList;



    public class HandleStringExp {
        private ArrayList<String> inputList;

        public HandleStringExp(ArrayList<String> inputList) {
            this.inputList = inputList;
            System.out.println( concatenateStrings());
        }

        public String concatenateStrings() {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < inputList.size(); i++) {
                if(!inputList.get(i).equals("\"") && !inputList.get(i).equals("+")){
                    if (inputList.get(i - 1).equals("\"") && inputList.get(i + 1).equals("\"")){
                        result.append(inputList.get(i));
                    }
                }
            }
            return result.toString();
        }

    }