package com.douzone.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public Map<String, Object> findAll(String id) {
		
		List<CategoryVo> list = categoryRepository.findAll(id);
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("listCount", list.size());
		
		return map;
	}

	public void insertCategory(CategoryVo vo) {
		
		categoryRepository.insertCategory(vo);
	}

	public void delete(CategoryVo vo) {
		categoryRepository.delete(vo);
	}

	public Map<String, Object> findAllMain(String id) {
		List<CategoryVo> list = categoryRepository.findAllMain(id);
		Map<String, Object> map = new HashMap<>();
		map.put("categoryList", list);
		map.put("categoryListCount", list.size());
		
		return map;
	}

	public Long findFirstNo(String id) {
		return categoryRepository.findFirstNo(id);
	}

	public int categoryCount(String id) {
		return categoryRepository.categoryCount(id);
	}

	public List<CategoryVo> findAllJson(String id) {
		return categoryRepository.findAllJson(id);
	}
}