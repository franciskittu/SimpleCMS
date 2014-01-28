/*   SimpleCMS - un semplice content management system di pagine statiche
 *    Copyright (C) 2013  Francesco Proietti, Luca Traini
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package it.lufraproini.cms.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fsfskittu
 */
public class FormUtility {

    /*la funzione ritorna in una mappa tutti i campi che passano il check sui caratteri o che non ne hanno bisogno
    la mappa in argomento deve essere del tipo (nome_campo, caratteri_non_ammessi)
    */
    public static Map verificaCampiUrlEncoded(HttpServletRequest request, Map<String, String> caratteri_non_ammessi) {
        Map result = new HashMap();
        //per elaborare tutti i campi della request, ne richiedo la mappa
        Map m = request.getParameterMap();
        Set<Map.Entry<String, String[]>> fieldset = m.entrySet();

        for (Map.Entry<String, String[]> field : fieldset) {
            //itero sui vari campi
            String nome = field.getKey();

            if (caratteri_non_ammessi.containsKey(nome)) {
                //il valore, anche se semplice, è sempre un array
                String[] valori = field.getValue();

                if (valori.length > 0) {
                    if (valori.length == 1) {
                        if (!valori[0].isEmpty() && !valori[0].matches(caratteri_non_ammessi.get(nome))) {
                            result.put(nome, valori[0]);
                        }
                    } else {
                        for (int i = 0; i < valori.length; i++) {
                            if (!valori[i].isEmpty() && !valori[i].matches(caratteri_non_ammessi.get(nome))) {
                                result.put(nome + "_" + i, valori[i]);
                            }
                        }
                    }
                }
            } else {
                result.put(nome, field.getValue());
            }
        }
        return result;
    }
}
