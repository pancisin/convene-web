package com.pancisin.bookster.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.github.slugify.Slugify;
import com.pancisin.bookster.models.Page;

public class UniqueSlugGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Page page = (Page) object;
		Slugify s = new Slugify();
		String slug = s.slugify(page.getName());
		
		Connection connection = session.connection();

		try {
			Statement statement = connection.createStatement();
			String query = "select count(id) as slug_count from pages WHERE slug LIKE '" + slug + "%'";

			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				slug = String.join("-", slug, String.valueOf(rs.getInt(1)));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return slug;
	}

}
