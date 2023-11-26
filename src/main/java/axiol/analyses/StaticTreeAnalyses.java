package axiol.analyses;

import axiol.parser.RootNodeProcessor;
import axiol.parser.tree.RootNode;
import axiol.parser.tree.Statement;
import axiol.parser.tree.expressions.*;
import axiol.parser.tree.expressions.control.MatchExpression;
import axiol.parser.tree.expressions.extra.ReferenceExpression;
import axiol.parser.tree.expressions.sub.BooleanExpression;
import axiol.parser.tree.expressions.sub.NumberExpression;
import axiol.parser.tree.expressions.sub.StringExpression;
import axiol.parser.tree.statements.BodyStatement;
import axiol.parser.tree.statements.LinkedNoticeStatement;
import axiol.parser.tree.statements.VariableStatement;
import axiol.parser.tree.statements.control.*;
import axiol.parser.tree.statements.oop.*;
import axiol.parser.tree.statements.special.NativeStatement;
import axiol.parser.util.SourceFile;

import java.util.Arrays;

public class StaticTreeAnalyses implements RootNodeProcessor {

    private final Class<? extends Statement>[] definitionStatements = new Class[]{
            ClassTypeStatement.class, FunctionStatement.class,
            StructTypeStatement.class, ConstructStatement.class,
            VariableStatement.class, LinkedNoticeStatement.class,
    };
    private final Class<? extends Statement>[] declarationStatements = new Class[]{
            UDTDeclareStatement.class
    };
    private final Class<? extends Statement>[] controlFlowStatements = new Class[]{
            BreakStatement.class, ContinueStatement.class, DoWhileStatement.class,
            ForStatement.class, IfStatement.class, LoopStatement.class,
            ReturnStatement.class, SwitchStatement.class, UnreachableStatement.class,
            WhileStatement.class, YieldStatement.class
    };
    private final Class<? extends Statement>[] expressions = new Class[]{
            MatchExpression.class, ReferenceExpression.class, BooleanExpression.class,
            NumberExpression.class, StringExpression.class, ArrayInitExpression.class,
            BinaryExpression.class, UnaryExpression.class, LiteralExpression.class,
            FunctionStatement.class
    };
    private final Class<? extends Statement>[] specialCases = new Class[]{
            NativeStatement.class,  BodyStatement.class
    };

    @Override
    public RootNode process(RootNode rootNode) {
        SourceFile sourceFile = rootNode.getSourceFile();
        for (Statement statement : rootNode.getStatements()) {
            this.processStatement(sourceFile, statement);
        }
        return rootNode;
    }

    public void processStatement(SourceFile sourceFile, Statement statement) {
        Class<? extends Statement> statementClass = statement.getClass();

        if (Arrays.stream(definitionStatements).anyMatch(aClass -> aClass == statementClass)) {
            this.processDefinitionStatements(sourceFile, statement);
        }
        if (Arrays.stream(declarationStatements).anyMatch(aClass -> aClass == statementClass)) {
            this.processDeclarationStatements(sourceFile, statement);
        }
        if (Arrays.stream(controlFlowStatements).anyMatch(aClass -> aClass == statementClass)) {
            this.processControlFlowStatements(sourceFile, statement);
        }
        if (Arrays.stream(expressions).anyMatch(aClass -> aClass == statementClass)) {
            this.processExpressions(sourceFile, statement);
        }
        if (Arrays.stream(specialCases).anyMatch(aClass -> aClass == statementClass)) {
            this.processSpecialCasesStatements(sourceFile, statement);
        }

        ValidationException.UNMATCHED_STATEMENT
                .throwException(sourceFile, statement.position(),
                        statement.type(), "General Processing");
    }

    public void processDefinitionStatements(SourceFile sourceFile, Statement statement) {
        switch (statement.type()) {
            case CLASS_TYPE_STATEMENT ->  this.analyseClassType(sourceFile, (ClassTypeStatement) statement);
            case FUNCTION_STATEMENT ->    this.analyseFunction(sourceFile, (FunctionStatement) statement);
            case STRUCT_TYPE_STATEMENT -> this.analyseStructureType(sourceFile, (StructTypeStatement) statement);
            case CONSTRUCT_STATEMENT ->   this.analyseConstruct(sourceFile, (ConstructStatement) statement);
            case VAR_STATEMENT ->         this.analyseVariable(sourceFile, (VariableStatement) statement);
            case LINKED_STATEMENT ->      this.analyseLinkedNotice(sourceFile, (LinkedNoticeStatement) statement);

            default -> ValidationException.UNMATCHED_STATEMENT
                    .throwException(sourceFile, statement.position(),
                            statement.type(), "Definition");
        }
    }

    public void processDeclarationStatements(SourceFile sourceFile, Statement statement) {
        if (statement instanceof UDTDeclareStatement udtDeclareStatement) {
            this.analyseUDTDeclaration(sourceFile, udtDeclareStatement);
        }
    }

    public void processControlFlowStatements(SourceFile sourceFile, Statement statement) {
        switch (statement.type()) {
            case BREAK_STATEMENT ->       this.analyseBreak(sourceFile, (BreakStatement) statement);
            case FOR_STATEMENT ->         this.analyseFor(sourceFile, (ForStatement) statement);
            case CONTINUE_STATEMENT ->    this.analyseContinue(sourceFile, (ContinueStatement) statement);
            case DO_WHILE_STATEMENT ->    this.analyseDoWhile(sourceFile, (DoWhileStatement) statement);
            case IF_STATEMENT ->          this.analyseIf(sourceFile, (IfStatement) statement);
            case LOOP_STATEMENT ->        this.analyseLoop(sourceFile, (LoopStatement) statement);
            case RETURN_STATEMENT ->      this.analyseReturn(sourceFile, (ReturnStatement) statement);
            case SWITCH_STATEMENT ->      this.analyseSwitch(sourceFile, (SwitchStatement) statement);
            case UNREACHABLE_STATEMENT -> this.analyseUnreachable(sourceFile, (UnreachableStatement) statement);
            case WHILE_STATEMENT ->       this.analyseWhile(sourceFile, (WhileStatement) statement);
            case YIELD_STATEMENT ->       this.analyseYield(sourceFile, (YieldStatement) statement);

            default -> ValidationException.UNMATCHED_STATEMENT
                    .throwException(sourceFile, statement.position(),
                            statement.type(), "ControlFlow");
        }
    }

    public void processSpecialCasesStatements(SourceFile sourceFile, Statement statement) {
        switch (statement.type()) {
            case BODY_STATEMENT ->        this.analyseBody(sourceFile, (BodyStatement) statement);
            case NATIVE_STATEMENT ->      this.analyseNative(sourceFile, (NativeStatement) statement);

            default -> ValidationException.UNMATCHED_STATEMENT
                    .throwException(sourceFile, statement.position(),
                            statement.type(), "SpecialCase");
        }
    }

    public void processExpressions(SourceFile sourceFile, Statement statement) {
        switch (statement.type()) {
            case MATCH_EXPR ->            this.analyseMatch(sourceFile, (MatchExpression) statement);
            case REFERENCE_EXPR ->        this.analyseReference(sourceFile, (ReferenceExpression) statement);
            case BOOLEAN_EXPR ->          this.analyseBoolean(sourceFile, (BooleanExpression) statement);
            case NUMBER_EXPR ->           this.analyseNumber(sourceFile, (NumberExpression) statement);
            case STRING_EXPR ->           this.analyseString(sourceFile, (StringExpression) statement);
            case ARRAY_EXPR ->            this.analyseArray(sourceFile, (ArrayInitExpression) statement);
            case BINARY_EXPR ->           this.analyseBinary(sourceFile, (BinaryExpression) statement);
            case UNARY_EXPR ->            this.analyseUnary(sourceFile, (UnaryExpression) statement);
            case LITERAL_EXPR ->          this.analyseLiteral(sourceFile, (LiteralExpression) statement);
            case CALL_EXPR ->             this.analyseCall(sourceFile, (CallExpression) statement);

            default -> ValidationException.UNMATCHED_STATEMENT
                    .throwException(sourceFile, statement.position(),
                            statement.type(), "Expressions");
        }
    }

    private void analyseClassType(SourceFile sourceFile, ClassTypeStatement statement) {
    }

    private void analyseFunction(SourceFile sourceFile, FunctionStatement statement) {
    }

    private void analyseStructureType(SourceFile sourceFile, StructTypeStatement statement) {
    }

    private void analyseConstruct(SourceFile sourceFile, ConstructStatement statement) {
    }

    private void analyseVariable(SourceFile sourceFile, VariableStatement statement) {
    }

    private void analyseLinkedNotice(SourceFile sourceFile, LinkedNoticeStatement statement) {
    }

    private void analyseUDTDeclaration(SourceFile sourceFile, UDTDeclareStatement udtDeclareStatement) {
    }

    private void analyseBreak(SourceFile sourceFile, BreakStatement statement) {
    }

    private void analyseContinue(SourceFile sourceFile, ContinueStatement statement) {
    }

    private void analyseFor(SourceFile sourceFile, ForStatement statement) {
    }

    private void analyseDoWhile(SourceFile sourceFile, DoWhileStatement statement) {
    }

    private void analyseIf(SourceFile sourceFile, IfStatement statement) {
    }

    private void analyseLoop(SourceFile sourceFile, LoopStatement statement) {
    }

    private void analyseReturn(SourceFile sourceFile, ReturnStatement statement) {
    }

    private void analyseSwitch(SourceFile sourceFile, SwitchStatement statement) {
    }

    private void analyseUnreachable(SourceFile sourceFile, UnreachableStatement statement) {
    }

    private void analyseWhile(SourceFile sourceFile, WhileStatement statement) {
    }

    private void analyseYield(SourceFile sourceFile, YieldStatement statement) {
    }

    private void analyseBody(SourceFile sourceFile, BodyStatement statement) {
    }

    private void analyseNative(SourceFile sourceFile, NativeStatement statement) {
    }

    private void analyseMatch(SourceFile sourceFile, MatchExpression statement) {
    }

    private void analyseReference(SourceFile sourceFile, ReferenceExpression statement) {
    }

    private void analyseBoolean(SourceFile sourceFile, BooleanExpression statement) {
    }

    private void analyseNumber(SourceFile sourceFile, NumberExpression statement) {
    }

    private void analyseString(SourceFile sourceFile, StringExpression statement) {
    }

    private void analyseArray(SourceFile sourceFile, ArrayInitExpression statement) {
    }

    private void analyseBinary(SourceFile sourceFile, BinaryExpression statement) {
    }

    private void analyseUnary(SourceFile sourceFile, UnaryExpression statement) {
    }

    private void analyseLiteral(SourceFile sourceFile, LiteralExpression statement) {
    }

    private void analyseCall(SourceFile sourceFile, CallExpression statement) {
    }


}
