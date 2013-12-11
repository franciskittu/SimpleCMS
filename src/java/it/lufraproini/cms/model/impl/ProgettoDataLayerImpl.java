package it.lufraproini.cms.model.impl;

import it.lufraproini.cms.model.ProgettoDataLayer;
import it.lufraproini.cms.model.Tab;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fsfskittu
 */

public class ProgettoDataLayerImpl implements ProgettoDataLayer{
    private Statement g_all_table;
    private PreparedStatement gTab;
    private String query_g_all_table;
    
    public ProgettoDataLayerImpl(Connection connection) throws SQLException {
        query_g_all_table = "SELECT * FROM tab";
        g_all_table = connection.createStatement();
        gTab = connection.prepareStatement("SELECT * FROM tab WHERE chiave=?");
    }
    
    public Tab createTab(){
        return new TabImpl(this);
    }
    
    public Tab getTab(Integer chiave){
        Tab result = null;
        ResultSet rs = null;
        try{
            gTab.setInt(1,chiave);
            rs = gTab.executeQuery();
            if(rs.next()){
                result = new TabImpl(this, rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(ProgettoDataLayer.class.getName()).log(Level.SEVERE,null,ex);
            } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return result;
    }
    
    public List<Tab> getAllTab(){
        List<Tab> result = new ArrayList();
        ResultSet rs = null;
        try{
            rs = this.g_all_table.executeQuery(query_g_all_table);
            while(rs.next()){
                result.add(new TabImpl(this, rs));
            }
        }catch (SQLException ex){
            Logger.getLogger(ProgettoDataLayer.class.getName()).log(Level.SEVERE,null,ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return result;
    }
    
    public Tab deleteTab(Tab tab){
        
    }
    
    public Tab addTab(Tab tab){
        
    }
}
