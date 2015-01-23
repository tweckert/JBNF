package de.nexum.util;

import java.util.UUID;

/**
 * @author <a href="mailto:thomas.weckert@nexum.de">Thomas Weckert</a>
 */
public class Position {
	
	private String uuid;
	private int position;
	
	public Position() {
		this(0);
	}
	
	public Position(Position position) {
		this(position.getPosition());
	}
	
	public Position(int position) {
		
		super();
		this.position = position;
		this.uuid = UUID.randomUUID().toString();
	}

	public int getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public void increment(int increment) {
		this.position += increment;
	}
	
	public void decrement(int decrement) {
		this.position -= decrement;
	}
	
	@Override
	public String toString() {
		return Integer.toString(position);
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Position)) {
			return false;
		}

		Position otherPosition = (Position) obj;

		return this.position == otherPosition.position;
	}

}
