package com.mpt.journal.controller;

import com.mpt.journal.model.FlowerModel;
import com.mpt.journal.service.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/flowers")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @GetMapping
    public String showFlowers(@RequestParam(required = false) String search,
                              @RequestParam(required = false) Double minPrice,
                              @RequestParam(required = false) Double maxPrice,
                              @RequestParam(required = false) Integer categoryId,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        List<FlowerModel> filtered = flowerService.searchAndFilter(search, minPrice, maxPrice, categoryId);

        int totalCount = filtered.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        if (totalPages == 0) totalPages = 1;

        if (page > totalPages) page = totalPages;
        if (page < 1) page = 1;

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalCount);
        List<FlowerModel> flowers = (totalCount == 0) ? List.of() : filtered.subList(fromIndex, toIndex);

        Map<Integer, String> categoryNames = new HashMap<>();
        for (FlowerModel f : flowers) {
            categoryNames.put(f.getCategoryId(), flowerService.getCategoryNameById(f.getCategoryId()));
        }

        model.addAttribute("flowers", flowers);
        model.addAttribute("categories", flowerService.getAllCategories());
        model.addAttribute("categoryNames", categoryNames);

        model.addAttribute("searchTerm", search);
        model.addAttribute("filterCategoryId", categoryId);
        model.addAttribute("filterMinPrice", minPrice);
        model.addAttribute("filterMaxPrice", maxPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        return "flowers";
    }


    @PostMapping("/add")
    public String addFlower(@RequestParam String name,
                            @RequestParam double price,
                            @RequestParam int stockQuantity,
                            @RequestParam int categoryId) {
        flowerService.add(new FlowerModel(0, name, price, stockQuantity, categoryId));
        return "redirect:/flowers";
    }

    @PostMapping("/update")
    public String updateFlower(@RequestParam int id,
                               @RequestParam String name,
                               @RequestParam double price,
                               @RequestParam int stockQuantity,
                               @RequestParam int categoryId) {
        flowerService.update(new FlowerModel(id, name, price, stockQuantity, categoryId));
        return "redirect:/flowers";
    }

    @PostMapping("/delete")
    public String deleteFlower(@RequestParam int id) {
        flowerService.deletePhysical(id);
        return "redirect:/flowers";
    }

    @PostMapping("/soft-delete")
    public String softDeleteFlower(@RequestParam int id) {
        flowerService.deleteLogical(id);
        return "redirect:/flowers";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam(value = "ids", required = false) List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            flowerService.deleteMultiple(ids);
        }
        return "redirect:/flowers";
    }

    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultiple(@RequestParam(value = "ids", required = false) List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            flowerService.deleteLogicalMultiple(ids);
        }
        return "redirect:/flowers";
    }
}
