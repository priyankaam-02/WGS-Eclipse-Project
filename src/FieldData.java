
public class FieldData {
	private String name;
	private int maxLength;
	private int minLength;
	private int order;
	private boolean optional;
	@Override
	public String toString() {
		return "FieldData [name=" + name + ", maxLength=" + maxLength + ", minLength=" + minLength + ", order=" + order
				+ ", optional=" + optional + "]";
	}
	public boolean isOptional() {
		return optional;
	}
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public int getMinLength() {
		return minLength;
	}
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	
	
}
