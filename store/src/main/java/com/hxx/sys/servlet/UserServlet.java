package com.hxx.sys.servlet;

import com.hxx.sys.bean.SysRole;
import com.hxx.sys.bean.SysUser;
import com.hxx.sys.service.IRoleService;
import com.hxx.sys.service.IUserService;
import com.hxx.sys.service.impl.IRoleServiceImpl;
import com.hxx.sys.service.impl.IUserServiceImpl;
import com.hxx.sys.utils.Constant;
import com.hxx.sys.utils.PageUtils;
import com.hxx.sys.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
/**
 * 系统用户的servlet
 * 处理客户端对用户的相关请求
 * 1.需要继承httpServlet
 * 2.重写对应的doGet的doPost方法
 * 3.通过@WebServlet注解定义相关的信息
 * urlPatterns定义客户端提交的请求和映射关系
 */
@WebServlet(name="userServlet",urlPatterns = {"/sys/userServlet"})
public class UserServlet extends BaseServlet{
    private IUserService service=new IUserServiceImpl();

    private IRoleService roleService=new IRoleServiceImpl();

    @Override
    public void list(HttpServletRequest req, HttpServletResponse resp)throws Exception {
        super.list(req,resp);//调用父类中方法完成分页数据的处理
        //执行分页查询
        service.ListPage(pageUtils);
        //将结果存储在request的作用域中
        req.setAttribute("pageUtils",pageUtils);
        //通过服务端转发方式,再页面跳转展示在页面上
        req.getRequestDispatcher("/sys/user/list.jsp").forward(req,resp);
    }

    @Override
    public void saveOrUpdatePage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //查询所有角色信息
        List<SysRole> roles = roleService.list(null);
        req.setAttribute("roles",roles);//传递



        //需要进入添加或更新页面
        String id=req.getParameter("id");
        if(StringUtils.isNotEmpty(id)){
            //说明是更新操作,需要回显具体的信息
            SysUser user = service.findById(Integer.parseInt(id));
            //entity实体
            req.setAttribute("entity",user);
        }
        req.getRequestDispatcher("/sys/user/addOrUpdate.jsp").forward(req,resp);
    }


    @Override
    public void saveOrUpdate(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        //验证是否存在该用户
        String userName = req.getParameter("usename");
        String s = service.checkUserName(userName);
        if(userName.length()<5){
            req.setAttribute("msg","用户名不少于5个字符");
            req.getRequestDispatcher("/sys/user/addOrUpdate.jsp").forward(req,resp);
        }else if(s.equals("error")){

                req.setAttribute("msg","用户名已存在");
            req.getRequestDispatcher("/sys/user/addOrUpdate.jsp").forward(req,resp);

        }else{
            //添加数据
            //获取客户端提交的数据表单
            String updateId=req.getParameter("id");

            String usename=req.getParameter("usename");
            String grade=req.getParameter("grade");
            String tol=req.getParameter("total");
            double total=Double.parseDouble(tol);
            String phone=req.getParameter("phone");
            String email=req.getParameter("email");
            String date=req.getParameter("createTime");
            Date createTime = java.sql.Date.valueOf(date);

            SysUser user=new SysUser();


            user.setUsename(usename);
            user.setGrade(grade);
            user.setTotal(total);
            user.setPhone(phone);
            user.setEmail(email);
            user.setCreateTime(createTime);
            System.out.println("添加成功");

            if(StringUtils.isNotEmpty(updateId)){
                //表示是更新操作
                user.setUid(Integer.parseInt(updateId));
                //调用更新方法
                service.updateById(user);

            }else{
                //表示添加操作
                service.save(user);
            }

            //保存数据，调用Service方法实现数据的存储
            //重定向的查询操作
            resp.sendRedirect("/sys/userServlet?action="+ Constant.BASE_ACTION_LIST);


        }


    }

    @Override
    public void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("id");
        int count=service.deleteById(Integer.parseInt(id));
        PrintWriter writer = resp.getWriter();
        writer.write(count+"");
        writer.flush();
        writer.close();
    }

    @Override
    public void findById(HttpServletRequest req, HttpServletResponse resp) throws Exception {

    }

    @Override
    public void findByName(HttpServletRequest req, HttpServletResponse resp) throws Exception {

    }

    @Override
    public void check(HttpServletRequest req, HttpServletResponse resp) throws Exception {



    }


}
