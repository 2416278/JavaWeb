package com.hxx.store.dao.impl;

import com.hxx.store.bean.Cart;
import com.hxx.store.dao.CartDao;
import com.hxx.sys.bean.SysGoods;
import com.hxx.sys.bean.SysMenu;
import com.hxx.sys.bean.SysRole;
import com.hxx.sys.dao.IGoodsDao;
import com.hxx.sys.utils.MyDbUtils;
import com.hxx.sys.utils.PageUtils;
import com.hxx.sys.utils.StringUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {
    @Override
    public List<Cart> List(Cart entity) {
        QueryRunner queryRunner= MyDbUtils.getQueryRunner();
        String sql = "select * from cart";

        try {

            List<Cart>List=queryRunner.query(sql, new ResultSetHandler<java.util.List<Cart>>() {
                @Override
                public java.util.List<Cart> handle(ResultSet resultSet) throws SQLException {
                    List<Cart>list=new ArrayList<>();
                    while(resultSet.next()){
                        if(resultSet.getInt("amount")==0){
                            //数量为0移除
                            continue;
                        }else{
                            Cart entity=new Cart();
                            entity.setId(resultSet.getInt("id"));
                            entity.setImg(resultSet.getString("img"));
                            entity.setName(resultSet.getString("name"));
                            entity.setPrice(resultSet.getDouble("price"));
                            entity.setAmount(resultSet.getInt("amount"));
                            entity.setIsPay(resultSet.getInt("isPay"));
                            list.add(entity);
                        }
                    }
                    return list;
                }
            });
            return List;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Cart> ListPage(PageUtils pageUtils) {
        QueryRunner queryRunner= MyDbUtils.getQueryRunner();
        String sql = "select * from cart where isPay=0";
        if(StringUtils.isNotEmpty(pageUtils.getKey())){
            // 需要带条件查询
            sql+= " and name like '%"+pageUtils.getKey()+"%' ";
        }
        sql+=" limit ?,? ";
        //计算 分页开始的页数
        int startNo=pageUtils.getStart();
        try {
            List<Cart>List=queryRunner.query(sql, new ResultSetHandler<java.util.List<Cart>>() {
                @Override
                public java.util.List<Cart> handle(ResultSet resultSet) throws SQLException {
                    List<Cart>list=new ArrayList<>();
                    while(resultSet.next()){
                        Cart entity=new Cart();
                        entity.setId(resultSet.getInt("id"));
                        entity.setImg(resultSet.getString("img"));
                        entity.setName(resultSet.getString("name"));
                        entity.setPrice(resultSet.getDouble("price"));
                        entity.setAmount(resultSet.getInt("amount"));
                        entity.setIsPay(resultSet.getInt("isPay"));
                         list.add(entity);
                    }
                    return list;
                }
            },startNo,pageUtils.getPageSize());
            return List;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


    @Override
    public int save(Cart entity) {
        QueryRunner queryRunner=MyDbUtils.getQueryRunner();//连接池queryRunner对象
        String sql="insert into cart(img,name,price,amount)values(?,?,?,?)";
        try {
            return queryRunner.update(sql,entity.getImg(),entity.getName(),entity.getPrice(),entity.getAmount());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("插入失败");
        return -1;
    }

    @Override
    public Cart findById(int id) {
        QueryRunner queryRunner= MyDbUtils.getQueryRunner();
        String sql="select * from cart where id=?";
        try {
          return queryRunner.query(sql, new ResultSetHandler<Cart>() {
                @Override
                public Cart handle(ResultSet resultSet) throws SQLException {
                    if(resultSet.next()){
                        Cart entity=new Cart();
                        entity.setId(resultSet.getInt("id"));
                        entity.setImg(resultSet.getString("img"));
                        entity.setName(resultSet.getString("name"));
                        entity.setPrice(resultSet.getDouble("price"));
                        entity.setAmount(resultSet.getInt("amount"));
                        entity.setIsPay(resultSet.getInt("isPay"));
                        return entity;
                    }
                    return null;
                }
            },id);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }



    public int delete() {
        QueryRunner queryRunner = MyDbUtils.getQueryRunner(); // 连接池queryRunner对象
        String sql = "delete from cart where amount=?";
        try {
            return queryRunner.update(sql, 0); // 将参数amount为0
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public int count(PageUtils pageUtils) {
        QueryRunner queryRunner=MyDbUtils.getQueryRunner();
        String sql="select count(1) from cart where isPay=0";
        if(StringUtils.isNotEmpty(pageUtils.getKey())){
            // 需要带条件查询
            sql+= " and goodsName like '%"+pageUtils.getKey()+"%' ";
        }
        try {
            return queryRunner.query(sql,new ResultSetHandler<Integer>(){

                @Override
                public Integer handle(ResultSet resultSet) throws SQLException {
                    resultSet.next();
                    return resultSet.getInt(1);
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteByName(String name) {
        QueryRunner queryRunner = MyDbUtils.getQueryRunner(); // 连接池queryRunner对象
        String sql = "delete from cart where name=?";
        try {
            return queryRunner.update(sql, name); // 将参数amount为0
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteById(int id) {
        QueryRunner queryRunner = MyDbUtils.getQueryRunner(); // 连接池queryRunner对象
        String sql = "delete from cart where id=?";
        try {
            return queryRunner.update(sql, id); // 将参数amount为0
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateAmount(String name, String amount) {
        QueryRunner queryRunner=MyDbUtils.getQueryRunner();//连接池queryRunner对象
        String sql="update cart set amount=? where name=?";
        try {
            queryRunner.update(sql,amount,name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Cart findByName(String name) {
        QueryRunner queryRunner= MyDbUtils.getQueryRunner();
        String sql="select * from cart where name=?";
        try {
            return queryRunner.query(sql, new ResultSetHandler<Cart>() {
                @Override
                public Cart handle(ResultSet resultSet) throws SQLException {
                    if(resultSet.next()){
                        Cart entity=new Cart();
                        entity.setId(resultSet.getInt("id"));
                        entity.setImg(resultSet.getString("img"));
                        entity.setName(resultSet.getString("name"));
                        entity.setPrice(resultSet.getDouble("price"));
                        entity.setAmount(resultSet.getInt("amount"));
                        entity.setIsPay(resultSet.getInt("isPay"));
                        return entity;
                    }
                    return null;
                }
            },name);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public void saveStateByName(String name) {
        QueryRunner queryRunner=MyDbUtils.getQueryRunner();//连接池queryRunner对象
        String sql="update cart set isPay=? where name=?";
        try {
            queryRunner.update(sql,1,name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void saveAmountByName(Integer amount, String name) {
        QueryRunner queryRunner = MyDbUtils.getQueryRunner(); // 连接池queryRunner对象
        String sql = "update cart set amount = amount + ? where name = ?";
        try {
            queryRunner.update(sql, amount, name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Cart findNotPayByName(String name) {
        QueryRunner queryRunner = MyDbUtils.getQueryRunner();
        String sql = "select * from cart where name = ? and isPay = 0";
        try {
            return queryRunner.query(sql, new ResultSetHandler<Cart>() {
                @Override
                public Cart handle(ResultSet resultSet) throws SQLException {
                    if (resultSet.next()) {
                        Cart entity = new Cart();
                        entity.setId(resultSet.getInt("id"));
                        entity.setImg(resultSet.getString("img"));
                        entity.setName(resultSet.getString("name"));
                        entity.setPrice(resultSet.getDouble("price"));
                        entity.setAmount(resultSet.getInt("amount"));
                        entity.setIsPay(resultSet.getInt("isPay"));
                        return entity;
                    }
                    return null;
                }
            }, name);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

}
