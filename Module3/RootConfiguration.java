/*
 * The MIT License (MIT)
 * Copyright (C) 2012 Jason Ish
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the Software), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uwyo.cs.acm2013.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;

import uwyo.cs.acm2013.dao.Acm2013DAO;
import uwyo.cs.acm2013.dao.Acm2013DAOImpl;
import uwyo.cs.acm2013.dao.Acm2013DAOMockImpl;
import uwyo.cs.acm2013.dao.TestDAO;
import uwyo.cs.acm2013.dao.TestDAOImpl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;

/**
 * The root application context.
 * <p/>
 * Scanning is enabled but will skip @Configuration and @Controller classes.
 *
 * @Configuration classes are skipped to prevent picking theses ones up again as
 * these files are in the scan path.  @Controller classes will be picked up by
 * MvcConfiguration.
 */
@Configuration
@Import({JettyConfiguration.class, SpringSecurityConfiguration.class})
@ComponentScan(basePackages = {"uwyo.cs.acm2013"},
        excludeFilters = {@ComponentScan.Filter(Controller.class),
                @ComponentScan.Filter(Configuration.class)})
public class RootConfiguration {

    /**
     * Allows access to properties. eg) @Value("${jetty.port}").
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    /**
     * Access to default database
     */
    @Bean
    public DataSource getDataSource() {
      DataSource dbsrc = null;
      try {
          // TODO 0 - Change the database driver, URL, username, and password in the following line

    	  	dbsrc = new SimpleDriverDataSource(new oracle.jdbc.OracleDriver(), "jdbc:oracle:thin:@localhost:1521:orcl", "hr", "orcl");
      } catch (Exception e) {
    	  e.printStackTrace();
      }      
      return dbsrc;
    }
    
    /**
     * The default DAO that accesses the database
     */
    @Bean
    public TestDAO getTestDAO() {
        return new TestDAOImpl(getDataSource());
    }

    /**
     * The default DAO that accesses the database
     */
    @Bean
    public Acm2013DAO getBasicDAO() {
    	// TODO 1 - Uncomment the following code, so that we use the actual DAO instead of the Mock  
        return new Acm2013DAOImpl(getDataSource());
        //return new Acm2013DAOMockImpl(getDataSource());
    }

    /**
     * A default Metrics registry.
     */
    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    /**
     * A default Health registry (part of Metrics).
     */
    @Bean
    public HealthCheckRegistry healthCheckRegistry() {
        return new HealthCheckRegistry();
    }


}
