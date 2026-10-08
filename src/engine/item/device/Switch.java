package engine.item.device;

import engine.map.Block;

/**
 * Small switch linked to one light.
 */
public class Switch {

	private Light light;
	private Block block;

	public Switch(Light light, Block block) {
		this.light = light;
		this.block = block;
	}

	public boolean isOn() {
		return light.isOn();
	}

	public void turnOn() {
		light.turnOn();
	}

	public void turnOff() {
		light.turnOff();
	}

	public Block getPosition() {
		return block;
	}

	public Light getLight() {
		return light;
	}
}
