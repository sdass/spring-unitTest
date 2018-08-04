package com.subra;

import java.io.Serializable;

public class MyNode implements Serializable {

	private static final long serialVersionUID = 4242910813619963078L;
	String name;
	String value;
	
	public MyNode(){}
	
	public MyNode(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() { return name; 	}
	public void setName(String name) { 	this.name = name; 	}
	public String getValue() { 	return value; 	}
	public void setValue(String value) { 	this.value = value; 	}
	
	@Override
	public String toString() {
		return "MyNode [name=" + name + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyNode other = (MyNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
	
	
}
