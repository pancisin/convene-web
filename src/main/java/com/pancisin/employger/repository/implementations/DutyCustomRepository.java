package com.pancisin.employger.repository.implementations;

import java.util.Date;
import java.util.List;

import com.pancisin.employger.rest.controllers.objects.DutyInstance;

public interface DutyCustomRepository {
	public List<DutyInstance> getInstances(Date dateFrom, Date dateTo);
}
