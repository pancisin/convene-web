package com.pancisin.employger.rest.controllers.objects;

import java.util.Date;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.DutyClause;

public class DutyInstance {

	private Duty duty;
	private Date date;
	private DutyClause clause;
	
	public boolean hasClause() {
		return clause != null;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public DutyClause getClause() {
		return clause;
	}

	public void setClause(DutyClause clause) {
		this.clause = clause;
	}
}
