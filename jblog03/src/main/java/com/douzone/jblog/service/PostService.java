package com.douzone.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.PostVo;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;

	public void insert(PostVo vo) {
		
		postRepository.insert(vo);
	}

	public Map<String, Object> findAllMain(Long categoryFirstNo) {
		
		List<PostVo> list = postRepository.findAllMain(categoryFirstNo);
		Map<String, Object> map = new HashMap<>();
		map.put("postList", list);
		map.put("postListCount", list.size());
		
		return map;
	}
}