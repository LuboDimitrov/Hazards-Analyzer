package mainPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static mainPackage.HazardType.*;

public class Hazard {

    //Instance variables
    private Instruction source;
    private Instruction dest;
    private HazardType hazardtype;

    //Color variables
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_RESET  = "\u001B[0m";

    //Main constructor
    public Hazard(Instruction source, Instruction dest, HazardType hazard) {
        this.source = source;
        this.dest = dest;
        this.hazardtype = hazard;
    }

    //Test constructor (ignore)
    public Hazard(HazardType hazard){
        this.hazardtype = hazard;
    }

    //Method we use to remove the parentheses of the memory address in the LD and ST instructions
    public static ArrayList<Instruction> getRidOfParentheses(ArrayList<Instruction> instructions){
        //result is an additional list so we dont alter the original list of instructions
        ArrayList<Instruction> result = new ArrayList<>(instructions);
        String aux;

        for (Instruction instruction : result) {
            if (instruction.getOpcode().equals("ST")) {
                aux = instruction.getOperands().get(0).replaceAll("[()]", "");
                aux = aux.strip();
                instruction.getOperands().set(0, aux);
            }
            if (instruction.getOpcode().equals("LD")) {
                aux = instruction.getOperands().get(1).replaceAll("[()]", "");
                aux = aux.strip();
                instruction.getOperands().set(1, aux);
            }

        }
        return result;
    }

    //Method that tells us the
    public static ArrayList<Hazard> findHazards(ArrayList<Instruction> instructions){
        ArrayList<Instruction> aux = getRidOfParentheses(instructions);
        ArrayList<Hazard> result = new ArrayList<>();
        String firstOperand;

        for(int i = 0; i < aux.size(); i++){
            Instruction i1 = aux.get(i);
            if(i1.getOpcode().equals("JMP")) continue;
            firstOperand = i1.getOperands().get(0);
            for(int j = i + 1; j < aux.size(); j++){
                Instruction i2 = aux.get(j);
                    if(i2.getOperands().subList(1, i2.getOperands().size()).contains(firstOperand)){
                        Hazard hazard = new Hazard(i1, i2, RAW);
                        result.add(hazard);
                    }
                    if(i1.getOperands().subList(1, i1.getOperands().size()).contains(i2.getOperands().get(0))){
                        Hazard hazard = new Hazard(i1, i2, WAR);
                        result.add(hazard);
                    }
                    if(firstOperand.equals(i2.getOperands().get(0))){
                        Hazard hazard = new Hazard(i1, i2, WAW);
                        result.add(hazard);
                    }
            }
        }
        return result;
    }

    public static void printHazardsGrouped(ArrayList<Instruction> instructions){
        ArrayList<Hazard> hazards = findHazards(instructions);

        List<Hazard> RAWlist = hazards.stream()
                .filter(h -> h.getHazardtype().equals(RAW))
                .collect(Collectors.toList());

        List<Hazard> WARlist = hazards.stream()
                .filter(h -> h.getHazardtype().equals(WAR))
                .collect(Collectors.toList());

        List<Hazard> WAWlist = hazards.stream()
                .filter(h -> h.getHazardtype().equals(WAW))
                .collect(Collectors.toList());

        RAWlist.forEach(System.out::println);
        System.out.println("------------------------");
        WARlist.forEach(System.out::println);
        System.out.println("------------------------");
        WAWlist.forEach(System.out::println);

    }


    public String toString() {
        String color = "";
        switch(hazardtype){
            case RAW:
                color = ANSI_RED;break;
            case WAR:
                color = ANSI_YELLOW;break;
            case WAW:
                color = ANSI_GREEN;break;
        }
        return
        color + hazardtype + ANSI_RESET + " hazard from I" + source.getPosition() + " to I" + dest.getPosition()+
        " caused by " +
        (getHazardtype().equals(WAR) ? dest.getOperands().get(0) : source.getOperands().get(0));
    }

    public Instruction getSource() {
        return source;
    }

    public Instruction getDest() {
        return dest;
    }

    public HazardType getHazardtype() {
        return hazardtype;
    }
}
