

[Spring与依赖注入_w3cschool](https://www.w3cschool.cn/javaweb/2yr91msw.html)



MP分页查询知识点


前端基础



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