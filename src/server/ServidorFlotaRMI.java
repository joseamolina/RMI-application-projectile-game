package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class ServidorFlotaRMI {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int numPuerto; 
		String registroURL;
		try{     
			System.out.println("Introduce el puerto del registro: ");
			
			numPuerto = sc.nextInt();
			if (System.getSecurityManager()==null)
				System.setSecurityManager(new SecurityManager());
			lanzaRegistro(numPuerto);
			registroURL = "rmi://localhost:" + numPuerto + "/ServidorFlota";
			System.out.println("Servidor registrado:");
			ImpServidorJuegoRMI iS = new ImpServidorJuegoRMI(numPuerto);
			Naming.rebind(registroURL, iS);
			listarRegistros(registroURL);
			System.out.println("¡Servidor listo!");
			sc.close();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Hubo un problema a la hora de lanzar el registro");
		}

	}
	
	private static void listarRegistros(String reg) throws RemoteException, MalformedURLException{
		System.out.println("Registry " + reg + " contains: ");
	    String [ ] names = Naming.list(reg);
	    for (int i=0; i < names.length; i++)
	    System.out.println(names[i]);
		
	}

	private static void lanzaRegistro(int nP) throws RemoteException{
		try {
	         Registry registry = 
	         LocateRegistry.getRegistry(nP);
	         registry.list( );
	      }
	      catch (RemoteException e) { 
	         System.out.println("No se puede localizar el registro en el puerto: " + nP);
	         Registry registry =  LocateRegistry.createRegistry(nP);
	         System.out.println("Registro creado en el puerto: " + nP);
	      }
	}

}
