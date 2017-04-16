package com.pancisin.employger.repository.implementations;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.rest.controllers.objects.DutyInstance;

public class DutyRepositoryImpl implements DutyCustomRepository {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public List<DutyInstance> getInstances(Date dateFrom, Date dateTo) {
		 Criteria crit = em.unwrap(Session.class).createCriteria(Duty.class);

		 
		// TODO Auto-generated method stub
		return null;
	}

}
