package com.EventCrafters.EventCrafters.service;

import com.EventCrafters.EventCrafters.model.Category;
import com.EventCrafters.EventCrafters.repository.CategoryRepository;
import com.EventCrafters.EventCrafters.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Autowired
	private EventRepository eventRepository;

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
		this.nextCategoryIndex = this.categoryRefreshSize + 1;
		if (allCategories.isEmpty() || allCategories.size() == 1){
			return new ArrayList<>();
		} else if (allCategories.size() <= nextCategoryIndex){
			return allCategories.subList(1,allCategories.size());
		}
		return allCategories.subList(1,this.nextCategoryIndex);
	}

	public void save(Category category) {
		repository.save(category);
	}

	@Transactional
	public void delete(long id) {
		if (id == 1) {
			throw new RuntimeException("No se puede eliminar la categoría predeterminada");
		}

		repository.reassignEventsToDefaultCategory(id);
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
		List<Integer> finalList= new ArrayList<>();
		int size = count.size();
		int aux = this.findAll().size();
		int i;
		for (i = 0; i< aux;i++){
			finalList.add(0);
		}
		for (i = 0; i<size;i++) {
			finalList.set(i,count.get(i));
		}

		return finalList;
	}
}
