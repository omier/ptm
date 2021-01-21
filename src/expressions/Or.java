package expressions;

public class Or extends BinaryExpression {
	public Or(Expression left, Expression right) {
		super(left, right);
	}

	@Override
	public double calculate() {
		return (left.calculate() + right.calculate()) > 0 ? 1 : 0;
	}
}
