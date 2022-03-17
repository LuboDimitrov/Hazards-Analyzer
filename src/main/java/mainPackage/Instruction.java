package mainPackage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Instruction {

    //Instance variables
    private final String opcode;
    private final ArrayList<String> operands;
    private final Integer position;

    //For simplicity, we declare the allowed instructions and registers as ArrayLists
    private static final ArrayList<String> opcodesList = new ArrayList<>(Arrays.asList
            ("LD","ST","ADD","MUL","DIV","SUB","JMP"));
    private static final ArrayList<String> registersList = new ArrayList<>(Arrays.asList
            ("r0","r1","r2","r3","r4","r5","r6","r7","r8","r9"));

    //Constructor
    public Instruction(String opcode, ArrayList<String> operands, Integer position) {
        this.opcode = opcode;
        this.operands = operands;
        this.position = position;
    }

    //Checks if the opcode exists.
    public boolean isValidOpcode() {
        return opcodesList.contains(this.opcode);
    }

    //Checks if the operands syntax is correct
    public boolean areOperandsValid() {
        for (int i = 0; i < this.operands.size(); i++) {
            //JMP will be handled in CodeFile class
            if(opcode.equals("JMP")) continue;
            if(opcode.equals("ST")) {
                if (!operands.get(0).startsWith("(") ||
                    !operands.get(0).endsWith(")")) return false;
            }
            else if(opcode.equals("LD")) {
                if (!operands.get(operands.size()-1).startsWith("(") ||
                    !operands.get(operands.size()-1).endsWith(")")) return false;
            }
            //check arithmetic operations
            else{
                if (!registersList.contains(operands.get(i))) return false;
                if(operands.size() != 3) return false;
            }

        }
        return true;

    }

    //Getters, setters and all that stuff

    public static ArrayList<String> getOpcodesList() {
        return opcodesList;
    }

    public String getOpcode() {
        return opcode;
    }

    public ArrayList<String> getOperands() {
        return operands;
    }

    public Integer getPosition() {
        return position;
    }


    @Override
    public String toString() {
        String formattedOperands = operands.stream()
                .map(String::toString).collect(Collectors.joining(","));
        return "I" + position + ":" + " " + opcode + " " + formattedOperands;

    }
}
