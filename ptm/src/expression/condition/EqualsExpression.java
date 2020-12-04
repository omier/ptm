package expression.condition;
import expression.BinaryExpression;
import expression.Expression;

public class EqualsExpression extends BinaryExpression {

	public EqualsExpression(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate() {
		return left.calculate() == right.calculate() ? 1 : 0;
	}
	
}
