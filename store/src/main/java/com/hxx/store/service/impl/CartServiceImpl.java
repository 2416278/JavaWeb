package com.hxx.store.service.impl;

import com.hxx.store.bean.Cart;
import com.hxx.store.dao.CartDao;
import com.hxx.store.dao.impl.CartDaoImpl;
import com.hxx.store.service.CartService;
import com.hxx.sys.utils.PageUtils;

import java.util.List;

public class CartServiceImpl implements CartService {
    private CartDao dao=new CartDaoImpl();

    @Override
    public List<Cart> list(Cart entity) {
        return dao.List(entity);
    }

    @Override
    public void ListPage(PageUtils pageUtils) {
        List<Cart> carts = dao.ListPage(pageUtils);
        int count = dao.count(pageUtils);
        pageUtils.setList(carts);
        pageUtils.setTotalCount(count);


    }

    @Override
    public int count(PageUtils pageUtils) {
        return dao.count(pageUtils);
    }

    @Override
    public int save(Cart entity) {
        return dao.save(entity);
    }

    @Override
    public Cart findById(int id) {
        return dao.findById(id);
    }

    @Override
    public Cart findByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public int delete() {
        return dao.delete();
    }

    @Override
    public int deleteById(int id) {
        return dao.deleteById(id);
    }

    @Override
    public int deleteByName(String name) {
        return dao.deleteByName(name);
    }

    @Override
    public Cart updateAmount(String id, String amount) {
         dao.updateAmount(id,amount);
        return null;
    }

    @Override
    public void saveStateByName(String name) {
        dao.saveStateByName(name);
    }

    @Override
    public void saveAmountByName(Integer amount,String name) {
        dao.saveAmountByName(amount,name);
    }

    @Override
    public Cart findNotPayByName(String name) {
        return dao.findNotPayByName(name);
    }


}
