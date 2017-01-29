package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorPartidasRMI extends Remote {
	public void nuevaPartida(int nf, int nc, int nb) throws RemoteException;
	
	public int pruebaCasilla(int f, int c) throws RemoteException;
	
	public String getBarco(int id) throws RemoteException;

	public String[] getsolucion() throws RemoteException;
}
