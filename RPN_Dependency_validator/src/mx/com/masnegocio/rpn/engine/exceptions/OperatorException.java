package mx.com.masnegocio.rpn.engine.exceptions;

public class OperatorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperatorException() {
		super("El operador produjo un error inesperado");
	}
	
	public OperatorException(String message) {
		super(message);
	}
	
	public static OperatorException OutOfParamsException(int expectedParams, int inputParams) {
		return new OperatorException("El operador tiene " + inputParams + " parámetros, se esperaban " + expectedParams);
	}
	
	public static OperatorException OutOfParamsException() {
		return new OperatorException("El operador tiene menos parámetros de los esperados");
	}
	
	public static OperatorException ParamNotExpectedException(Object o1, Object o2) {
		return new OperatorException("Se ingresó un " + o1.getClass() + ", se esperaba un " + o2.getClass());
	}
	
	public static OperatorException NotNumericParamException(Object o) {
		return new OperatorException("El parámetro " + o + " no puede ser procesado por una operación aritmética");
	}
}
