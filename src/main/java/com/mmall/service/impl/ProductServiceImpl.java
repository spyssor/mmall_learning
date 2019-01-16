package com.mmall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product){

        if (product != null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null){
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }
                ServerResponse.createByErrorMessage("更新产品失败");
            }else{
                int rowCount = productMapper.insert(product);
                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){

        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0){
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> manageProductDetail(Integer productId){

        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        //VO对象--value object
        //pojo->bo(business object)->vo(view object)
        //pojo->vo(view object)
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        return ServerResponse.createBySuccess(productDetailVO);
    }

    private ProductDetailVO assembleProductDetailVO(Product product){

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());

        //imageHost
        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetailVO.setParentCategoryId(0);
        }else{
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        //createTime
        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        //updateTime
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));


        return productDetailVO;
    }

    @Override
    public ServerResponse getProductList(int pageNum, int pageSize){

        //startPage--start
        //填充sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVO productListVO = assembleProductListVO(productItem);
            productListVOList.add(productListVO);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVO assembleProductListVO(Product product){

        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setName(product.getName());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setStatus(product.getStatus());
        productListVO.setPrice(product.getPrice());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVO.setCategoryId(product.getCategoryId());

        return productListVO;
    }

    @Override
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, int pageNum, int pageSize){

        PageHelper.startPage(pageNum, pageSize);
        if (productName != null){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVO productListVO = assembleProductListVO(productItem);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
