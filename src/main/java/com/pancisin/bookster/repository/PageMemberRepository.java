package com.pancisin.bookster.repository;

import com.pancisin.bookster.model.PageMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PageMemberRepository extends JpaRepository<PageMember, UUID> {

  @Query("SELECT pageMember FROM PageMember pageMember WHERE pageMember.page.id = :page_id")
  public List<PageMember> findByPage(@Param("page_id") Long page_id);

  @Query("SELECT pageMember FROM PageMember pageMember WHERE pageMember.page.id = :page_id AND pageMember.user.id = :user_id")
  public PageMember findByAttendance(@Param("page_id") Long page_id,
                                     @Param("user_id") Long user_id);
}
