package it.regioneveneto.myp3.gestgraduatorie.utils;

import java.util.Collection;

/**
 * Classe di utilit� per le liste.
 */
public abstract class ListUtils {
	
	/**
	 * Verifica se lista e' non nulla e non vuota.
	 * @param list la lista da controllare
	 * @return true se lista possiede almeno un elemento
	 */
	public static boolean isNotEmpty( Collection<?> list ){
		return list != null && list.isEmpty( ) == false;
	}
	
	/**
	 * Verifica se un array e' non nullo e non vuoto.
	 * @param array e' l'array da controllare
	 * @return true se l'array possiede almeno un elemento
	 */
	public static boolean isNotEmpty( Object[] array ){
		return array != null && array.length > 0 == true;
	}
	
	/**
	 * Verifica se la lista e' null oppure vuota
	 * @param list la lista da controllare
	 * @return true se la lista e' nulla oppure vuota
	 */
	public static boolean isEmpty(Collection<?> list){
		return isNotEmpty( list ) == false;
	}
	
	/**
	 * Verifica se l'array e' null oppure vuoto
	 * @param array l'array da controllare
	 * @return true se l'array e' nullo oppure vuoto
	 */
	public static boolean isEmpty(Object[] array){
		return isNotEmpty( array ) == false;
	}
	
	/**
	 * Restituisce la dimensione della lista.
	 * @param list la lista
	 * @return le dimensioni dell lista previo check not empty
	 */
	public static int size( Collection<?> list ){
		return isNotEmpty( list ) ? list.size( ) : 0;
	}

}
