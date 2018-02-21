package mx.com.masnegocio.rpn.engine;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import com.caronte.encoding.Base64;
import com.caronte.jpath.JPATH;
import com.caronte.json.JSON;
import com.caronte.json.JSONObject;
import com.caronte.json.JSONValue;
import com.caronte.json.JSONValueType;
import mx.com.masnegocio.rpn.engine.JSONdecoder.OperationMember;
import mx.com.masnegocio.rpn.engine.exceptions.OperandStackException;
import mx.com.masnegocio.rpn.engine.exceptions.OperatorException;

public class RPN {
	private ArrayList<Object> dependencies;
	private Stack<OperationMember> stack;
	
	@SuppressWarnings("unchecked")
	public RPN(String stringB64) throws Exception {
		String jsonObject = new String(Base64.decode(stringB64));
		JSONObject dependenciesJSON = JSON.parse(jsonObject);
		JSONValue array = JPATH.find(dependenciesJSON, "/dependency");
		this.dependencies = null;
		
		if(array.getType() == JSONValueType.ARRAY)
			this.dependencies = (ArrayList<Object>)array.getValue();
		
		this.stack = new Stack<>();
	}
	
	@SuppressWarnings("unchecked")
	public RPN(JSONObject dependencies) throws Exception {
		JSONObject dependenciesJSON = dependencies;
		JSONValue array = JPATH.find(dependenciesJSON, "/dependency");
		this.dependencies = null;

		if(array.getType() == JSONValueType.ARRAY)
			this.dependencies = (ArrayList<Object>)array.getValue();

		this.stack = new Stack<>();
	}
	
	public Stack<OperationMember> Resolve() throws OperandStackException, OperatorException{
		if(this.dependencies == null)
			return null;
		OperationMember op1;
		OperationMember op2;
		OperationMember op3;
		
		for (Object object : this.dependencies) {
			OperationMember member = new OperationMember((JSONObject)object);
			if(member.getType().equals("Operand"))
				this.stack.push(member);
			else {
				String value = (String)member.getValue(); 
				switch (value) {
					case "IN":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("IN");
						}
						
						stack.push(new OperationMember(op2.IN(op1), "Operand"));
						
						break;
					case "OR":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("OR");
						}
						
						stack.push(new OperationMember(op2.OR(op1), "Operand"));
						
						break;
					case "AND":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("AND");
						}
						
						stack.push(new OperationMember(op2.AND(op1), "Operand"));
						
						break;
					case "XOR":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("XOR");
						}
						
						stack.push(new OperationMember(op2.XOR(op1), "Operand"));
						
						break;
					case "EQUALS":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("EQUALS");
						}
						 
						stack.push(new OperationMember(op2.EQUALS(op1), "Operand"));
						
						break;
					case "DIFFERENT":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("DIFFERENT");
						}
						
						stack.push(new OperationMember(op2.DIFERENT(op1), "Operand"));
						
						break;
					case "LTH":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("LTH");
						}
						
						stack.push(new OperationMember(op2.LessThan(op1), "Operand"));
						
						break;
					case "GTH":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("GTH");
						}
						
						stack.push(new OperationMember(op2.GreaterThan(op1), "Operand"));
						
						break;
					case "LTHE":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("LTHE");
						}
						
						stack.push(new OperationMember(op2.LessThanOrEquals(op1), "Operand"));
						
						break;
					case "GTHE":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("GTHE");
						}
						
						stack.push(new OperationMember(op2.GreaterThanOrEquals(op1), "Operand"));
						
						break;
					case "NOT":
						try {
							op1 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("NOT");
						}
						
						stack.push(new OperationMember(op1.NOT(), "Operand"));
						
						break;
					case "CONTAINS":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("CONTAINS");
						}
						
						stack.push(new OperationMember(op2.CONTAINS(op1), "Operand"));
						
						break;
					case "SUMA":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("SUMA");
						}
						
						op2.SUMA(op1, stack);
						
						break;
					case "RESTA":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("RESTA");
						}
						
						op2.RESTA(op1, stack);
						break;
					case "MULTIPLICA":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("MULTIPLICA");
						}
						
						op2.MULTIPLICA(op1, stack);
						break;
					case "DIVIDE":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("DIVIDE");
						}
						
						op2.DIVIDE(op1, stack);
						break;
					case "POW":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("POW");
						}
						
						op2.POW(op1, stack);
						break;
					case "IF":
						try {
							op1 = stack.pop();
							op2 = stack.pop();
							op3 = stack.pop();
						}catch(EmptyStackException ese) {
							throw OperandStackException.NoOperandsException("IF");
						}
						
						stack.push(new OperationMember(op3.IF(op2, op1), "Operand"));
						
						break;
				}
			}
		}
		
		if(this.stack.size() != 1)
			throw OperandStackException.NoSolutionException(stack);
		
		return this.stack;
	}
}
