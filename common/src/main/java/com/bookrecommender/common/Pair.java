package com.bookrecommender.common;

import java.io.Serializable;
import java.rmi.Remote;

// I campi del pair sono fissati a 'Serializable' e 'Remote' perché viene usato solo in questo modo
public record Pair<A extends Serializable, B extends Remote>(A first, B second) implements Serializable {}