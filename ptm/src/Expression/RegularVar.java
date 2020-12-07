package Expression;

public class RegularVar extends Var {

	public RegularVar(String name, double val) {
		super(name, val);

	}

	@Override
	public double calculate() {
		return super.getValue();
	}

	@Override
	public void set(double val) {
	}

	@Override
	public void setValue(Double value) {
	}

}
