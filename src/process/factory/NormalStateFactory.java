package process.factory;

import engine.state.MasterState;

/**
 * Default state factory.
 */
public class NormalStateFactory implements StateFactory {

	@Override
	public MasterState create() {
		return new MasterState();
	}
}
