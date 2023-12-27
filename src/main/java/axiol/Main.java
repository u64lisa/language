package axiol;

import axiol.analyses.StaticAnalysis;
import axiol.instruction.InstructionGenerator;
import axiol.instruction.InstructionSet;
import axiol.parser.LanguageParser;
import axiol.parser.tree.RootNode;
import axiol.parser.util.scope.Scope;
import axiol.parser.util.scope.ScopeAble;
import axiol.parser.util.scope.ScopeElement;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        LanguageParser languageParser = new LanguageParser();

        //languageParser.parseSource("/test/expressions.ax", readFile("/test/expressions.ax"));
        RootNode rootNode = languageParser.parseSource("/test/syntax.ax", readFile("/test/syntax.ax"));

        StaticAnalysis staticAnalysis = new StaticAnalysis();
        rootNode = staticAnalysis.process(rootNode);

        InstructionGenerator instructionGenerator = new InstructionGenerator();
        InstructionSet instructionSet = instructionGenerator.emit(rootNode);
    }

    public static String readFile(String file) {
        Scanner scanner = new Scanner(Objects.requireNonNull(Main.class.getResourceAsStream(file)));
        StringBuilder fileContents = new StringBuilder();

        while (scanner.hasNextLine()) {
            fileContents.append(scanner.nextLine()).append("\n");
        }

        scanner.close();
        return fileContents.toString();
    }

}