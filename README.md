# Servlet-Demo
实现登陆页面,(servlet / C3p0 / Mysql)
* 建数据库，表  
* 创建工程，将静态登陆页面导入webcontent/下  
* 修改login.htm  
  * 给用户名和密码添加name属性  
  * 修改表单<form>的'action'属性，action = "http://localhost/Servlet_learning2/login.htm"  
  * 添加'method'属性，method = "post"  
* 导入jar包:mysql驱动 / dbutils / c3p0   
  * 将jar包放到WebInf/lib/ 下，会自动导包在web app libraries  
* 导入工具类和配置文件  
  * datasourceUtils.java(src/xxx包/)：工具类集合了以下方法：  
    用c3p0获取数据源，创建连接；  
    释放资源；  
  * c3p0-config.xml(/src/)  
    修改基本配置信息； 
  * web.xml(WEB-INF/lib)  
    配置servlet到web服务器，配置servlet的类全路径，servlet的名称，servlet的访问路径
 
* 创建servlet(LoginServlet.java:路径 /login/ 下)选择继承 HttpServlet 
  * 重写doPost(HttpServletRequest request, HttpServletResponse response)
  * 接受用户名和密码
  * 调用service层（UserService）返回user,完成登录操作
  * 提示信息
* UserService
  * login(username,password)
  * 调用dao
* dao: 
  * 通过用户名和密码查询数据库。c3p0
## 实现在提示用户名不匹配后，3秒之后跳转
* response.setHeader("refresh","3,url=/登陆界面")；//三秒后刷新
## 统计登录成功的总人次
* 需求：在一个用户登陆成功之后，获取之前登录成功总人次，将次数+1，在访问另一个servlet的时候，显示登陆成功的总人次
* 注意：他是一个单实例多线程的操作，一个计数器要考虑并发的问题
* 技术分析：`ServletContext`
### ServletContext:上下文（全局管理者）
* 常用的方法：
  * setAttribute(String key,Object value);//设置值
  * Object getAttribute(String key);//获取值 
  * removeAttribute(String key);//移除值
* 获取全局管理者：
  * getServletContext()
### 步骤分析：
* 在项目启动的时候，初始化登陆次数
  * 在loginservlet的init方法中获取全局管理者，将值初始化为0，放入servletcontext上
* 登录成功之后，在loginservlet中获取全局管理者，获取登陆成功的总次数，
* 然后将次数+1，然后将值设置回去，
* 当访问showServlet的时候设置全局管理者，获取登陆成功的总次数，然后在页面上打印出来即可
