package it.regioneveneto.myp3.gestgraduatorie.repository;

import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import it.regioneveneto.myp3.gestgraduatorie.model.Allegato;
import it.regioneveneto.myp3.gestgraduatorie.model.Istanza;
import it.regioneveneto.myp3.gestgraduatorie.model.StatoBando;
import it.regioneveneto.myp3.gestgraduatorie.model.TipologiaBando;


public class RichiestaReducer implements LinkedHashMapRowReducer<Integer, Istanza> {

	@Override
	public void accumulate(final Map<Integer, Istanza> map, final RowView rowView) {
        final Istanza richiesta = map.computeIfAbsent(rowView.getColumn("ist_id", Integer.class),
	            id -> rowView.getRow(Istanza.class));
        
        int idAllegato = 0;
        if (rowView.getColumn("all_id", Integer.class) != null) {
        	idAllegato = rowView.getColumn("all_id", Integer.class);
        	boolean found = false;
        	for (Allegato all : richiesta.getAllegati()) {
				if(idAllegato==all.getId()) {
					found = true;
					break;
				}
			}
        	if(!found) {
	        	Allegato allegato = rowView.getRow(Allegato.class);
	        	richiesta.addAllegato(allegato);
        	}
        }
        
        if (rowView.getColumn("tb_id", Integer.class) != null) {
        	TipologiaBando tipologiaBando = rowView.getRow(TipologiaBando.class);
        	richiesta.setTipoBando(tipologiaBando);
        }
        if (rowView.getColumn("sb_id", Integer.class) != null) {
        	StatoBando statoBando = rowView.getRow(StatoBando.class);
        	richiesta.setStatoBando(statoBando);
        }
        /*
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
