package axiol.parser.tree.expressions.sub;

import axiol.parser.tree.Expression;
import axiol.parser.util.error.Position;

public class NumberExpression extends Expression {

    private final Position position;
    private final double numberValue;

    public NumberExpression(Position position, double numberValue) {
        this.position = position;
        this.numberValue = numberValue;
    }

    public Position getPosition() {
        return position;
    }

    public double getNumberValue() {
        return numberValue;
    }
}
