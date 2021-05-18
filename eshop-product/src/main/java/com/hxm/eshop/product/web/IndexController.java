package com.hxm.eshop.product.web;

import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.service.CategoryService;
import com.hxm.eshop.product.vo.Catalogs2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/", "index.html"})
    private String indexPage(Model model) {
        //1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntities);
        return "index";
    }

    /**
     * 二级、三级分类数据
     *
     * @return
     */
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }
}
