package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.UserVo;

@Repository
public class UserRepository {

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
	
	public UserVo findByEmailAndPassword(String email, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("e", email);
		map.put("p", password);
		
		return sqlSession.selectOne("user.findByEmailAndPassword2", map);
	}

	public UserVo find(Long no) {
		return sqlSession.selectOne("user.find", no);
	}

	public int update(UserVo userVo) {
		return sqlSession.update("user.update", userVo);
	}
}