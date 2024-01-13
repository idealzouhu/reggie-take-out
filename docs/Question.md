

[Spring与依赖注入_w3cschool](https://www.w3cschool.cn/javaweb/2yr91msw.html)



MP分页查询知识点



```
categoryService.removeById(category);
```

这个是什么情况？




前端基础



@Component  有什么用

## 参数流转

@RequestBody  的作用： 将传进来的 json 数据格式封装成 某个entity对象

@GetMapping("/{id}") 的作用，@PathVariable 的作用

```
@GetMapping("/{id}")
public R<Employee> getById(@PathVariable Long id){
    log.info("根据员工id（{}）查询信息：", id);
    Employee employee = employeeService.getById(id);
    return R.success(employee);
}
```





```
@GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
    }
```

这个是怎么解析 URI 里面自带的参数？

[基于注解的URL映射_w3cschool](https://www.w3cschool.cn/javaweb/h1ym1mt8.html)

[JSON_Java Web_w3cschool](https://www.w3cschool.cn/javaweb/86io1mtb.html)



## 消息转换器 配置







## TreadLocal

什么是ThreadLocal?
ThreadLocal并不是一个Thread，而是Thread的局部变量。当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。ThreadLocal为每个线程提供单独一份存储空间，具有线程隔离的效果，只有在线程内才能获取到对应的值，线程外则不能访问。
ThreadLocal常用方法:
. public void set(T value)设置当前线程的线程局部变量的值. 

public T get()
返回当前线程所对应的线程局部变量的值

我们可以在LoginCheckFilter的 doFilter 方法中获取当前登录用户id，并调用ThreadLocal的set方法来设置当前线程的线程局部变量的值（用户id)，然后在MyMetaObjectHandler的updateFill方法中调用ThreadLocal的get方法来获得当前线程所对应的线程局部变量的值（用户id)。



# 数据库

约束 没有看懂



# 有意思的观点

 <font color="red">**客户端发送的每次 http 请求，对应的在服务端都会分配一个新的线程来处理**</font>







在Spring里面， 请求/ 的方法 ， 可以根据请求方法 get、post、delete找到不同的控制器方法。