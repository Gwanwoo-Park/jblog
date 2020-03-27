package com.douzone.jblog.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.security.Auth;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;

	@RequestMapping({"", "/{pathNo1}", "/{pathNo1}/{pathNo2}" })
	public String blogMain(
		@PathVariable String id,
		@PathVariable Optional<Long> pathNo1,
		@PathVariable Optional<Long> pathNo2,
		Model model) {

		Long categoryNo = 0L;
		Long postNo = 0L;
		
		if( pathNo2.isPresent()) {
			postNo = pathNo2.get();
			categoryNo = pathNo1.get();
		} else if( pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
		}
		
		Map<String, Object> map = blogService.getAll(id, categoryNo, postNo);
		
		BlogVo blogVo = (BlogVo) map.get("blogVo");
		if(blogVo == null) {
			return "error/404";
		}
		
		model.addAllAttributes(map);
		
		return "blog/blog-main";
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String blogMain(@PathVariable("id") String id,
			@RequestParam(value = "title", required = true, defaultValue = "수정 안됨") String title,
			@RequestParam(value = "logo-file") MultipartFile multipartFile, Model model) {

		BlogVo vo = new BlogVo();

		if (multipartFile.isEmpty()) {
			BlogVo newBlogVo = new BlogVo();
			newBlogVo = blogService.find(id);
			vo.setLogo(newBlogVo.getLogo());
		} else {
			String url = blogService.restore(multipartFile);
			vo.setLogo(url);
		}

		vo.setId(id);
		vo.setTitle(title);

		blogService.update(vo);

		model.addAttribute("vo", vo);

		return "redirect:/" + id;
	}

	@Auth
	@RequestMapping(value = "/admin/basic", method = RequestMethod.GET)
	public String blogAdminBasic(@PathVariable("id") String id, Model model) {
		BlogVo vo = blogService.find(id);
		model.addAttribute("blogVo", vo);

		return "blog/blog-admin-basic";
	}

	@Auth
	@RequestMapping(value = "/admin/category", method = RequestMethod.GET)
	public String blogAdminCatogory(@PathVariable("id") String id, Model model) {
		Map<String, Object> map = new HashMap<>();
		map = categoryService.findAll(id);
		
		int listSize = (int) map.get("listCount");
		int deleteFalse = 0;
		if(listSize == 1) {
			deleteFalse = 1;
			map.put("deleteFalse", deleteFalse);
		} else {
			deleteFalse = 0;
			map.put("deleteFalse", deleteFalse);
		}
		
		BlogVo vo = blogService.find(id);
		map.put("blogVo", vo);
		
		model.addAllAttributes(map);

		return "blog/blog-admin-category";
	}

	@Auth
	@RequestMapping(value = "/admin/category", method = RequestMethod.POST)
	public String blogAdminCatogory(@PathVariable("id") String id,
			@RequestParam(value = "name", required = true, defaultValue = "") String name,
			@RequestParam(value = "description", required = true, defaultValue = "") String description, Model model) {

		CategoryVo vo = new CategoryVo();
		vo.setId(id);
		vo.setName(name);
		vo.setDescription(description);

		categoryService.insertCategory(vo);

		return "redirect:/" + id + "/admin/category";
	}

	@Auth
	@RequestMapping("/{no}/delete")
	public String categoryDelete(@PathVariable("id") String id, @PathVariable("no") Long no, Model model) {

		CategoryVo vo = new CategoryVo();
		vo.setId(id);
		vo.setNo(no);
		int countCategory = categoryService.categoryCount(id);
		int countPost = postService.postCount(no);
		if(countCategory > 1 || countPost != 0 )
			return "redirect:/" + id + "/admin/category";
		categoryService.delete(vo);

		return "redirect:/" + id + "/admin/category";
	}

	@Auth
	@RequestMapping(value = "/admin/write", method = RequestMethod.GET)
	public String blogAdminWrite(@PathVariable("id") String id, Model model) {

		Map<String, Object> map = new HashMap<>();
		map = categoryService.findAll(id);
		BlogVo vo = blogService.find(id);
		map.put("blogVo", vo);
		model.addAllAttributes(map);

		return "blog/blog-admin-write";
	}

	@Auth
	@RequestMapping(value = "/admin/write", method = RequestMethod.POST)
	public String blogAdminWriteSuccess(@PathVariable("id") String id,
			@RequestParam(value = "categoryNo", required = true, defaultValue = "") Long categoryNo,
			@RequestParam(value = "title", required = true, defaultValue = "") String title,
			@RequestParam(value = "contents", required = true, defaultValue = "") String contents, Model model) {

		PostVo vo = new PostVo();
		vo.setCategoryNo(categoryNo);
		vo.setTitle(title);
		vo.setContents(contents);

		postService.insert(vo);

		return "redirect:/" + id;
	}

}