package com.bookrecommender.common;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Classe di utilità (Record) che rappresenta una coppia immutabile di oggetti tipizzati.
 * <p>
 * Questa implementazione è specifica per l'architettura RMI del progetto:
 * </p>
 * <ul>
 * <li>Il tipo generico <code>A</code> deve essere <code>Serializable</code> (usato per Enums o DTO).</li>
 * <li>Il tipo generico <code>B</code> deve essere <code>Remote</code> (usato per restituire le interfacce dei servizi).</li>
 * </ul>
 * <p>
 * Viene utilizzata principalmente durante il login e la registrazione per restituire in un solo passaggio
 * sia l'esito dell'operazione che il riferimento al nuovo servizio remoto.
 * </p>
 *
 * @param <A>    il tipo del primo elemento (vincolo: deve essere serializzabile)
 * @param <B>    il tipo del secondo elemento (vincolo: deve essere un'interfaccia remota)
 * @param first  il primo oggetto della coppia
 * @param second il secondo oggetto della coppia
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public record Pair<A extends Serializable, B extends Remote>(A first, B second) implements Serializable {}