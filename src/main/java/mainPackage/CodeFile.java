package mainPackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CodeFile {

    private final Path path;
    private List<String> auxList; // raw data

    ArrayList<Instruction> ListOfInstructions = new ArrayList<>();
    ArrayList<String> labels; //Jump destinations

    //Constructor
    public CodeFile(String filename) {
        path = Paths.get("./"+filename); //files are stored in root directory
    }

    public void readFile() throws IOException {
        auxList = Files.readAllLines(path);
        //Creates an arraylist that contains all the labels
        labels = auxList.
                 stream().
                 filter(line -> Character.isLowerCase(line.charAt(0))).
                 collect(Collectors.toCollection(ArrayList::new));
    }

    //Creates instructions from the file
    public ArrayList<Instruction> parseInstructions() throws IOException, InvalidOpcodeException, InvalidOperandException, LabelDoesntExistException {
        readFile();
        String opcode;
        String auxoperands; //String that will contain the operands separated by ,
        String currentLine;
        Instruction instr;
        ArrayList<String> operands;

        for (int i = 0; i < auxList.size(); i++) {
            currentLine = auxList.get(i);
            if(!currentLine.isBlank()) {
                if(Character.isLowerCase(currentLine.charAt(0))) continue; //labels aint instructions
                currentLine = currentLine.trim(); //get rid of whitespaces and blanks
                opcode = currentLine.substring(0, currentLine.indexOf(" "));
                auxoperands = currentLine.substring(currentLine.indexOf(" ")+1);
                auxoperands = auxoperands.strip();
                if(opcode.equals("JMP")){
                    //In the case of JUMP instructions there is no need to split the values
                    //since there is only one operand
                    operands = new ArrayList<>(List.of(auxoperands));
                } else {
                    operands = new ArrayList<>(Arrays.asList(auxoperands.split(",")));
                }
                
                instr = new Instruction(opcode, operands, i+1);
                //check whether the instruction's opcode and its operands are valid or not
                    if(!instr.isValidOpcode()) throw new InvalidOpcodeException
                            ("Invalid opcode at line "+ (i+1) + ". Check Allowed Opcodes -> " + Instruction.getOpcodesList());
                    if(!instr.areOperandsValid()) throw new InvalidOperandException
                            ("Check operands "+operands+ " at line "+ (i+1));
                    if(instr.getOpcode().equals("JMP") && !labels.contains(instr.getOperands().get(0))) throw new LabelDoesntExistException
                            ("Destination of JMP at line " + (i+1) + " does not exist. Make sure label names match");

                ListOfInstructions.add(instr);
                System.out.println(instr);
            }
        }
        return ListOfInstructions;
    }


}
