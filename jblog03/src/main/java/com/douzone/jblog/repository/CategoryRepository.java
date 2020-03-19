package com.douzone.jblog.repository;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private SqlSession sqlSession;

	public List<CategoryVo> findAll(String id) {
		return sqlSession.selectList("category.findAll", id);
	}

	public void insertCategory(CategoryVo vo) {
		
		sqlSession.insert("category.insertCategory", vo);
	}

	public void delete(CategoryVo vo) {
		sqlSession.delete("category.delete", vo);
	}

	public List<CategoryVo> findAllMain(String id) {
		return sqlSession.selectList("category.findAllMain", id);
	}

	public Long findFirstNo(String id) {
		return sqlSession.selectOne("category.findFirstNo", id);
	}

	public List<CategoryVo> lookUp() {
		return sqlSession.selectList("category.lookUp");
	}
}