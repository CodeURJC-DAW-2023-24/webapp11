package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	public Optional<Category> findById(long id) {
		return repository.findById(id);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Category> findAll() {
		return repository.findAll();
	}

	public List<Category> findAjax(int page, int pageSize){ return repository.findAll(PageRequest.of(page,pageSize)).getContent();}

	public void save(Category category) {
		repository.save(category);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}


}
