package xyz.jeevan.springoauth.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 8:53:54 pm
 * @purpose Web application configuration (same as what we used to do in web.xml
 *          file earlier, remember?).
 *
 */
public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.setConfigLocation("xyz.jeevan.springoauth.config");

		// to manage life cycle of root application context.
		servletContext.addListener(new ContextLoaderListener(rootContext));

		// Register and map dispatcher servlet.
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(rootContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");
	}

}
