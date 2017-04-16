package com.pancisin.employger.rest.controllers.objects;

import java.util.Calendar;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.DutyClause;

public class DutyInstance {

	private Duty duty;
	private Calendar date;
	private DutyClause clause;
	
	
	public DutyInstance(Duty duty, Calendar date, DutyClause clause) {
		this.duty = duty;
		this.date = date;
		this.clause = clause;
	}
	
	public boolean hasClause() {
		return clause != null;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public DutyClause getClause() {
		return clause;
	}

	public void setClause(DutyClause clause) {
		this.clause = clause;
	}
}
