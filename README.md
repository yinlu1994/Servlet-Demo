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
### （理解）ServletContext:上下文（全局管理者）
一个项目的引用，代表了当前项目。当项目启动的时候，服务器为每个web项目创建一个servletcontext对象，当项目被移除的时候，或者服务器关闭的时候servletcontext销毁
* 作用：
  * 获取全局的初始化参数
  * 共享资源（xxxAttribute）
  * 获取文件资源
  * 其他操作
* 常用的方法：
  * (了解）String getInitParameter(String key):通过名称获取指定的参数值
  * (了解)Enumeration getInitParameterNames():获取所有的参数名称,在根标签下有一个`<context-param>`子标签用来存放初始化参数
  ```(xml)
  <context-param>
    <param-name>encoding</param-name>
    <param-value>utf-8</param-value>
  </context-param>
  ```
  * xxxAttribute
  * getRealPath(String path):获取文件部署到tomcat上真是路径（带tomcat路径）返回String
  * getResourceAsStream(String path):以流的形式返回文件
  * 获取文件的mime类型 大类型/小类型
    * String getMimeType(String 文件类型)
* 获取全局管理者：
  * getServletContext()
### 域对象：
* 创建：在服务器启动的时候，服务器会为每一个项目创建一个全局管理者，servletcontext就是当前项目的引用
* 销毁：在项目被移除或者服务器关闭的时候销毁
* 常用方法：
  * xxxAttribute()
     * setAttribute(String key,Object Value):
     * Object getAttribute(String key)
     * removeAttribute(String key)
### 通过类加载器获取文件的路径（处于classes目录下的文件）
类.class.getClassLoader().getResource("文件目录").getPath()
### 步骤分析：
  * setAttribute(String key,Object value);//设置值
  * Object getAttribute(String key);//获取值 
  * removeAttribute(String key);//移除值  
复制之前的项目时，要右键->properties->web Project Settings->contect root:新名字
* 在项目启动的时候，初始化登陆次数
  * 在loginservlet的init无参的方法（继承override）中获取全局管理者，将值初始化为0，放入servletcontext上
  ```(java)
  //初始化登录次数
	@Override
	public void init() throws ServletException {
		//获取全局管理者
		ServletContext context = getServletContext(); 
		//初始化次数
		context.setAttribute("count", 0);
	}
  ```
* 登录成功之后，在loginservlet中获取全局管理者，获取登陆成功的总次数，
* 然后将次数+1，然后将值设置回去，
```(java)
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 0. 设置编码（显示中文）
		response.setContentType("text/html;charset=utf-8");
		// 1.接收用户名和密码
		String username = request.getParameter("username");//name属性中的string值
		String password = request.getParameter("password");
		
		// 2.调用userservice 里的login(username,password) 返回值：User user
		User user = null;
		try {
			user = new UserService().login(username,password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 3.判断user是否为空
		if(user == null) {
			// 3.1 若为空，写提示信息
			response.getWriter().print("用户名和密码不匹配");
			//案例2：定时跳转
			response.setHeader("refresh", "3;url=/Servlet_learning/login.htm");
		}else {
			// 3.2 若不为空，写提示信息
			response.getWriter().print(user.getUsername()+":欢迎回来");
			//4.登录成功之后，获取全局管理者
			ServletContext context = this.getServletContext();
			//5.获取总次数
			Integer cs = (Integer) context.getAttribute("count");
			//6.将次数+1
			cs++;
			//7.将次数赋予一个“count”名，再次放入域对象中
			context.setAttribute("count", cs);
		}
	}
```
* 当访问showServlet的时候设置全局管理者，获取登陆成功的总次数，然后在页面上打印出来即可
* 新建另外一个servlet文件，右键->new->other->servlet 选择重写doget()dopost()，取消选择构造器
```(java)
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  // 0.设置编码
response.setContentType("text/html;charset=utf-8");
//1.获取全局管理者
	ServletContext context = this.getServletContext();
//2.获取登陆的总次数
Integer cs = (Integer) context.getAttribute("count");
//3.在页面上打印总次数
response.getWriter().print("登陆成功的总次数："+cs);
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
}
```
* 在web.xml中设置LoginServlet.init()初始化操作随着服务器的启动而启动
```(xml)
<servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>com.servlet.webservlet.LoginServlet</servlet-class>
    <load-on-startup>2</load-on-startup>
  </servlet>
```
### (了解)servletConfig
* 它是servlet配置对象，是由服务器创建的，在创建servlet的同时也创建了它，通过servlet的init(ServletConfig config)将config对象传递给servlet,由servlet的getServletConfig方法获取
* 作用：
    * 获取当前servlet的名称
    * 获取当前servlet的初始化参数
    * 获取全局管理者
* 方法：
  * String getServletName():获取当前servlet的名称（web.xml配置的servlet-name）
  ```(java)
  protected void doGet(xxx){
      //获取sercletconfig
      ServletConfig config = this.getServletConfig();
      //获取当前servlet名称
      String servletName = config.getServletname();
      //获取初始化参数(例，获取user名)
      String user = config.getInitParameter("user");
      Enumeration<string> names = config.getInitParameterNames();
      while(names.hasMoreElements()){
        String name = names.nextElement();
	}
      }
  ```
  * String getInitParameter(String key):通过名称获取指定的参数值
  * Enumeration getInitParameterNames():获取所有的参数名称
    * 注：初始化参数是放在web.xml文件`<servlet>`标签的子标签`<init-param>`





	
