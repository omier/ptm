package Expression;

public abstract class Var implements Expression {
	protected String name;
	protected Double value;
	public Var(String name,double value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public abstract void set(double val);

	public abstract void setValue(Double value);

	public double calculate() {
		return this.value;
	}

	public double getValue() {
		return this.value;
	}
}