package engine.item.device;

/**
 * Very small light object.
 */
public class Light {
	private boolean state = false;

	public boolean isOn() {
		return state;
	}

	public void turnOn() {
		state = true;
	}

	public void turnOff() {
		state = false;
	}
}
