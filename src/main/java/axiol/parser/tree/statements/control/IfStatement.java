package axiol.parser.tree.statements.control;

import axiol.parser.tree.Expression;
import axiol.parser.tree.Statement;
import axiol.parser.tree.statements.BodyStatement;

public class IfStatement extends Statement {

    private final Expression condition;
    private final BodyStatement body;
    private final Statement elseStatement; // body or more if

    public IfStatement(Expression condition, BodyStatement body, Statement elseStatement) {
        this.condition = condition;
        this.body = body;
        this.elseStatement = elseStatement;
    }

    public Expression getCondition() {
        return condition;
    }

    public BodyStatement getBody() {
        return body;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }
}
