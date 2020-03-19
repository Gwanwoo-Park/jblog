package com.douzone.jblog.repository;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.UserVo;

@Repository
public class BlogRepository {

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(UserVo vo) {
		sqlSession.insert("user.insert", vo);
		sqlSession.insert("blog.insert", vo);
		sqlSession.insert("category.insert", vo);
	}

	public UserVo findByIdAndPassword(UserVo vo) {
		return sqlSession.selectOne("user.findByIdAndPassword", vo);
	}

	public BlogVo find(String id) {
		return sqlSession.selectOne("blog.find", id);
	}

	public int update(BlogVo vo) {
		return sqlSession.update("blog.update", vo);
	}
}