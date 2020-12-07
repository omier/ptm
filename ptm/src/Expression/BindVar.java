package Expression;

public class BindVar extends Var {
	String bindPath;

	public BindVar(String name, double val, String path) {
		super(name, val);
		this.bindPath = path;
	}

	public void set(double val, String path) {
		super.value = val;
		this.bindPath = path;
	}

	@Override
	public void set(double value) {
		super.value = value;
	}

	@Override
	public void setValue(Double value) {
		super.value = value;
	}

	public String getPath() {
		return this.bindPath;
	}

	@Override
	public double calculate() {
		return super.value;
	}
}
