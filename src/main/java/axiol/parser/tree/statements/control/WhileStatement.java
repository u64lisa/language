package axiol.parser.tree.statements.control;

import axiol.parser.tree.Expression;
import axiol.parser.tree.Statement;
import axiol.parser.tree.statements.BodyStatement;

public class WhileStatement extends Statement {
    private final Expression condition;
    private final BodyStatement bodyStatement;

    public WhileStatement(Expression condition, BodyStatement bodyStatement) {
        this.condition = condition;
        this.bodyStatement = bodyStatement;
    }

    public Expression getCondition() {
        return condition;
    }

    public BodyStatement getBodyStatement() {
        return bodyStatement;
    }
}
