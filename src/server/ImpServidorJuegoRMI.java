package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import common.IntServidorJuegoRMI;

public class ImpServidorJuegoRMI extends UnicastRemoteObject implements IntServidorJuegoRMI  {
	private static final long serialVersionUID = 1L;
	private int port;

	protected ImpServidorJuegoRMI(int port) throws RemoteException {
		super( );
		this.port = port;
	}

	@Override
	public IntServidorPartidasRMI nuevoServidorPartidas() throws RemoteException {
		return new ImpServidorPartidasRMI(port);
	}

}
