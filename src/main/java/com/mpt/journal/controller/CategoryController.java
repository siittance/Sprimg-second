package com.mpt.journal.controller;

import com.mpt.journal.model.CategoryModel;
import com.mpt.journal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {

        List<CategoryModel> sourceList = (search != null && !search.isEmpty()) ?
                categoryService.search(search) : categoryService.getActive();

        int totalCount = sourceList.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        if (totalPages == 0) totalPages = 1;
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalCount);
        List<CategoryModel> categories = (totalCount == 0) ? List.of() : sourceList.subList(fromIndex, toIndex);

        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("searchTerm", search);

        return "categories";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam String description) {
        categoryService.add(new CategoryModel(0, name, description));
        return "redirect:/categories";
    }

    @PostMapping("/soft-delete")
    public String softDeleteCategory(@RequestParam int id) {
        categoryService.softDelete(id);
        return "redirect:/categories";
    }


    @PostMapping("/update")
    public String updateCategory(@RequestParam int id,
                                 @RequestParam String name,
                                 @RequestParam String description) {
        categoryService.update(new CategoryModel(id, name, description));
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam int id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam(value = "ids", required = false) String ids) {
        if (ids != null && !ids.isEmpty()) {
            List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            categoryService.deleteMultiple(idList);
        }
        return "redirect:/categories";
    }
}
