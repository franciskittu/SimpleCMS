/*
 * ATTENZIONE: il codice di questa classe dipende dalla corretta definizione delle
 * risorse presente nei file context.xml (Resource) e web.xml (resource-ref)
 * 
 * Inoltre per fare la prova basta creare una tabella di prova e fare select * from tabella come query di prova
 * 
 */
package it.lufraproini.cms.servlet;

import it.lufraproini.cms.framework.result.HTMLResult;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public class EsempioDS extends HttpServlet {
    
    private void action_query(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Query executor with JDBC connection pooling");
        result.appendToBody("<form method='get' action='EsempioDS'>");
        result.appendToBody("<p>Write an SQL query: <input type='text' name='query'/>");
        result.appendToBody("<input type='submit' name='submit' value='execute'/></p>");
        result.appendToBody("</form>");
        result.activate(request, response);
    }

    private void action_error(HttpServletRequest request, HttpServletResponse response, String messaggio) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Query executor error");
        result.appendToBody("<h1>ERROR</h1>");
        result.appendToBody("<p>" + messaggio + "</p>");
        result.activate(request, response);
    }

    private void action_results(HttpServletRequest request, HttpServletResponse response, String query) throws SQLException, IOException, NamingException {
        int i;
        Statement s = null;
        ResultSet rs = null;
        Connection connection = null;

        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Query executor results");
        result.appendToBody("<p><b>Query:</b> " + query + "</p>");
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
            DataSource ds = (DataSource) ctx.lookup(getServletContext().getInitParameter("data.source"));
            //connessione al database locale
            //database connection
            connection = ds.getConnection();

            //eseguiamo la query
            //query execution
            s = connection.createStatement();
            rs = s.executeQuery(query);

            //in questo esempio, non conosciamo la struttura
            //dei record risultanti dalla query, quindi la esploriamo
            //utilizzando i metadati associati al ResultSet

            //in this particular example, we don't know the structure of the
            //returned records, so we use the ResultSet metadata to explore them
            ResultSetMetaData rsm = rs.getMetaData();

            result.appendToBody("<table border = '1'>");
            result.appendToBody("<tr>");
            for (i = 1; i <= rsm.getColumnCount(); ++i) {
                String cname = rsm.getColumnName(i);
                String clabel = rsm.getColumnLabel(i);
                String qcname = (rsm.getTableName(i).isEmpty()) ? cname : (rsm.getTableName(i) + "." + cname);
                String header = (cname.equals(clabel)) ? qcname : (clabel + " (" + qcname + ")");
                result.appendToBody("<th>" + header + "</th>");
            }
            result.appendToBody("</tr>");

            //iteriamo sulle righe del risultato
            //iterate on the result rows
            while (rs.next()) {
                result.appendToBody("<tr>");
                //in questo caso non preleviamo le colonne
                //usando i loro nomi, ma le leggiamo tutte
                //tramite l'indice
                //in this case we address the columns using their index
                //otherwise we could use their name
                for (i = 1; i <= rsm.getColumnCount(); ++i) {
                    result.appendToBody("<td>" + (rs.getObject(i) != null ? rs.getObject(i).toString() : "<i>null</i>") + "</td>");
                }
                result.appendToBody("</tr>");
            }
            result.appendToBody("</table>");
            result.activate(request, response);
        } finally {
            //alla fine chiudiamo tutte le risorse, in ogni caso
            //finally, we close all the opened resources
            try {
                rs.close();
                s.close();
            } catch (Exception ex) {
                //ignoriamo ulteriori errori in fase di chiusura
                //ignore further exceptions when closing 
            }
            try {
                connection.close();
            } catch (Exception ex) {
                //ignoriamo ulteriori errori in fase di chiusura
                //ignore further exceptions when closing 
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("query");
        try {
            if (query != null && !query.isEmpty()) {
                action_results(request, response, query);
            } else {
                action_query(request, response);
            }
        } catch (NamingException ex) {
            action_error(request, response, ex.getLocalizedMessage());
        } catch (SQLException ex) {
            action_error(request, response, ex.getLocalizedMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
