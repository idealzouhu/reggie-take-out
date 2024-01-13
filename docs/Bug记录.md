## Bean 识别不到

问题记录：在 `EmployeeController` 类中使用了 `@Autowired` 注解来注入一个类型为 `EmployeeService` 的 bean，但是在 Spring 的应用上下文中找不到这个类型的 bean。

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Field employeeService in com.idealzouhu.reggie.controller.EmployeeController required a bean of type 'com.idealzouhu.reggie.service.EmployeeService' that could not be found.

The injection point has the following annotations:
	- @org.springframework.beans.factory.annotation.Autowired(required=true)


Action:

Consider defining a bean of type 'com.idealzouhu.reggie.service.EmployeeService' in your configuration.


Process finished with exit code 1

```

常见原因：

- **确保 `EmployeeService` 类上有 `@Service` 或 `@Component` 注解：** 在 `EmployeeService` 类上添加 `@Service` 或 `@Component` 注解，以确保它被 Spring 容器扫描并注册为一个 bean。
- **确保 `EmployeeController` 类上有 `@Controller` 或 `@RestController` 注解：** 确保 `EmployeeController` 类上添加了正确的注解，以使其成为 Spring 容器中的一个控制器。
- **确保包扫描路径正确：** 确保在主应用程序类上使用 `@SpringBootApplication` 注解时，`basePackages` 或 `basePackageClasses` 属性正确设置，以便 Spring 容器能够扫描到 `EmployeeService` 和 `EmployeeController` 所在的包。

- **检查依赖注入的字段名称和类型是否正确：** 确保 `EmployeeController` 中的字段名为 `employeeService`，并且确保 `EmployeeService` 类型的 bean 存在。

解决步骤：EmployeeServiceImpl 类的上面忘记加了 @Service 注解

```
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
```

有个问题，我实际上调用的是 EmployeeService， 为什么还跟 EmployeeServiceImpl 类 有关系



## 奇怪的前端页面跳转

问题：如果控制器里面的某个方法， 最后返回的是 null 的话。那么最后 页面会跳转到登录页面，并且会删除session里面的员工登录信息。



BUG原因：

`src/main/java/com/idealzouhu/reggie/filter/LoginCheckFilter.java`里面的方法没有实现号。注意，官方视频在这里少打了一个 return ，导致登录用户存放在session里面会被删掉（即误执行`response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")))` ，间接执行前端代码`localStorage.removeItem('userInfo')` ）。