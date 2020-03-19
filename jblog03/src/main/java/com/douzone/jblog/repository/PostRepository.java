package com.douzone.jblog.repository;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;

@Repository
public class PostRepository {

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private SqlSession sqlSession;

	public void insert(PostVo vo) {
		sqlSession.insert("post.insert", vo);
	}

	public List<PostVo> findAllMain(Long categoryFirstNo) {
		return sqlSession.selectList("post.findAllMain", categoryFirstNo);
	}

	public PostVo findContents(Long postNo) {
		return sqlSession.selectOne("post.findContents", postNo);
	}

	public PostVo findMainPostContents(Long categoryFirstNo) {
		return sqlSession.selectOne("post.findMainPostContents", categoryFirstNo);
	}
}