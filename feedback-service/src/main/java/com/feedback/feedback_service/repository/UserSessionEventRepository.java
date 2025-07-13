package com.feedback.feedback_service.repository;

import com.feedback.feedback_service.model.UserSessionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionEventRepository extends JpaRepository<UserSessionEvent,Long> {
}
