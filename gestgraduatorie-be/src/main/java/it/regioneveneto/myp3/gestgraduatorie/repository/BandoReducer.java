package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import it.regioneveneto.myp3.gestgraduatorie.model.Bando;


public class BandoReducer implements LinkedHashMapRowReducer<Integer, Bando> {

	@Override
	public void accumulate(final Map<Integer, Bando> map, final RowView rowView) {
        final Bando bando = map.computeIfAbsent(rowView.getColumn("idAnagraficaCittadino", Integer.class),
	            id -> rowView.getRow(Bando.class));
        /*
        if (rowView.getColumn("de_idSituazioneEconomica", Integer.class) != null) {
        	DatiEconomici datiEconomici = rowView.getRow(DatiEconomici.class);
        	anagraficaCittadino.setDatiEconomici(datiEconomici);
        }
        
        if (rowView.getColumn("dc_idDatiCatastali", Integer.class) != null) {
        	DatiCatastali datiCatastali = rowView.getRow(DatiCatastali.class);
        	anagraficaCittadino.setDatiCatastali(datiCatastali);
        }
        int idFragilita = 0;
        if (rowView.getColumn("fr_id_fragilita", Integer.class) != null) {
        	idFragilita = rowView.getColumn("fr_id_fragilita", Integer.class);
        	boolean found = false;
        	for (Fragilita fr : anagraficaCittadino.getFragilita()) {
				if(idFragilita==fr.getIdFragilita()) {
					found = true;
					break;
				}
			}
        	if(!found) {
	        	Fragilita fragilita = rowView.getRow(Fragilita.class);
		        if (rowView.getColumn("mis_id_misura", Integer.class) != null) {
		        	Misura misura = rowView.getRow(Misura.class);
		        	fragilita.setMisura(misura);
		        }
		        anagraficaCittadino.addFragilita(fragilita);
        	}
        }
        int idCareGiver = 0;
        if (rowView.getColumn("car_id_care_giver", Integer.class) != null) {
        	idCareGiver = rowView.getColumn("car_id_care_giver", Integer.class);
        	boolean found = false;
        	for (CareGiver cg : anagraficaCittadino.getCareGiver()) {
				if(idCareGiver==cg.getIdCareGiver()) {
					found = true;
					break;
				}
			}
        	if(!found) {
	        	CareGiver careGiver = rowView.getRow(CareGiver.class);
	        	anagraficaCittadino.addCareGiver(careGiver);
        	}
        }
        */
	}

}
