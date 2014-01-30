/*filtro copiato dal prof. Della Penna*/
/*QUESTA CLASSE È DISABILITATA: CIOÈ NON È STATA AGGIUNTA AL DEPLOY DESCRIPTOR*/

/*
 * Questa classe implementa un Servlet Filter, cioè un oggetto che intercetta
 * e gestisce il traffico di dati in entrata e in uscita dalle servlet.
 * Perchè funzioni, deve essere configurata tramite il file web.xml.
 * Il filtro sostituisce la request che arriverà alla servlet
 * con una MultipartHttpServletRequest, nel caso in cui la request stessa
 * contenga dati codificati in multipart.
*/

package it.lufraproini.cms.config;



import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Giuseppe Della Penna
 */
public class MultipartFilter implements Filter {
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1) {
            MultipartHttpServletRequest mrequest = new MultipartHttpServletRequest(httprequest);
            //passo alla catena la mia ServletRequest "truccata" al posto dell'originale
            //pass to the filter chain (which ends with the servlet) my "advanced" servlet request
            chain.doFilter(mrequest, response);
        } else {
            //la catena dei filtri deve essere SEMPRE rispettata
            //the doFilter method MUST always end with a call to the chain's doFilter
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        config = null;
    }
}