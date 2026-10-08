package process.imprevu;

/**
 * Small object that describes one imprevu.
 */
public class Imprevu {

	private int code;
	private String name;
	private String description;

	/**
	 * Builds one imprevu entry.
	 * @param code the config code
	 * @param name the displayed name
	 * @param description the short effect description
	 */
	public Imprevu(int code, String name, String description) {
		this.code = code;
		this.name = name;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return name + " - " + description;
	}
}
