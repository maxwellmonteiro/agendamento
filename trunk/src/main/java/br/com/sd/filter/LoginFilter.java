package br.com.sd.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.sd.entity.Usuario;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	private void toLoginPage(ServletRequest request, ServletResponse response) throws IOException, ServletException {		
		((HttpServletResponse) response).sendRedirect("/sd/login");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		
		if (session.isNew()) {
			toLoginPage(request, response);
		} else {
			Usuario usuario = (Usuario) session.getAttribute("usuario");
			
			if (usuario == null) {
				toLoginPage(request, response);
			} else {
				chain.doFilter(request, response);
			}
		}		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
