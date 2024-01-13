package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.entity.Employee;
import com.idealzouhu.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);


        // 3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        // 4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        // 5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        // 6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 清除 session 中的 保存的当前登录员工的id
        log.info("员工退出登录");
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping("/addEmployee")
    public R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工， 员工信息：{}", employee.toString());

        // 设置初始密码 123456， 需要进行MD5 加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 以下注释代码用 公共字段填充 所取代
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        // log.info("新增员工，当前登录员工id为: {} ",request.getSession().getAttribute("employee"));
        // Long empID = (Long) request.getSession().getAttribute("employee"); // 获得当前登录用户的id
        // employee.setCreateUser(empID);
        // employee.setUpdateUser(empID);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 根据id 编辑员工 （事实上，这段代码与下面的功能”启用/禁用员工“一样, 都是 update 操作）
     * @param employee
     * @return
     */
    @PutMapping("/editEmployee")
    public R<String> editEmployee(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());
        // log.info("线程id: {}" , Thread.currentThread().getId()) ;

        // Long empID = (Long)request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empID);
        // employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name  按条件分页查询里面的条件
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name={} ", page, pageSize,name);

        // 1. 构造分页查询器
        Page pageInfo = new Page(page, pageSize);

        // 2. 构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name); // 添加过滤条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);  // 添加排序条件

        // 3.执行查询
        employeeService.page(pageInfo, lambdaQueryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据 id 修改员工信息, 即 启用/禁用 员工
     * @param employee
     * @return
     */
    @PutMapping("/enableOrDisableEmployee")
    public R<String> enableOrDisableEmployee(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());

        // Long empID = (Long)request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empID);
        // employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);

        return R.success("员工信息修改(启用/禁用员工)成功");
    }

    /**
     * 根据员工 id 查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据员工id（{}）查询信息：", id);
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
