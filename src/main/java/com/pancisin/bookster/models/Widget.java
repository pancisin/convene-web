package com.pancisin.bookster.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.pancisin.bookster.models.enums.WidgetType;

@Entity
@Table(name = "widgets")
public class Widget {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(updatable = false, nullable = false)
	private UUID id;

	@Column
	private int x;
	
	@Column
	private int y;
	
	@Column
	private int w;
	
	@Column
	private int h;
	
	@Column
	private int i;

	@Enumerated(EnumType.STRING)
	private WidgetType type;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public UUID getId() {
		return id;
	}

	public WidgetType getType() {
		return type;
	}

	public void setType(WidgetType type) {
		this.type = type;
	}
}
