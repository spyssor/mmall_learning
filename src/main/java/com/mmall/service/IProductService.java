package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVO;

public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVO> manageProductDetail(Integer productId);

    ServerResponse getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> productSearch(String productName, Integer productId, int pageNum, int pageSize);
}
