package axiol.parser.tree.expressions;

import axiol.parser.tree.Expression;
import axiol.parser.tree.NodeType;
import axiol.parser.tree.Statement;

import java.util.ArrayList;
import java.util.List;

public class ArrayInitExpression extends Expression {
    private final List<Expression> values;
    private final Expression initSize;

    public ArrayInitExpression(List<Expression> values, Expression initSize) {
        this.values = values;
        this.initSize = initSize;
    }

    @Override
    public List<Statement> childStatements() {
        List<Statement> statements = new ArrayList<>(values);
        statements.add(initSize);
        return statements;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY_EXPR;
    }


    public Expression getInitSize() {
        return initSize;
    }

    public List<Expression> getValues() {
        return values;
    }
}
