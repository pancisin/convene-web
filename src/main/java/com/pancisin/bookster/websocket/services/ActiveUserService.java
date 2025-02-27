package com.pancisin.bookster.websocket.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.repository.AdministratorRepository;
import com.pancisin.bookster.repository.UserRepository;

@Component
public class ActiveUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdministratorRepository paRepository;

	private LoadingCache<String, UserStats> statsByUser = CacheBuilder.newBuilder()
			.build(new CacheLoader<String, UserStats>() {

				@Override
				public UserStats load(String key) throws Exception {
					return getUserStats(key);
				}
			});

	public void mark(String username) {
		statsByUser.getUnchecked(username).mark();
	}

	public UserStats getUserStats(String email) {
		User user = userRepository.findByEmail(email);
		Set<String> contacts = paRepository.getContacts(user.getId()).stream().map(u -> u.getEmail()).collect(Collectors.toSet());
		return new UserStats(email, contacts);
	}

	public List<UserStats> getActiveUsers() {
//		Set<UserStats> active = Sets.newTreeSet();

		List<UserStats> active = new ArrayList<UserStats>();

		for (String user : statsByUser.asMap().keySet()) {
			if ((System.currentTimeMillis() - statsByUser.getUnchecked(user).lastAccess()) < 15000) {
//				active.add(user);
				active.add(statsByUser.getUnchecked(user));
			}
		}

		return active;
	}

	public boolean isActive(String user) {
		try {
			return statsByUser.get(user) != null;
		} catch (ExecutionException e) {
			return false;
		}
	}
}
