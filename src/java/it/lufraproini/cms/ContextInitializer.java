package it.lufraproini.cms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class ContextInitializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        try {
            //preleviamo un riferimento al naming context
            //get a reference to the naming context
            InitialContext ctx = new InitialContext();
            //e da questo otteniamo un riferimento alla DataSource
            //che gestisce il pool di connessioni. 
            //usiamo un parametro del contesto per ottenere il nome della sorgente dati (vedi web.xml)

            //and from the context we get a reference to the DataSource
            //that manages the connection pool
            //we also use a context parameter to obtain the data source name (see web.xml)                     
            DataSource ds = (DataSource) ctx.lookup(sce.getServletContext().getInitParameter("data.source"));

            //scriviamo un riferimento al DataSource tra gli attributi del contesto
            //put a reference to the DataSource in the context attributes
            sce.getServletContext().setAttribute("datasource", ds);
        } catch (NamingException ex) {
            Logger.getLogger(ContextInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //niente da fare (la datasource si chiude da sola)
        //nothing to do (datasources are closed automatically))
    }
}