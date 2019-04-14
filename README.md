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
  * datasourceUtils  
  * c3p0-config.xml  
* 创建servlet(LoginServlet:路径 /login/ 下)
  * 接受用户名和密码
  * 调用service层（UserService）完成登录操作
  * 提示信息
* UserService
  * login(username,password)
  * 调用dao
* dao:通过用户名和密码查询数据库。
