<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.security.Principal"%>
<%@page import="java.util.Locale"%>
<%@page import="org.skyve.metadata.user.User"%>
<%@page import="org.skyve.util.Util"%>
<%@page import="org.skyve.impl.web.UserAgent"%>
<%@page import="org.skyve.impl.web.WebUtil"%>
<%
	String basePath = Util.getSkyveContextUrl() + "/";
	boolean mobile = UserAgent.getType(request).isMobile();
	String referer = request.getHeader("Referer");
	Principal p = request.getUserPrincipal();
	User user = WebUtil.processUserPrincipalForRequest(request, (p == null) ? null : p.getName(), true);
	Locale locale = (user == null) ? request.getLocale() : user.getLocale();
%>
<!DOCTYPE html>
<html dir="<%=Util.isRTL(locale) ? "rtl" : "ltr"%>">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
		<title><%=Util.i18n("page.logout.title", locale)%></title>
		<base href="<%=basePath%>" />

		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="private,no-cache,no-store" />
		<meta http-equiv="expires" content="0" />

		<% if (mobile) { %>
			<meta name="format-detection" content="telephone=no" />
			<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />
		<% } %>

		<link rel="icon" type="image/png" href="images/window/skyve_fav.png" />
		<link rel="stylesheet" type="text/css" href="css/basic-min.css" />
	</head>
	<body>
		<table class="logo" align="center">
			<tr height="150px">
				<td><img src="images/skyve.png" alt="Skyve" /></td>
			</tr>
		</table>
		<%
			request.logout();

			// NB invalidate the session after logging out otherwise WebLogic 12c NPEs
			HttpSession s = request.getSession(false);
			if (s != null) {
				s.invalidate();
			}

			// remove all cookies too
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					cookie.setValue("-");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		%>
		<form>
			<table align="center">
				<tr>
					<td>
						<div class="loginTable">
							<table align="center" style="border-spacing: 10px;">
								<tr>
									<td style=";text-align:center">
										<div style="font-size:28px;"><%=Util.i18n("page.logout.banner", locale)%></div>
									</td>
								</tr>
								<tr>
									<td style="text-align: center;">
										<div class="buttonDiv">
											<% if (referer == null) { %>
												<a href="<%=request.getContextPath()%><%=org.skyve.util.Util.getHomeUri()%><%=(user == null) ? "" : (String.format("home?customer=%s", user.getCustomerName()))%>"><%=Util.i18n("page.login.submit.label", locale)%></a>
											<% } else { %>
												<a href="<%=referer%>"><%=Util.i18n("page.login.banner", locale)%></a>
											<% } %>
										</div>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
