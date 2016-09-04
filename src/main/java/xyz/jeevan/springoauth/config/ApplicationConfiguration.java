package xyz.jeevan.springoauth.config;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

import xyz.jeevan.springoauth.converters.OAuthAuthenticationReadConverter;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 9:14:05 pm
 * @purpose
 *
 */

@Configuration
@PropertySources(value = { @PropertySource(value = "classpath:resource.properties") })
@ComponentScan("xyz.jeevan.springoauth")
@EnableMongoRepositories("xyz.jeevan.springoauth.repository")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	private Environment environment;

	private static final org.slf4j.Logger _log = org.slf4j.LoggerFactory.getLogger(ApplicationConfiguration.class);

	@Bean
	public MongoClient mongo() throws NumberFormatException, UnknownHostException {
		MongoCredential mongoCredential = MongoCredential.createCredential(environment.getProperty("mongodb.username"),
				environment.getProperty("mongodb.dbname"), environment.getProperty("mongodb.password").toCharArray());
		List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
		mongoCredentialList.add(mongoCredential);

		// Create client with configure database.
		MongoClient mongo = new MongoClient(getseeds(), mongoCredentialList);
		return mongo;
	}

	public @Bean MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(mongo(), environment.getProperty("mongodb.dbname"));
	}

	public @Bean MongoTemplate mongoTemplate() throws Exception {
		_log.info("Create mongodb template.");
		MongoDbFactory mongoFactory = mongoDbFactory();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoFactory, mongoConverter());
		mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		mongoTemplate.setReadPreference(ReadPreference.primaryPreferred());
		return mongoTemplate;
	}

	private List<ServerAddress> getseeds() {
		List<ServerAddress> serverAddress = new ArrayList<ServerAddress>();

		String hostUrl = environment.getProperty("mongodb.host.url");
		String port = environment.getProperty("mongodb.host.port");

		try {
			serverAddress.add(new ServerAddress(hostUrl, Integer.parseInt(port)));
		} catch (NumberFormatException ex) {
			_log.error("Unable to parse mongodb server port: " + port);
		} catch (UnknownHostException ex) {
			_log.error("Unknown host expection to parse mongodb congif as = host: " + hostUrl + " and port : " + port);
		}

		return serverAddress;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		// registry.addResourceHandler("/index.html").addResourceLocations("/WEB-INF/views/index.html");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("/index");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public ViewResolver configureViewResolver() {
		InternalResourceViewResolver viewResolve = new InternalResourceViewResolver();
		viewResolve.setPrefix("/WEB-INF/views/");
		viewResolve.setSuffix(".jsp");
		return viewResolve;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT")
				.allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")
				.exposedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization")
				.allowCredentials(false).maxAge(3600);
	}

	@Bean
	public CustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
		converters.add(new OAuthAuthenticationReadConverter());
		return new CustomConversions(converters);
	}

	@Bean
	public MappingMongoConverter mongoConverter() throws Exception {
		MongoMappingContext mappingContext = new MongoMappingContext();
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
		MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
		mongoConverter.setCustomConversions(customConversions());
		mongoConverter.afterPropertiesSet();
		return mongoConverter;
	}
}
