package com.usit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usit.domain.Unsubscribe;

public interface UnsubscribeRepository extends JpaRepository<Unsubscribe, String> {
	public List<Unsubscribe> findAll();
}
