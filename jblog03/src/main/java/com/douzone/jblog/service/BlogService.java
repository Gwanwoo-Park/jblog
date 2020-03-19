package com.douzone.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.BlogVo;

@Service
public class BlogService {
	
	private static final String SAVE_PATH = "/jblog-uploads";
	private static final String URL = "/assets/sibal";

	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	public BlogVo find(String id) {
		return blogRepository.find(id);
	}
	
	public String restore(MultipartFile multipartFile) {
		String url = "";
		try {
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originFilename = multipartFile.getOriginalFilename();
			String extName = originFilename.substring(originFilename.lastIndexOf('.') + 1);
			String saveFilename = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("######### " + originFilename);
			System.out.println("######### " + saveFilename);
			System.out.println("######### " + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFilename;
		} catch(IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}
		
		return url;
	}

	private String generateSaveFilename(String extName) {
		String filename = "";
		
		Calendar calendar = Calendar.getInstance();
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);
		
		return filename;
	}

	public void update(BlogVo vo) {
		blogRepository.update(vo);
	}

	public BlogVo findAll(String id) {
		return null;
	}

	public Map<String, Object> getAll(String id, Long categoryNo, Long postNo) {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("blogVo", blogRepository.find(id));
		map.put("categoryList", categoryRepository.findAll(id));
		
		if(postNo != 0L) {
			map.put("postVo", postRepository.findContents(postNo));
			map.put("postList", postRepository.findAllMain(categoryNo));
		} else if(categoryNo != 0L) {
			Long no = postRepository.findPostNo(categoryNo);
			System.out.println(no);
			System.out.println(postRepository.findMainPostContents(no));
			map.put("postVo", postRepository.findMainPostContents(no));
			
			map.put("postList", postRepository.findAllMain(categoryNo));
		} else {
			Long categoryFirstNo = categoryRepository.findFirstNo(id);
			Long no = postRepository.findPostNo(categoryFirstNo);
			map.put("postList", postRepository.findAllMain(categoryFirstNo));
			map.put("postVo", postRepository.findMainPostContents(no));
		}
		
		return map;
	}
}