package expressions;

public class And extends BinaryExpression {

	public And(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate() {
		return (left.calculate() + right.calculate()) == 2 ? 1 : 0;
	}
}
