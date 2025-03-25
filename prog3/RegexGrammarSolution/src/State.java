/**
 * Class representing a state in an automaton. Has an identifier which is solely
 * used for printing and debugging purposes.
 */
public class State {

	/**
	 * Counter for unique state ids.
	 */
	static private int counter = 0;

	/**
	 * State id.
	 */
	private int id;

	/**
	 * Constructor.
	 */
	State() {
		id = counter++;
	}

	/**
	 * Getter.
	 * 
	 * @return Id
	 */
	int getId() {
		return id;
	}

}
