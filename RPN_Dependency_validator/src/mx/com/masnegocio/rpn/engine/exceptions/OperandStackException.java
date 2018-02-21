package mx.com.masnegocio.rpn.engine.exceptions;

import java.util.Stack;

import mx.com.masnegocio.rpn.engine.JSONdecoder.OperationMember;

public class OperandStackException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperandStackException() {
		super("Ocurrió un error inesperado en la pila de operandos");
	}
	
	public OperandStackException(String message) {
		super(message);
	} 
	
	public static OperandStackException NoSolutionException(Stack<OperationMember> stack) {
		return new OperandStackException("La pila tiene " + stack.size() + " elementos, se esperaba 1");
	}
	
	public static OperandStackException NoOperandsException(String opName) {
		return new OperandStackException("No hay elementos suficientes en la pila para realizar la operación " + opName);
	}
}
