package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.IntServidorPartidasRMI;

public interface IntServidorJuegoRMI extends Remote {

	public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException;
}
