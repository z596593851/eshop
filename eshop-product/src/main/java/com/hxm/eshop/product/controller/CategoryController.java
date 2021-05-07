package com.hxm.eshop.product.controller;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hxm.common.utils.R;
import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 商品三级分类
 *
 * @author xiaoming
 * @date 2021-04-26 21:30:08
 */
@Slf4j
@RestController
@RequestMapping("/product/category")
public class CategoryController {
    @Value("${fdfs.host}")
    private String host;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list/tree")
    public R list(){
        List<CategoryEntity> menus = categoryService.listTree();

        return R.ok().put("data", menus);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Valid @RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

    /**
     * 上传图片同时生成默认大小缩略图
     * @param file
     */
    @PostMapping("/img")
    public R uploadImage(@RequestParam("file") MultipartFile file){
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "安吉小小"));
        metaDataSet.add(new MetaData("CreateDate", new Date().toString()));
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), metaDataSet);
            String fullPath = storePath.getFullPath();
            String thumbImagePath = thumbImageConfig.getThumbImagePath(storePath.getPath());
            log.info("路径:{}",fullPath);
            log.info("缩略图路径:{}",thumbImagePath);
            return R.ok().put("url","http://"+host+"/"+fullPath);
        } catch (IOException e) {
            log.info("e:",e);
            return R.error();
        }
    }

    /**
     * 上传图片同时生成指定大小缩略图
     * @param file
     */
    @PostMapping("imgCustom")
    public void uploadimgCustom(@RequestParam("file") MultipartFile file) throws Exception {

        FastImageFile fastImageFile = crtFastImageFileOnly(file);

        StorePath storePath = fastFileStorageClient.uploadImage(fastImageFile);
        System.out.println("上传结果---" + storePath);
        //拿到元数据
        Set<MetaData> metadata = fastFileStorageClient.getMetadata(storePath.getGroup(), storePath.getPath());
        System.out.println("元数据---" + metadata);

        // 带分组的路径
        String fullPath = storePath.getFullPath();
        System.out.println("带分组的路径---" + fullPath);
        // 获取缩略图路径
        //String path = thumbImageConfig.getThumbImagePath(storePath.getPath());

    }

    private FastImageFile crtFastImageFileOnly(MultipartFile file) throws Exception {
        InputStream in = file.getInputStream();
        Set<MetaData> metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("Author", "Author"));
        metaDataSet.add(new MetaData("CreateDate", "当前时间"));
        String name = file.getOriginalFilename();
        String fileExtName = FilenameUtils.getExtension(name);
        return new FastImageFile.Builder()
                .withFile(in, file.getSize(), fileExtName)
                .withMetaData(metaDataSet)
                .build();
    }

}
