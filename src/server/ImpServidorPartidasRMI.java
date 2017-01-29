package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ImpServidorPartidasRMI extends UnicastRemoteObject implements IntServidorPartidasRMI {
	private static final long serialVersionUID = 1L;
	private Partida partida = null;

	protected ImpServidorPartidasRMI(int port) throws RemoteException {
		super(port);
	}

	@Override
	public void nuevaPartida(int nf, int nc, int nb) throws RemoteException {
		partida = new Partida(nf,nc,nb);

	}

	@Override
	public int pruebaCasilla(int f, int c) throws RemoteException {
		return partida.pruebaCasilla(f,c);
	}

	@Override
	public String getBarco(int id) throws RemoteException {
		return partida.getBarco(id);
	}

	@Override
	public String[] getsolucion() throws RemoteException {
		return partida.getSolucion();
	}

}
