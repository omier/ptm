package expressions;

import java.util.function.Predicate;

public class PredicateExp extends BinaryExpression {
	String cond;
	Predicate<Expression> pred;

	public PredicateExp(Expression left, Expression right, String cond) {
		super(left, right);
		this.cond = cond;
	}

	@Override
	public double calculate() {
		switch (cond) {
		case "<":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() < right.calculate();
				}
			};
			
			break;
		case ">":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() > right.calculate();
				}
			};
			
			break;
		case "<=":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() <= right.calculate();
				}
			};
			
			break;
		case ">=":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() >= right.calculate();
				}
			};
			
			break;
		case "==":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() == right.calculate();
				}
			};
			
			break;
		case "!=":
			pred = new Predicate<Expression>() {
				@Override
				public boolean test(Expression expression) {
					return expression.calculate() != right.calculate();
				}
			};
			
			break;
		}
		
		return pred.test(left) ? 1 : 0;
	}
}
