
<%--
  Created by IntelliJ IDEA.
  User: diva
  Date: 08/01/2025
  Time: 07:41
  To change this template use File | Settings | File Templates.
--%>


    <div class="navbar-bg"></div>
    <nav class="navbar navbar-expand-lg main-navbar sticky">
    <div class="form-inline mr-auto">
        <ul class="navbar-nav mr-3">
            <li><a href="#" data-toggle="sidebar" class="nav-link nav-link-lg
									collapse-btn"> <i data-feather="align-justify"></i></a></li>
            <li><a href="#" class="nav-link nav-link-lg fullscreen-btn">
                <i data-feather="maximize"></i>
            </a></li>
        </ul>
    </div>
</nav>
    <div class="main-sidebar sidebar-style-2">
      <aside id="sidebar-wrapper">
        <div class="sidebar-brand">
          <a href="index.html"> <img alt="image" src="<%=request.getContextPath()%>/assets/img/mozart.png" class="header-logo" /> <span
                  class="logo-name">DS Studio</span>
          </a>
        </div>
        <ul class="sidebar-menu">
            <li class="menu-header">Main</li>
            <li class="dropdown">
            <a href="<%=request.getContextPath()%>/index.jsp" class="nav-link"><i class="fas fa-file-audio"></i><span>Generateur de son</span></a>
            </li>
            <li class="dropdown">
            <a href="<%=request.getContextPath()%>/modification.jsp" class="nav-link"><i class="fas fa-edit"></i><span>Modificateur de son</span></a>
            </li>

<%--          <li class="dropdown">--%>
<%--            <a href="#" class="menu-toggle nav-link has-dropdown"><i--%>
<%--                    data-feather="briefcase"></i><span>Widgets</span></a>--%>
<%--            <ul class="dropdown-menu">--%>
<%--              <li><a class="nav-link" href="widget-chart.html">Chart Widgets</a></li>--%>
<%--              <li><a class="nav-link" href="widget-data.html">Data Widgets</a></li>--%>
<%--            </ul>--%>
<%--          </li>--%>
        </ul>
      </aside>
    </div>
