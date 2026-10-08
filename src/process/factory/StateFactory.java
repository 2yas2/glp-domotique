package process.factory;

import engine.state.MasterState;

/**
 * Factory interface for the initial master state.
 */
public interface StateFactory {

	MasterState create();
}
