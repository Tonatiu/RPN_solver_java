package mx.com.masnegocio.rpn.engine.JSONdecoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;
import com.caronte.jpath.JPATH;
import com.caronte.json.JSONObject;
import com.caronte.json.JSONValue;
import com.caronte.json.JSONValueType;
import mx.com.masnegocio.rpn.engine.exceptions.OperatorException;

public class OperationMember {
	protected Object value;
	protected String type;
	
	public OperationMember(JSONObject obj){
		JSONValue value = JPATH.find(obj, "/value");
		this.value = value.getValue();
		JSONValue type = JPATH.find(obj, "/type");
		if(type.getType() == JSONValueType.STRING)
			this.type = (String)type.getValue();
	}
	
	public OperationMember(Object value, String type) {
		this.value = value;
		this.type = type;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean LessThan(OperationMember compare) throws OperatorException {
		if(!this.areNumbers(compare))
			return false;
		
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = compare.getValue();
		this.paramsCheck(params);
		
		return this.compare(compare) < 0;
	}
	
	public boolean GreaterThan(OperationMember compare) throws OperatorException {
		if(!this.areNumbers(compare))
			return false;
		
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = compare.getValue();
		this.paramsCheck(params);
		
		return this.compare(compare) > 0;
	}
	
	public boolean LessThanOrEquals(OperationMember compare) throws OperatorException {
		if(!this.areNumbers(compare))
			return false;
		
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = compare.getValue();
		this.paramsCheck(params);
		
		return this.compare(compare) <= 0;
	}
	
	public boolean GreaterThanOrEquals(OperationMember compare) throws OperatorException {
		if(!this.areNumbers(compare))
			return false;
		
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = compare.getValue();
		this.paramsCheck(params);
		
		return this.compare(compare) >= 0;
	}
	
	public boolean AND(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		if((this.value instanceof Boolean) && (op2.getValue() instanceof Boolean))
			return (Boolean)this.value && (Boolean)op2.getValue();
		
		return false;
	}
	
	public boolean OR(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		if((this.value instanceof Boolean) && (op2.getValue() instanceof Boolean))
			return (Boolean)this.value || (Boolean)op2.getValue();
		return false;
	}
	
	public boolean XOR(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		if((this.value instanceof Boolean) && (op2.getValue() instanceof Boolean))
			return (Boolean)this.value ^ (Boolean)op2.getValue();
		return false;
	}
	
	public boolean EQUALS(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		return this.value.equals(op2.getValue());
	}
	
	public boolean DIFERENT(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		return !this.value.equals(op2.getValue());
	}
	
	public boolean NOT() throws OperatorException {
		Object [] params = new Object [1];
		params[0] = this.value;
		this.paramsCheck(params);
		
		if((this.value instanceof Boolean))
			return !(Boolean)this.value;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean CONTAINS(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		if(!(this.value instanceof ArrayList) || !(op2.getValue() instanceof ArrayList)) 
			return false;
		
		ArrayList<Object> array1 = (ArrayList<Object>)this.value;
		ArrayList<Object> array2 = (ArrayList<Object>)op2.getValue();
		ArrayList<Object> elements;
		ArrayList<Object> container;
		
		if(array1.size() < array2.size()) {
			elements = array1;
			container = array2;
		}
		else {
			elements = array2;
			container = array1;
		}
		
		for (Object element : elements) {
			if(container.contains(element))
				return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean IN(OperationMember op2) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		ArrayList<Object> array;
		Object element;
		
		if(this.value instanceof ArrayList) {
			array = (ArrayList<Object>)this.value;
			element = op2.getValue();
		}else if(op2.getValue() instanceof ArrayList) {
			array = (ArrayList<Object>)op2.getValue();
			element = this.value;
		}else {
			return false;
		}

		return  array.contains(element);
	}
	
	public void SUMA(OperationMember op2, Stack<OperationMember> stack) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		this.areNumbers(this, op2);
		
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(op2.getValue().toString());
		
		stack.push(new OperationMember(a.add(b).toString(), "Operand"));
	}
	
	public void RESTA(OperationMember op2, Stack<OperationMember> stack) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		this.areNumbers(this, op2);
		
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(op2.getValue().toString());
		
		stack.push(new OperationMember(a.subtract(b).toString(), "Operand"));
	}
	
	public void MULTIPLICA(OperationMember op2, Stack<OperationMember> stack) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		this.areNumbers(this, op2);
		
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(op2.getValue().toString());
		
		stack.push(new OperationMember(a.multiply(b), "Operand"));
	}
	
	public void DIVIDE(OperationMember op2, Stack<OperationMember> stack) throws OperatorException{
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		this.areNumbers(this, op2);
		
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(op2.getValue().toString());
		
		stack.push(new OperationMember(a.divide(b).toString(), "Operand"));
	}
	
	public void POW(OperationMember op2, Stack<OperationMember> stack) throws OperatorException {
		Object [] params = new Object [2];
		params[0] = this.value;
		params[1] = op2.getValue();
		this.paramsCheck(params);
		
		this.areNumbers(this, op2);
		
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(op2.getValue().toString());
		
		stack.push(new OperationMember(a.pow(b.intValue()).toString(), "Operand"));
	}
	
	public Object IF(OperationMember op2, OperationMember op3) throws OperatorException {
		Object [] params = new Object [3];
		params[0] = this.value;
		params[1] = op2.getValue();
		params[2] = op3.getValue();
		this.paramsCheck(params);
		
		if(this.value instanceof Boolean) {
			if((boolean)this.value) {
				return op2.getValue();
			}
			else {
				return op3.getValue();
			}
		}
		
		return null;
	}
	
	private boolean areNumbers(OperationMember compare) {
		String number;
		
		number = this.value.toString();
		if(!number.matches("-?[0-9]+(\\.[0-9]+)?"))
			return false;
		number = compare.getValue().toString();
		if(!number.matches("-?[0-9]+(\\.[0-9]+)?"))
			return false;
		
		return true;
	}
	
	private void areNumbers(OperationMember op1, OperationMember op2) throws OperatorException {
		String number;
		number = op1.getValue().toString();
		
		if(!number.matches("-?[0-9]+(\\.[0-9]+)?"))
			throw OperatorException.NotNumericParamException(number);
		number = op2.getValue().toString();
		if(!number.matches("-?[0-9]+(\\.[0-9]+)?"))
			throw OperatorException.NotNumericParamException(number);
	}
	
	private int compare(OperationMember compare) {
		BigDecimal a = new BigDecimal(this.value.toString());
		BigDecimal b = new BigDecimal(compare.getValue().toString());
		
		return a.compareTo(b); 
	}
	
	private void paramsCheck(Object[] params) throws OperatorException {
		int counter = 0;
		for (Object param : params) {
			if(param != null)
				counter++;
		}
		
		if(params.length != counter)
			throw OperatorException.OutOfParamsException(params.length, counter);
	}
	
	public String toString() {
		String object = "";
		object += "type=" + this.type + ", value=" + this.value.toString();
		return object;
	}
}
