package com.douzone.jblog.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.jblog.dto.JsonResult;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.vo.CategoryVo;

@RestController("CategoryApiController")
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/spa/category")
	public JsonResult list(@PathVariable("id") String id) {
		List<CategoryVo> list = categoryService.findAllJson(id);
		return JsonResult.success(list);
	}
	
	@PostMapping("/admin/category")
	public JsonResult add(
			@PathVariable("id") String id,
			@RequestBody CategoryVo vo) {
		vo.setId(id);
		categoryService.insertCategory(vo);
		return JsonResult.success(vo);
	}
	
	@DeleteMapping("/spa/delete")
	public JsonResult delete(
			@PathVariable("id") String id,
			@RequestBody CategoryVo vo) {
		categoryService.delete(vo);
		return JsonResult.success(vo);
	}
}