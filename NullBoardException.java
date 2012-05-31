
public class NullBoardException extends Exception {

	/**
	 * IDK
	 */
	private static final long serialVersionUID = 1L;
	
	private String error;

	public NullBoardException(String error) {
		super(error);
		this.error = error;
	}
	
	public String getError() {
		return error;
	}
}
