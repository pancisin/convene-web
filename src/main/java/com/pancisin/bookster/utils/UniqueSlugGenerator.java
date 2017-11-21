package com.pancisin.bookster.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

import com.github.slugify.Slugify;
import com.pancisin.bookster.models.Page;

public class UniqueSlugGenerator implements ValueGenerator<String> {

	private String slug;

	@Override
	public String generateValue(Session session, Object owner) {
		Page page = (Page) owner;
		Slugify s = new Slugify();
		slug = s.slugify(page.getName());
		
		session.doWork(connection -> {
			try {
				Statement statement = connection.createStatement();
				String query = "select count(id) as slug_count from pages WHERE slug LIKE '" + slug + "%'";

				ResultSet rs = statement.executeQuery(query);

				if (rs.next()) {
					int number = rs.getInt(1);
					
					if (number > 0) {
						slug = String.join("-", slug, String.valueOf(rs.getInt(1)));
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		
		return slug;
	}

}
