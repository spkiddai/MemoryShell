<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <title>Tomcat内存马测试</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
  <style>
    body {
      background-color: #FFFFFF; /* 白色背景 */
      color: #333333; /* 深灰色 */
      font-family: 'KaiTi', '楷体', serif; /* 指定楷体字体 */
    }
    .container {
      margin-top: 50px;
    }
    h1, h3 {
      font-weight: bold;
    }
    .section {
      margin-top: 30px;
    }
    .btn-group {
      display: flex;
      flex-wrap: wrap;
    }
    .btn-group > .col-md-3 > a {
      margin: 5px 0; /* 每个按钮的间距 */
      display: block;
    }
  </style>
</head>
<body>
<div class="container">
  <h1 class="display-4 mb-4">内存马演示平台</h1>

  <!-- 访问测试部分 -->
  <div class="row section">
    <div class="col-md-12">
      <h3>访问测试</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/test" class="btn btn-outline-primary">默认演示页</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/servlet/index" class="btn btn-outline-primary">Servlet演示页</a>
        </div>
      </div>
    </div>
  </div>

  <!-- 注册示例部分 -->
  <div class="row section">
    <div class="col-md-12">
      <h3>注册示例</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/servlet/test" class="btn btn-outline-primary">注册Servlet</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/filter/test" class="btn btn-outline-primary">注册Filter</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/listener/test" class="btn btn-outline-primary">注册Listener</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/valve/test" class="btn btn-outline-primary">注册Valve</a>
        </div>
      </div>
    </div>
  </div>

  <!-- 内存马示例部分 -->
  <div class="row section">
    <div class="col-md-12">
      <h3>内存马示例</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/servlet/memshell" class="btn btn-outline-primary">Servlet内存马</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/filter/memshell" class="btn btn-outline-primary">Filter内存马</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/listener/memshell" class="btn btn-outline-primary">Listener内存马</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/valve/memshell" class="btn btn-outline-primary">Valve内存马</a>
        </div>
      </div>
    </div>
  </div>

  <!-- 销毁示例部分 -->
  <div class="row section">
    <div class="col-md-12">
      <h3>销毁示例</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/servlet/delete" class="btn btn-outline-primary">销毁Servlet</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/filter/delete" class="btn btn-outline-primary">销毁Filter</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/listener/delete" class="btn btn-outline-primary">销毁Listener</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/valve/delete" class="btn btn-outline-primary">销毁Valve</a>
        </div>
      </div>
    </div>
  </div>

  <div class="row section">
    <div class="col-md-12">
      <h3>JavaAgent示例</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/agent/test" class="btn btn-outline-primary">注册JavaAgent示例</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/agent/reflect" class="btn btn-outline-primary">注册JavaAgent示例(反射调用)</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/agent/shell" class="btn btn-outline-primary">注册JavaAgent内存马</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/agent/rasp" class="btn btn-outline-primary">注册JavaAgent RASP</a>
        </div>
      </div>
    </div>
  </div>


  <div class="row section">
    <div class="col-md-12">
      <h3>Msmap工具注入示例</h3>
      <div class="btn-group">
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/msmap/servlet" class="btn btn-outline-primary">Msmap Servlet</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/msmap/filter" class="btn btn-outline-primary">Msmap Filter</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/msmap/listener" class="btn btn-outline-primary">Msmap Listener</a>
        </div>
        <div class="col-md-3">
          <a href="${pageContext.request.contextPath}/msmap/valve" class="btn btn-outline-primary">Msmap Valve</a>
        </div>
      </div>
    </div>
  </div>

</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
