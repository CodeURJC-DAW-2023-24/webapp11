package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	private List<Category> allCategories;

	private int categoryRefreshSize = 1;

	private int nextCategoryIndex;

	public Optional<Category> findById(long id) {
		return repository.findById(id);
	}

	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Category> findAll() {
		return repository.findAll();
	}

	public List<String> findAllNames() {return repository.findAllNames();}

	public List<Category> findAjax(){
		this.allCategories = repository.findAll();
		this.nextCategoryIndex = this.categoryRefreshSize;
		if (allCategories.isEmpty()){
			return new ArrayList<>();
		}
		return allCategories.subList(0,this.categoryRefreshSize);
	}

	public void save(Category category) {
		repository.save(category);
	}

	public void delete(long id) {
		repository.deleteById(id);
	}

	public List<Category> getAllCategories() {
		return allCategories;
	}

	public int getCategoryRefreshSize() {
		return categoryRefreshSize;
	}

	public int getNextCategoryIndex() {
		return nextCategoryIndex;
	}

	public void setNextCategoryIndex(int nextCategoryIndex) {
		this.nextCategoryIndex = nextCategoryIndex;
	}



	public List<Integer> categoriesNumbers(){
		List<Integer> count = repository.findAllCategoriesUsedCount();
		List<Integer> allCategoriesUsedId = repository.findAllCategoriesUsedId();
		List<Integer> finalList= new ArrayList<>();
		int size = count.size();
		int i;
		for (i = 0; i<this.findAll().size();i++){
			finalList.add(0);
		}
		for (i = 0; i<size;i++) {
			finalList.set(allCategoriesUsedId.get(i) - 1,count.get(i));
		}

		return finalList;
	}
}
