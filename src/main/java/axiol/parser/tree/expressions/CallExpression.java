package axiol.parser.tree.expressions;

import axiol.parser.tree.Expression;
import axiol.parser.tree.NodeType;
import axiol.parser.tree.Statement;
import axiol.parser.util.error.TokenPosition;
import axiol.types.Reference;
import axiol.types.SimpleType;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class CallExpression extends Expression {

    private final Reference reference;
    private final String path;
    private final List<Expression> parameters;

    public CallExpression(Reference reference, String path, List<Expression> parameters, TokenPosition position) {
        this.reference = reference;
        this.path = path;
        this.parameters = parameters;
        this.position = position;
    }

    private final TokenPosition position;

    @Override
    public TokenPosition position() {
        return position;
    }

    public List<Expression> getParameters() {
        return parameters;
    }

    public Reference getReference() {
        return reference;
    }

    @Override
    public List<Statement> childStatements() {
        return new ArrayList<>(parameters);
    }

    @Override
    public NodeType type() {
        return NodeType.CALL_EXPR;
    }

    public String getPath() {
        return path;
    }

    @Override
    public SimpleType valuedType() {
        return reference.getValueType();
    }
}
