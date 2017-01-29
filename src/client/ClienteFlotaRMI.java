package client;

import java.rmi.Naming;
import java.util.Scanner;

import common.IntServidorJuegoRMI;



public class ClienteFlotaRMI {

	public static void main(String[] args) {
		try {
		      int puertoRMI;         
		      String hostName;
		      Scanner sc = new Scanner(System.in);
		      System.out.println("Introduce el nombre del host :");
		      hostName = sc.nextLine();
		      System.out.println("Introduce el puerto del registro: ");
		      puertoRMI = Integer.parseInt(sc.nextLine());
		      if (System.getSecurityManager()==null)  
		    	  System.setSecurityManager(new SecurityManager());
		      String registryURL = "rmi://"+hostName +":"+ puertoRMI + "/ServidorFlota";  
		      IntServidorJuegoRMI h = (IntServidorJuegoRMI) Naming.lookup(registryURL);
		      System.out.println("Lookup completado " );
		      new Juego(h);
		      sc.close();
		    } // end try 
		    catch (Exception e) {
		      System.out.println(
		        "Excepcion en clienteflotaRMI: " + e);
		    } 

	}

}
