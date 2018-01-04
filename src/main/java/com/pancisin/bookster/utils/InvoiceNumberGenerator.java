package com.pancisin.bookster.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.pancisin.bookster.model.UserSubscription;

public class InvoiceNumberGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		UserSubscription us = (UserSubscription) object;

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%04d", us.getUser().getId()));
		sb.append("-");
		sb.append(formatter.format(new Date()));
		sb.append("-");

		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();
			String query = "select count(id) as id from users_subscriptions WHERE id LIKE '" + sb.toString() + "%'";

			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				sb.append(rs.getInt(1));
			} else
				sb.append(String.valueOf(0));
		} catch (SQLException ex) {

		}

		return sb.toString();
	}

}
